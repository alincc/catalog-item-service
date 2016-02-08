package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;
import org.apache.htrace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class ItemWrapperServiceImpl implements ItemWrapperService {
    final ItemService itemService;

    @Autowired
    public ItemWrapperServiceImpl(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    @Async
    public Future<Item> getById(ItemWrapper itemWrapper) {
        Item item = null;
        try {
            SecurityInfo securityInfo = itemWrapper.getSecurityInfo();

            Trace.continueSpan(itemWrapper.getSpan());
            if (itemWrapper.getItemResource() == null) {
                item = itemService.getItemById(itemWrapper.getItemResource().getItemId(), itemWrapper.getSearchRequest().getFields(), "", securityInfo);
            } else {
                item = itemService.getItemWithResource(itemWrapper.getItemResource(), itemWrapper.getSearchRequest().getFields(), "", securityInfo);
            }


        } finally {
            itemWrapper.getLatch().countDown();
        }
        return new AsyncResult<>(item);
    }

}
