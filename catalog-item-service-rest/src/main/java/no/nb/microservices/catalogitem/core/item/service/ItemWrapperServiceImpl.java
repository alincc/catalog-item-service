package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;
import no.nb.microservices.catalogitem.rest.controller.assembler.ItemResultResourceAssembler;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
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
    public Future<ItemResource> getById(ItemWrapper itemWrapper) {
        ItemResource itemResource = null;
        try {
            SecurityInfo securityInfo = itemWrapper.getSecurityInfo();

            Trace.continueSpan(itemWrapper.getSpan());
            Item item = itemService.getItemById(itemWrapper.getId(), "");
            itemResource = new ItemResultResourceAssembler().toResource(item);


        } finally {
            itemWrapper.getLatch().countDown();
        }
        return new AsyncResult<>(itemResource);
    }

}
