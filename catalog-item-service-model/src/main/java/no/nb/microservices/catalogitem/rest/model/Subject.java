package no.nb.microservices.catalogitem.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subject {
    private List<String> topics;
    private List<Person> persons;

    public Subject() {

    }

    public Subject(List<String> topics, List<Person> persons) {
        this.topics = topics;
        this.persons = persons;
    }

    public List<String> getTopics() {
        if (topics == null) {
            return Collections.emptyList();
        } else {
            return topics;
        }
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<Person> getPersons() {
        if (persons == null) {
            return Collections.emptyList();
        } else {
            return persons;
        }
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
