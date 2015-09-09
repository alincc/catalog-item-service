package no.nb.microservices.catalogitem.core.item.model;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class TitleInfo {

    private String title;

    private TitleInfo(String title) {
        this.title =title;
    }

    public String getTitle() {
        return title;
    }

    public abstract static class TitleBuilder {
        
        protected Mods mods;
        
        public TitleBuilder(final Mods mods) {
            this.mods = mods != null ? mods : new Mods();
        }
        
        public TitleInfo build() {
            String title = getTitle();
            if (title != null) {
                return new TitleInfo(title);
            } else {
                return null;
            }
        }
        
        abstract String getTitle();
        
    }

    public static class StandardTitleBuilder extends TitleBuilder {
        
        public StandardTitleBuilder(final Mods mods) {
            super(mods);
        }

        @Override
        protected String getTitle() {
            if (mods.getTitleInfos() != null) {
                for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo title : mods.getTitleInfos()) {
                    if (title.getType() == null) {
                        return title.getTitle();
                    }
                }
            }
            return null;
        }
    }
    
    public static class AlternativeTitleBuilder extends TitleBuilder {
        
        public AlternativeTitleBuilder(final Mods mods) {
            super(mods);
        }
        
        @Override
        protected String getTitle() {
            if (mods.getTitleInfos() != null) {
                for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo title : mods.getTitleInfos()) {
                    if ("alternative".equalsIgnoreCase(title.getType())) {
                        return title.getTitle();
                    }
                }
            }
            return null;
        }
    }

}
