package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class TitleInfo {

    private String title;

    private TitleInfo(Builder builder) {
        this.title = builder.getTitle();
    }

    public String getTitle() {
        return title;
    }

    public static class Builder {
        
        private Mods mods;
        
        public Builder(final Mods mods) {
            this.mods = mods != null ? mods : new Mods();
        }
        
        public TitleInfo build() {
            return new TitleInfo(this);
        }
        
        private String getTitle() {
            if (mods.getTitleInfos() != null) {
                return mods.getTitleInfos().iterator().next().getTitle();
            }
            return null;
        }
    }
}
