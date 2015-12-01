package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;

public interface ItemService {

    Item getItemById(String id, String fields, String expand);
    Item getItemById(String id, String fields, String expand, SecurityInfo securityInfo);   
}
