package no.nb.microservices.catalogitem.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.search.exception.LatchException;
import no.nb.microservices.catalogitem.core.search.model.*;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.FacetValueResource;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements ISearchService {
    private final IndexService indexService;
    private final ItemWrapperService itemWrapperService;

    @Autowired
    public SearchServiceImpl(ItemWrapperService itemWrapperService, IndexService indexService) {
        this.itemWrapperService = itemWrapperService;
        this.indexService = indexService;
    }

    @Override
    public SearchAggregated search(SearchRequest searchRequest, Pageable pageable) {
        SearchResult result = indexService.search(searchRequest, pageable, new SecurityInfo());
        List<Item> items = consumeItems(searchRequest, result);
        Page<Item> page = new PageImpl<>(items, pageable, result.getTotalElements());
        return new SearchAggregated(page, result.getAggregations(), result.getScrollId(), searchRequest);
    }

    @Override
    public SuperSearchAggregated superSearch(SearchRequest searchRequest, Pageable pageable) {
        searchRequest.setAggs("mediatype");
        SearchAggregated search = search(searchRequest, new PageRequest(0, 1));

        searchRequest.setAggs(null);
        List<AggregationResource> aggregations = search.getAggregations();
        Map<String, SearchAggregated> searchAggregateds = new HashMap<>();
        List<String> otherMediaTypes = new ArrayList<>();

        List<String> wantedMediaTypes = searchRequest.getMediatypes().stream().map(String::toLowerCase).collect(Collectors.toList());

        for(AggregationResource aggregationResource : aggregations) {
            if("mediatype".equalsIgnoreCase(aggregationResource.getName())) {
                for (FacetValueResource facetValueResource : aggregationResource.getFacetValues()) {
                    String mediaType = facetValueResource.getKey().toLowerCase();
                    if(searchRequest.getMediatypes().size() == 0 || wantedMediaTypes.contains(mediaType)) {

                        SearchRequest mediaTypeSearchRequest = new SearchRequestBuilder(searchRequest)
                                .withFilter(new String[]{"mediatype:" + mediaType})
                                .build();

                        searchAggregateds.put(mediaType, search(mediaTypeSearchRequest, new PageRequest(0, pageable.getPageSize())));
                    } else {
                        otherMediaTypes.add("mediatype:" + mediaType);
                    }
                }
            }
        }

        if(!otherMediaTypes.isEmpty()) {
            String join = String.join(" OR ", otherMediaTypes);
            SearchRequest otherSearchRequest = new SearchRequest();
            otherSearchRequest.setQ(searchRequest.getQ() + " AND (" + join + ")");
            searchAggregateds.put("other", search(otherSearchRequest, new PageRequest(0, pageable.getPageSize())));
        }

        return new SuperSearchAggregated(searchAggregateds);
    }

    private List<Item> consumeItems(SearchRequest searchRequest, SearchResult result) {
        final CountDownLatch latch = new CountDownLatch(result.getItems().size());
        List<Item> items = Collections.synchronizedList(new ArrayList<>());
        List<Future<Item>> workList = new ArrayList<>();

        for (ItemResource itemResource : result.getItems()) {

            ItemWrapper itemWrapper = createItemWrapper(latch, items, itemResource, searchRequest);
            Future<Item> item = itemWrapperService.getById(itemWrapper);
            workList.add(item);
        }

        waitForAllItemsToFinish(latch);

        for (Future<Item> item : workList) {
            try {
                items.add(item.get());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return items;
    }

    private void waitForAllItemsToFinish(final CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new LatchException(ex);
        }
    }

    private ItemWrapper createItemWrapper(final CountDownLatch latch, List<Item> items, ItemResource itemResource, SearchRequest searchRequest) {
        ItemWrapper itemWrapper = new ItemWrapper(itemResource, latch, items, searchRequest);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        itemWrapper.getSecurityInfo().setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        itemWrapper.getSecurityInfo().setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        itemWrapper.getSecurityInfo().setxRealIp(UserUtils.getClientIp(request));
        itemWrapper.getSecurityInfo().setSsoToken(UserUtils.getSsoToken(request));
        itemWrapper.setSpan(Trace.currentSpan());

        return itemWrapper;
    }

}
