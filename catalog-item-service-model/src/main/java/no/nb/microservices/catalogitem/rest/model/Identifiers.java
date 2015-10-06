package no.nb.microservices.catalogitem.rest.model;

import java.util.ArrayList;
import java.util.List;

public class Identifiers {

    private List<String> urns = new ArrayList<>();
    private String sesamId;
    private String oaiId;

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
    }

    public String getSesamId() { return sesamId; }

    public void setSesamId(String sesamId) { this.sesamId = sesamId; }

    public String getOaiId() { return oaiId; }

    public void setOaiId(String oaiId) { this.oaiId = oaiId; }

}