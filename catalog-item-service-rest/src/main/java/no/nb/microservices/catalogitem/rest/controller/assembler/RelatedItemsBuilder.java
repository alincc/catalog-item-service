package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.item.model.RelatedItems;
import no.nb.microservices.catalogitem.rest.model.ItemResource;
import no.nb.microservices.catalogitem.rest.model.RelatedItemResource;

public class RelatedItemsBuilder {
    private RelatedItems relatedItems;
    
    public RelatedItemsBuilder withRelatedItems(RelatedItems relatedItems) {
        this.relatedItems = relatedItems;
        return this;
    }

    public RelatedItemResource build() {
        if (relatedItems != null) {
            List<ItemResource> constituents = getConstituents();
            List<ItemResource> hosts = getHosts();
            ItemResource preceding = createPreceding();
            ItemResource succeding = createSucceding();
    
            if (!hosts.isEmpty() || !constituents.isEmpty() || preceding != null || succeding != null) {
                return new RelatedItemResource(hosts, constituents, preceding, succeding);
            }
        }
        return null;
    }

    private List<ItemResource> getConstituents() {
        List<ItemResource> constituents = new ArrayList<>();
        for(Item item : relatedItems.getConstituents()) {
            ItemResource itemResource = new ItemResultResourceAssembler().toResource(item);
            constituents.add(itemResource);
        }
        return constituents;
    }
    
    private List<ItemResource> getHosts() {
        List<ItemResource> hosts = new ArrayList<>();
        for(Item item : relatedItems.getHosts()) {
            ItemResource itemResource = new ItemResultResourceAssembler().toResource(item);
            hosts.add(itemResource);
        }
        return hosts;
    }
    
    private ItemResource createPreceding() {
        if (relatedItems.getPreceding() != null) {
            return new ItemResultResourceAssembler().toResource(relatedItems.getPreceding());
        } else {
            return null;
        }
    }
    
    private ItemResource createSucceding() {
        if (relatedItems.getSucceding() != null) {
            return new ItemResultResourceAssembler().toResource(relatedItems.getSucceding());
        } else {
            return null;
        }
    }

}
