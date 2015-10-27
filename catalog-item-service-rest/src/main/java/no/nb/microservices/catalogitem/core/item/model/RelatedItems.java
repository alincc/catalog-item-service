package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogitem.rest.model.ItemResource;

import java.util.Collections;
import java.util.List;

public class RelatedItems {

    private List<Item> constituents;
    private List<Item> hosts;
    private Item preceding;
    private Item succeding;

    public RelatedItems(List<Item> constituents, List<Item> hosts) {
        super();
        this.constituents = constituents;
        this.hosts = hosts;
    }

    public RelatedItems(List<Item> constituents, List<Item> hosts, Item preceding, Item succeding) {
        this.constituents = constituents;
        this.hosts = hosts;
        this.preceding = preceding;
        this.succeding = succeding;
    }

    public List<Item> getConstituents() {
        if (constituents == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(constituents);
        }
    }

    public List<Item> getHosts() {
        if (hosts == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(hosts);
        }
    }

    public Item getPreceding() {
        return preceding;
    }

    public Item getSucceding() {
        return succeding;
    }
}
