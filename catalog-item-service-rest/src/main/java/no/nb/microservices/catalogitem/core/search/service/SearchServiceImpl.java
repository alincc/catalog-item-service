package no.nb.microservices.catalogitem.core.search.service;

import no.nb.commons.web.util.UserUtils;
import no.nb.commons.web.xforwarded.feign.XForwardedFeignInterceptor;
import no.nb.microservices.catalogitem.core.index.model.SearchResult;
import no.nb.microservices.catalogitem.core.index.service.IndexService;
import no.nb.microservices.catalogitem.core.item.service.ItemWrapperService;
import no.nb.microservices.catalogitem.core.item.service.SecurityInfo;
import no.nb.microservices.catalogitem.core.search.exception.LatchException;
import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;
import no.nb.microservices.catalogitem.core.search.model.SearchAggregated;
import no.nb.microservices.catalogitem.core.search.model.SearchRequest;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

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
        List<ItemResource> items = consumeItems(result);
        return null;
    }

    private List<ItemResource> consumeItems(SearchResult result) {
        final CountDownLatch latch = new CountDownLatch(result.getIds().size());
        List<ItemResource> items = Collections.synchronizedList(new ArrayList<>());
        List<Future<ItemResource>> workList = new ArrayList<>();

        for (String id : result.getIds()) {
            ItemWrapper itemWrapper = createItemWrapper(latch, items, id);
            Future<ItemResource> item = itemWrapperService.getById(itemWrapper);
            workList.add(item);
        }

        waitForAllItemsToFinish(latch);

        for (Future<ItemResource> item : workList) {
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
            latch.wait();
        } catch (InterruptedException ex) {
            throw new LatchException(ex);
        }
    }

    private ItemWrapper createItemWrapper(final CountDownLatch latch, List<ItemResource> items, String id) {
        ItemWrapper itemWrapper = new ItemWrapper(id, latch, items);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        itemWrapper.getSecurityInfo().setxHost(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_HOST));
        itemWrapper.getSecurityInfo().setxPort(request.getHeader(XForwardedFeignInterceptor.X_FORWARDED_PORT));
        itemWrapper.getSecurityInfo().setxRealIp(UserUtils.getClientIp(request));
        itemWrapper.getSecurityInfo().setSsoToken(UserUtils.getSsoToken(request));
        itemWrapper.setSpan(Trace.currentSpan());

        return itemWrapper;
    }

}
