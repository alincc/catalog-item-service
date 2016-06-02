package no.nb.microservices.catalogitem.core.search.service;

import no.nb.microservices.catalogitem.core.content.service.ContentSearchService;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.item.service.TracableId;
import no.nb.microservices.catalogitem.core.search.exception.LatchException;
import no.nb.microservices.catalogitem.core.search.model.*;
import no.nb.microservices.catalogitem.core.utils.SecurityInfoService;
import no.nb.microservices.catalogitem.rest.model.ContentSearch;
import no.nb.microservices.catalogsearchindex.BucketValue;
import no.nb.microservices.catalogsearchindex.ItemResource;
import no.nb.microservices.catalogsearchindex.SimpleAggregation;
import org.apache.htrace.Trace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.stereotype.Service;

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
        SearchAggregated result = doAggsSearch(superSearchRequest);
        if (result != null) {
            List<String> possibleMediaTypesToSearch = getPossibleMediaTypesToSearch(result.getAggregations());
            SecurityInfo securityInfo = new SecurityInfoService().getSecurityInfo();

            Map<String, SearchAggregated> searchAggregateds = new HashMap<>();
            List<String> wantedMediaTypes = superSearchRequest.getWantedMediaTypes(possibleMediaTypesToSearch);
            for (String mediaType : wantedMediaTypes) {
                searchAggregateds.put(mediaType, searchForMediaTypes(mediaType, pageable, superSearchRequest, securityInfo));
            }

            List<String> otherMediaTypes = superSearchRequest.getOtherMediaTypes(possibleMediaTypesToSearch, wantedMediaTypes);
            if (!otherMediaTypes.isEmpty()) {
                searchAggregateds.put("other", searchForOtherMediaTypes(pageable, superSearchRequest, otherMediaTypes));
            }

            PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(result.getPage().getSize(), result.getPage().getNumber(), result.getPage().getTotalElements(), result.getPage().getTotalPages());
            return new SuperSearchAggregated(pageMetadata, searchAggregateds);
        }
        return new SuperSearchAggregated(new PagedResources.PageMetadata(0, 0, 0), new HashMap<>());
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

    private ItemWrapper createItemWrapper(final CountDownLatch latch, List<Item> items, ItemResource itemResource, SearchRequest searchRequest) {
        ItemWrapper itemWrapper = new ItemWrapper(itemResource, latch, items, searchRequest);
        itemWrapper.setSecurityInfo(new SecurityInfoService().getSecurityInfo());
        itemWrapper.setSpan(Trace.currentSpan());

        return itemWrapper;
    }

    private void waitForAllItemsToFinish(final CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new LatchException(ex);
        }
    }

    private SearchAggregated doAggsSearch(SearchRequest searchRequest) {
        try {
            SearchRequest aggsSearchRequest = (SearchRequest) searchRequest.clone();
            aggsSearchRequest.setAggs("mediatype:20");
            return search(aggsSearchRequest, new PageRequest(0, 1));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getPossibleMediaTypesToSearch(List<SimpleAggregation> aggregations) {
        List<String> mediaTypes = new ArrayList<>();
        if (aggregations != null) {
            for (SimpleAggregation aggregationResource : aggregations) {
                if ("mediatype".equalsIgnoreCase(aggregationResource.getName())) {
                    for (BucketValue facetValueResource : aggregationResource.getBuckets()) {
                        mediaTypes.add(facetValueResource.getKey().toLowerCase());
                    }
                }
            }
        }

        return mediaTypes;
    }

    private SearchAggregated searchForMediaTypes(String mediaType, Pageable pageable, SuperSearchRequest superSearchRequest, SecurityInfo securityInfo) {
        SearchRequest mediaTypeSearchRequest = createNewSearchRequestInstance(superSearchRequest);
        addMediaTypeFilter(mediaType, mediaTypeSearchRequest);

        SearchAggregated search = search(mediaTypeSearchRequest, new PageRequest(0, pageable.getPageSize()));

        if("aviser".equalsIgnoreCase(mediaType)) {
            search.setContentSearches(getContentSearchs(search.getPage().getContent(), mediaTypeSearchRequest.getQ(), securityInfo));
        }
        return search;
    }

    private void addMediaTypeFilter(String mediaType, SearchRequest mediaTypeSearchRequest) {
        int numOfFilters = mediaTypeSearchRequest.getFilter().size();
        String[] filters = new String[numOfFilters + 1];
        for (int i = 0; i < numOfFilters; i++) {
            filters[i] = mediaTypeSearchRequest.getFilter().get(i);
        }
        filters[numOfFilters] = "mediatype:" + mediaType;
        mediaTypeSearchRequest.setFilter(filters);
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
}
