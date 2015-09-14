package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by andreasb on 14.09.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordInfo {
    private String identifier;
    private String identifierSource;
    private String created;

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
}
