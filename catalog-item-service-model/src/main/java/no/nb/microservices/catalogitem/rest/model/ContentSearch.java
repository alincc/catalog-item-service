package no.nb.microservices.catalogitem.rest.model;

public class ContentSearch {
    private String id;
    private String text;

    public ContentSearch() {
    }

    public ContentSearch(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
