package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.Collections;
import java.util.List;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.RelatedItem;

public class RelatedItemsBuilder {
    private List<Item> constituents = Collections.emptyList();
    private List<Item> hosts = Collections.emptyList();

    public RelatedItemsBuilder withRelatedItems(RelatedItems relatedItems) {
        if (relatedItems != null) {
            constituents = relatedItems.getConstituents();
            hosts = relatedItems.getHosts();
        }
        return this;
    }

    public RelatedItem build() {
        RelatedItem relatedItem = new RelatedItem();
        for(Item item : constituents) {
            ItemResource itemResource = new ItemResultResourceAssembler().toResource(item);
            relatedItem.getConstituents().add(itemResource);
        }
        for(Item item : hosts) {
            ItemResource itemResource = new ItemResultResourceAssembler().toResource(item);
            relatedItem.getHosts().add(itemResource);
        }
        
        return relatedItem;
    }

}
