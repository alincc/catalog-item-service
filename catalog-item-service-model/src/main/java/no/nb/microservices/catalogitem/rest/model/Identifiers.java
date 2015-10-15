package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifiers {

    private List<String> urns = new ArrayList<>();
    private List<String> isbn10 = new ArrayList<>();
    private List<String> isbn13 = new ArrayList<>();
    private List<String> issn = new ArrayList<>();
    private String sesamId;
    private String oaiId;

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        this.urns = urns;
    }

    public String getSesamId() {
        return sesamId; 
    }

    public void setSesamId(String sesamId) {
        this.sesamId = sesamId;
    }

    public List<String> getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(List<String> isbn10) {
        this.isbn10 = isbn10;
    }

    public List<String> getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(List<String> isbn13) {
        this.isbn13 = isbn13;
    }

    public List<String> getIssn() {
        return issn;
    }

    public void setIssn(List<String> issn) {
        this.issn = issn;
    }

    public String getOaiId() {
        return oaiId;
    }

    public void setOaiId(String oaiid) {
        this.oaiId = oaiid;
    }
}