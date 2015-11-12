package no.nb.microservices.catalogitem.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identifiers {

    private List<String> isbn10 = new ArrayList<>();
    private List<String> isbn13 = new ArrayList<>();
    private List<String> issn = new ArrayList<>();
    private String sesamId;
    private String oaiId;
    private String urn;

    public Identifiers() {

    }

    public Identifiers(List<String> isbn10, List<String> isbn13, List<String> issn, String sesamId, String oaiId, String urn) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.issn = issn;
        this.sesamId = sesamId;
        this.oaiId = oaiId;
        this.urn = urn;
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

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return isbn10.isEmpty()
                && isbn13.isEmpty()
                && issn.isEmpty()
                && sesamId == null
                && oaiId == null
                && urn == null;
    }

}