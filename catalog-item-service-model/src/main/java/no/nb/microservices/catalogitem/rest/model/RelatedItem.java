package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedItem {
    private List<ItemResource> hosts = new ArrayList<>();
    private List<ItemResource> constituents = new ArrayList<>();
    private ItemResource preceding;
    private ItemResource succeding;

    public List<ItemResource> getHosts() {
        return hosts;
    }

    public void setHosts(List<ItemResource> hosts) {
        this.hosts = hosts;
    }

    public List<ItemResource> getConstituents() {
        return constituents;
    }

    public void setConstituents(List<ItemResource> constituents) {
        this.constituents = constituents;
    }

    public ItemResource getPreceding() {
        return preceding;
    }

    public void setPreceding(ItemResource preceding) {
        this.preceding = preceding;
    }

    public ItemResource getSucceding() {
        return succeding;
    }

    public void setSucceding(ItemResource succeding) {
        this.succeding = succeding;
    }
}
