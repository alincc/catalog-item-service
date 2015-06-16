package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;

public interface IItemService {

    Item getItemById(String id);
    
}
