package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordInfo {
    private String identifier;
    private String identifierSource;
    private String created;

    public RecordInfo() {
        super();
    }

    public RecordInfo(String identifier, String identifierSource, String created) {
        this();
        this.identifier = identifier;
        this.identifierSource = identifierSource;
        this.created = created;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierSource() {
        return identifierSource;
    }

    public void setIdentifierSource(String identifierSource) {
        this.identifierSource = identifierSource;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return identifier == null
                && identifierSource == null
                && created == null;
    }
}
