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
    private Item preceding;
    private Item succeding;

    public RelatedItemsBuilder withRelatedItems(RelatedItems relatedItems) {
        if (relatedItems != null) {
            constituents = relatedItems.getConstituents();
            hosts = relatedItems.getHosts();
            preceding = relatedItems.getPreceding();
            succeding = relatedItems.getSucceding();
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

        if (preceding != null) {
            relatedItem.setPreceding(new ItemResultResourceAssembler().toResource(preceding));
        }
        if (succeding != null) {
            relatedItem.setSucceding(new ItemResultResourceAssembler().toResource(succeding));
        }

        return relatedItem;
    }

}
