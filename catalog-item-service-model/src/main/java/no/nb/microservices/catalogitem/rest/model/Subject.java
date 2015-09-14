package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by andreasb on 14.09.15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Subject {
    private List<String> topics;

    public Subject() {
    }

    public Subject(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
