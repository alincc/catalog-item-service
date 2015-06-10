package no.nb.microservices.catalogitem.core.item.service;

import org.springframework.hateoas.Identifiable;

public class Item implements Identifiable<String> {

    private String id;
    private String title;

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

}
