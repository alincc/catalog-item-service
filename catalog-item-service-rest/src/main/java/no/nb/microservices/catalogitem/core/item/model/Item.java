package no.nb.microservices.catalogitem.core.item.model;

import org.springframework.hateoas.Identifiable;

import java.util.List;

/**
 * 
 * @author ronnymikalsen
 *
 */
public class Item implements Identifiable<String> {

    private String id;
    private String title;
    private AccessInfo accessInfo = new AccessInfo();
    private List<Person> persons;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
