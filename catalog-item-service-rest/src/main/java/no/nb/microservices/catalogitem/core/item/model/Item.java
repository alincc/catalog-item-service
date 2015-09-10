package no.nb.microservices.catalogitem.core.item.model;

import org.springframework.hateoas.Identifiable;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class Item implements Identifiable<String> {
    
    private String id;
    private Mods mods; 
    private FieldResource field;
    private boolean hasAccess;

    private Item(String id, Mods mods, FieldResource field, boolean hasAccess) {
        this.id = id;
        this.mods = mods;
        this.field = field;
        this.hasAccess = hasAccess;
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

    public FieldResource getField() {
        if (field == null) {
            field = new FieldResource();
        }
        return field;
    }

    public boolean hasAccess() {
        return hasAccess;
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
            return new Item(id, mods, fields, hasAccess);
        }

    }

}
