package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedItemResource extends ResourceSupport {
    @JsonProperty(value="id")
    private String id;
    private List<ItemResource> hosts = new ArrayList<>();
    private List<ItemResource> constituents = new ArrayList<>();
    private ItemResource preceding;
    private ItemResource succeding;

    @JsonCreator
    public RelatedItemResource() {
        super();
    }

    @JsonCreator
    public RelatedItemResource(String id) {
        this();
        this.id = id;
    }

    public RelatedItemResource(List<ItemResource> hosts,
            List<ItemResource> constituents, ItemResource preceding,
            ItemResource succeding) {
        this();
        this.hosts = hosts;
        this.constituents = constituents;
        this.preceding = preceding;
        this.succeding = succeding;
    }

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
