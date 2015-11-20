package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;

import java.util.concurrent.Future;

public interface ItemWrapperService {

    Future<Item> getById(ItemWrapper itemWrapper);

}
