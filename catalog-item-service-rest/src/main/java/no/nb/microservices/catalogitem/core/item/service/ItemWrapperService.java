package no.nb.microservices.catalogitem.core.item.service;

import no.nb.microservices.catalogitem.core.search.model.ItemWrapper;
import no.nb.microservices.catalogitem.rest.model.ItemResource;

import java.util.concurrent.Future;

public interface ItemWrapperService {

    Future<ItemResource> getById(ItemWrapper itemWrapper);

}
