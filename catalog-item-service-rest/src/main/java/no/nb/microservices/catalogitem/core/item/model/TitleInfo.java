package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class TitleInfo {

    private String title;

    private TitleInfo(TitleBuilder builder) {
        this.title = builder.getTitle();
    }

    public String getTitle() {
        return title;
    }

    public static class TitleBuilder {
        
        private Mods mods;
        
        public TitleBuilder(final Mods mods) {
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
