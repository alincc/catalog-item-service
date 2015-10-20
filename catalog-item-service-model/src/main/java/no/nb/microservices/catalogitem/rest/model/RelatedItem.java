package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedItem {
    private List<Metadata> hosts;
    private List<Metadata> constituents;
    private Metadata preceding;
    private Metadata succeding;

    public List<Metadata> getHosts() {
        return hosts;
    }

    public void setHosts(List<Metadata> hosts) {
        this.hosts = hosts;
    }

    public List<Metadata> getConstituents() {
        return constituents;
    }

    public void setConstituents(List<Metadata> constituents) {
        this.constituents = constituents;
    }

    public Metadata getPreceding() {
        return preceding;
    }

    public void setPreceding(Metadata preceding) {
        this.preceding = preceding;
    }

    public Metadata getSucceding() {
        return succeding;
    }

    public void setSucceding(Metadata succeding) {
        this.succeding = succeding;
    }
}
