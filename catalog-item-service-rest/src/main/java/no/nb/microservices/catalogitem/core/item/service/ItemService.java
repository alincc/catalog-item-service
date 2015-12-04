package no.nb.microservices.catalogitem.core.item.service;

import java.util.List;

import no.nb.microservices.catalogitem.core.item.model.Item;

public interface ItemService {

    Item getItemById(String id, List<String> fields, String expand);
    Item getItemById(String id, List<String> fields, String expand, SecurityInfo securityInfo);   
}
