package no.nb.microservices.catalogitem.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogitem.core.content.service.ContentSearchService;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.search.exception.LatchException;
import no.nb.microservices.catalogitem.core.search.model.*;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.AggregationResource;
import no.nb.microservices.catalogsearchindex.FacetValueResource;
import org.apache.htrace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class SearchServiceImpl implements ISearchService {
    private static final Logger LOG = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final IndexService indexService;
    private final ItemWrapperService itemWrapperService;
    private final ContentSearchService contentSearchService;

    @Autowired
    public SearchServiceImpl(ItemWrapperService itemWrapperService, IndexService indexService, ContentSearchService contentSearchService) {
        this.itemWrapperService = itemWrapperService;
        this.indexService = indexService;
        this.contentSearchService = contentSearchService;
    }

    @Override
    public SearchAggregated search(SearchRequest searchRequest, Pageable pageable) {
        SearchResult result = indexService.search(searchRequest, pageable, new SecurityInfo());
        List<Item> items = consumeItems(searchRequest, result);
        Page<Item> page = new PageImpl<>(items, pageable, result.getTotalElements());
        return new SearchAggregated(page, result.getAggregations(), result.getScrollId(), searchRequest);
    }

    @Override
    public SuperSearchAggregated superSearch(SuperSearchRequest superSearchRequest, Pageable pageable) {
        List<String> possibleMediaTypesToSearch = getPossibleMediaTypesToSearch(superSearchRequest);
        SecurityInfo securityInfo = getSecurityInfo();

        Map<String, SearchAggregated> searchAggregateds = new HashMap<>();
        List<String> wantedMediaTypes = superSearchRequest.getWantedMediaTypes(possibleMediaTypesToSearch);
        for (String mediaType : wantedMediaTypes) {
            searchAggregateds.put(mediaType, searchForMediaTypes(mediaType, pageable, superSearchRequest, securityInfo));
        }

        List<String> otherMediaTypes = superSearchRequest.getOtherMediaTypes(possibleMediaTypesToSearch, wantedMediaTypes);
        if(!otherMediaTypes.isEmpty()) {
            searchAggregateds.put("other", searchForOtherMediaTypes(pageable, superSearchRequest, otherMediaTypes));
        }

        return new SuperSearchAggregated(searchAggregateds);
    }

    private SearchAggregated searchForMediaTypes(String mediaType, Pageable pageable, SuperSearchRequest superSearchRequest, SecurityInfo securityInfo) {
        SearchRequest mediaTypeSearchRequest = createNewSearchRequestInstance(superSearchRequest);
        mediaTypeSearchRequest.setFilter(new String[]{"mediatype:" + mediaType});

        SearchAggregated search = search(mediaTypeSearchRequest, new PageRequest(0, pageable.getPageSize()));

        if("aviser".equalsIgnoreCase(mediaType)) {
            search.setContentSearches(getContentSearchs(search.getPage().getContent(), mediaTypeSearchRequest.getQ(), securityInfo));
        }
        return search;
    }

    private SearchAggregated searchForOtherMediaTypes(Pageable pageable, SuperSearchRequest superSearchRequest, List<String> otherMediaTypes) {
        SearchRequest otherSearchRequest = createNewSearchRequestInstance(superSearchRequest);
        otherSearchRequest.setQ(superSearchRequest.getQ() + " AND (" + String.join(" OR ", otherMediaTypes) + ")");

        return search(otherSearchRequest, new PageRequest(0, pageable.getPageSize()));
    }

    private SearchRequest createNewSearchRequestInstance(SearchRequest searchRequest) {
        try {
            return (SearchRequest)searchRequest.clone();
        } catch (CloneNotSupportedException e) {
            LOG.error("Cant clone searchRequest", e);
        }
        SearchRequest newSearchRequest = new SearchRequest();
        newSearchRequest.setQ(searchRequest.getQ());
        return newSearchRequest;
    }

    private SecurityInfo getSecurityInfo() {
        SecurityInfo securityInfo = new SecurityInfo();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        securityInfo.setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        securityInfo.setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        securityInfo.setxRealIp(UserUtils.getClientIp(request));
        securityInfo.setSsoToken(UserUtils.getSsoToken(request));
        return securityInfo;
    }

    private List<ContentSearch> getContentSearchs(List<Item> content, String searchQuery, SecurityInfo securityInfo) {
        List<Future<ContentSearch>> futureContentSearches = new ArrayList<>();
        for(Item item : content) {
            TracableId tracableId = new TracableId(Trace.currentSpan(), item.getId(), securityInfo);
            futureContentSearches.add(contentSearchService.search(searchQuery, tracableId));
        }

        for(Future<ContentSearch> contentSearchFuture : futureContentSearches) {
            while(!contentSearchFuture.isDone()) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException ex) {
                    LOG.error("Interrupted", ex);
                }
            }
        }

        List<ContentSearch> contentSearches = new ArrayList<>();
        for (Future<ContentSearch> futureContentSearch : futureContentSearches) {
            try {
                ContentSearch contentSearch = futureContentSearch.get();
                if(contentSearch != null) {
                    contentSearches.add(contentSearch);
                }
            } catch (InterruptedException | ExecutionException e) {
                LOG.debug("Cant get contentSearch", e);
                LOG.error("Cant get contentSearch", e.getMessage());
            }
        }
        return contentSearches;
    }

    private List<String> getPossibleMediaTypesToSearch(SearchRequest searchRequest) {
        searchRequest.setAggs("mediatype");
        SearchAggregated search = search(searchRequest, new PageRequest(0, 1));

        List<String> mediaTypes = new ArrayList<>();
        List<AggregationResource> aggregations = search.getAggregations();
        for(AggregationResource aggregationResource : aggregations) {
            if("mediatype".equalsIgnoreCase(aggregationResource.getName())) {
                for (FacetValueResource facetValueResource : aggregationResource.getFacetValues()) {
                    mediaTypes.add(facetValueResource.getKey().toLowerCase());
                }
            }
        }
        searchRequest.setAggs(null);
        return mediaTypes;
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
                LOG.error("Interrupted", ex);
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
