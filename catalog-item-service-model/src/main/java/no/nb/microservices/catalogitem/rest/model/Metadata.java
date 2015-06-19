package no.nb.microservices.catalogitem.rest.model;

import java.util.List;

public class Metadata {

    private TitleInfo titleInfo;
    private List<Person> people;

    public TitleInfo getTitleInfo() {
        return titleInfo;
    }

    public void setTitleInfo(TitleInfo titleInfo) {
        this.titleInfo = titleInfo;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }
}
