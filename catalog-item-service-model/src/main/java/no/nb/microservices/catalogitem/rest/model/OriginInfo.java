package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginInfo {
    private String publisher;
    private String issued;
    private String frequency;
    private String created;
    private String captured;
    private String modified;
    private String edition;
    private String firstIndexTime;

    public OriginInfo() {

    }

    public OriginInfo(String publisher, String issued, String frequency, String created, String captured, String modified, String edition, String firstIndexTime) {
        this.publisher = publisher;
        this.issued = issued;
        this.frequency = frequency;
        this.created = created;
        this.captured = captured;
        this.modified = modified;
        this.edition = edition;
        this.firstIndexTime = firstIndexTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCaptured() {
        return captured;
    }

    public void setCaptured(String captured) {
        this.captured = captured;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getFirstIndexTime() {
        return firstIndexTime;
    }

    public void setFirstIndexTime(String firstIndexTime) {
        this.firstIndexTime = firstIndexTime;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return publisher == null 
                && issued == null 
                && frequency == null
                && created == null 
                && captured == null 
                && modified == null
                && edition == null
                && firstIndexTime == null;

    }
}