package no.nb.microservices.catalogitem.core.item.model;

import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.Identifiable;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class Item implements Identifiable<String> {

    private String id;
    private String compositeTitle;
    private TitleInfo titleInfo;
    private String summary;
    private AccessInfo accessInfo;
    private List<Person> persons;
    private Origin origin;
    private Classification classification;

    private Item(String id, String compositeTitle, TitleInfo titleInfo, String summary, List<Person> persons, AccessInfo accessInfo, Origin origin, Classification classification) {
        this.id = id;
        this.compositeTitle = compositeTitle;
        this.titleInfo = titleInfo;
        this.summary = summary;
        this.persons = persons;
        this.accessInfo = accessInfo;
        this.origin = origin;
        this.classification = classification;
    }

    @Override
    public String getId() {
        return id;
    }
    public String getTitle() {
        return compositeTitle;
    }
    
    public TitleInfo getTitleInfo() {
        return titleInfo;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(persons);
    }

    public Origin getOrigin() {
        return origin;
    }
    
    public Classification getClassification() {
        return classification;
    }

    public static class ItemBuilder  {
        private final String id;
        private Mods mods; 
        private FieldResource fields;
        private boolean hasAccess;
        
        public ItemBuilder(final String id) {
            this.id = id;
        }
        
        public ItemBuilder mods(final Mods mods) {
            this.mods = mods;
            return this;
        }
        
        public ItemBuilder fields(final FieldResource fields) {
            this.fields = fields;
            return this;
        }
        
        public ItemBuilder hasAccess(final boolean hasAccess) {
            this.hasAccess = hasAccess;
            return this;
        }

        public Item build() {
            String compositeTitle = null;
            if (fields != null) {
                compositeTitle = fields.getTitle();
            }
            return new Item(id,
                    compositeTitle,
                    new TitleInfo.Builder(mods).build(),
                    new Summary.Builder(mods).build().getValue(),
                    new Persons.Builder(mods).buildList(),
                    new AccessInfo.Builder().fields(fields).hasAccess(hasAccess).build(),
                    new Origin.Builder().mods(mods).build(),
                    new Classification.Builder(mods).build());
        }

    }

}
