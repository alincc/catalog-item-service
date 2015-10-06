package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifiers {

    private List<String> urns = new ArrayList<>();
    private String sesamId;

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
    }

    public String getSesamId() { return sesamId; }

    public void setSesamId(String sesamId) { this.sesamId = sesamId; }

}