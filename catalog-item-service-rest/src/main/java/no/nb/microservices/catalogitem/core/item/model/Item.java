package no.nb.microservices.catalogitem.core.item.model;

import java.util.Collections;
import java.util.List;

import org.springframework.hateoas.Identifiable;

import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class Item implements Identifiable<String> {

    private String id;
    private TitleInfo titleInfo;
    private AccessInfo accessInfo;
    private List<Person> persons;
    private Origin origin;

    private Item(String id, TitleInfo titleInfo, List<Person> persons, AccessInfo accessInfo, Origin origin) {
        this.id = id;
        this.titleInfo = titleInfo;
        this.persons = persons;
        this.accessInfo = accessInfo;
        this.origin = origin;
    }

    @Override
    public String getId() {
        return id;
    }

    public TitleInfo getTitleInfo() {
        return titleInfo;
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
    
    public static class ItemBuilder  {
        private final String id;
        private Mods mods; 
        private Fields fields;
        private boolean hasAccess;
        
        public ItemBuilder(final String id) {
            this.id = id;
        }
        
        public ItemBuilder mods(final Mods mods) {
            this.mods = mods;
            return this;
        }
        
        public ItemBuilder fields(final Fields fields) {
            this.fields = fields;
            return this;
        }
        
        public ItemBuilder hasAccess(final boolean hasAccess) {
            this.hasAccess = hasAccess;
            return this;
        }

        public Item build() {
            return new Item(id, 
                    new TitleInfo.TitleBuilder(mods).build(), 
                    new Persons.PersonsBuilder(mods).buildList(),
                    new AccessInfo.AccessInfoBuilder().fields(fields).hasAccess(hasAccess).build(),
                    new Origin.OriginBuilder().mods(mods).build());
        }

    }

}
