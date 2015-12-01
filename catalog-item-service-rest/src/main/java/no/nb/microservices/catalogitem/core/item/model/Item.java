package no.nb.microservices.catalogitem.core.item.model;

import org.springframework.hateoas.Identifiable;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogsearchindex.SearchResource;

public class Item implements Identifiable<String> {
    
    private String id;
    private Mods mods; 
    private boolean hasAccess;
    private RelatedItems relatedItems;
    private SearchResource searchResource;

    private Item(String id, Mods mods, boolean hasAccess, RelatedItems relatedItems, SearchResource searchResource) {
        this.id = id;
        this.mods = mods;
        this.hasAccess = hasAccess;
        this.relatedItems = relatedItems;
        this.searchResource = searchResource;
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

    public SearchResource getSearchResource() {
        return searchResource;
    }

    public static class ItemBuilder  {
        private final String id;
        private Mods mods; 
        private boolean hasAccess;
        private RelatedItems relatedItems;
        private SearchResource searchResource;
        
        public ItemBuilder(final String id) {
            this.id = id;
        }
        
        public ItemBuilder mods(final Mods mods) {
            this.mods = mods;
            return this;
        }

        public ItemBuilder withSearchResource(final SearchResource searchResource) {
            this.searchResource = searchResource;
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
            return new Item(id, mods, hasAccess, relatedItems, searchResource);
        }

    }

}
