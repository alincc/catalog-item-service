package no.nb.microservices.catalogitem.core.item.model;

import java.util.List;

import org.springframework.hateoas.Identifiable;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogsearchindex.ItemResource;

public class Item implements Identifiable<String> {
    
    private String id;
    private List<String> fields;
    private Mods mods; 
    private boolean hasAccess;
    private RelatedItems relatedItems;
    private ItemResource itemResource;

    private Item(String id, List<String> fields, Mods mods, boolean hasAccess, RelatedItems relatedItems, ItemResource itemResource) {
        this.id = id;
        this.setFields(fields);
        this.mods = mods;
        this.hasAccess = hasAccess;
        this.relatedItems = relatedItems;
        this.itemResource = itemResource;
    }

    @Override
    public String getId() {
        return id;
    }

    public Mods getMods() {
        if (mods == null) {
            mods = new Mods();
        }
        return mods;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public RelatedItems getRelatedItems() {
        return relatedItems;
    }

    public ItemResource getItemResource() {
        return itemResource;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public static class ItemBuilder  {
        private final String id;
        private List<String> fields;
        private Mods mods; 
        private boolean hasAccess;
        private RelatedItems relatedItems;
        private ItemResource itemResource;
        
        public ItemBuilder(final String id) {
            this.id = id;
        }

        public ItemBuilder withFields(final List<String> fields) {
            this.fields = fields;
            return this;
        }

        public ItemBuilder mods(final Mods mods) {
            this.mods = mods;
            return this;
        }

        public ItemBuilder withItemResource(final ItemResource itemResource) {
            this.itemResource = itemResource;
            return this;
        }
        
        public ItemBuilder hasAccess(final boolean hasAccess) {
            this.hasAccess = hasAccess;
            return this;
        }

        public ItemBuilder withRelatedItems(final RelatedItems relatedItems) {
            this.relatedItems = relatedItems;
            return this;
        }

        public Item build() {
            return new Item(id, fields, mods, hasAccess, relatedItems, itemResource);
        }

    }

}
