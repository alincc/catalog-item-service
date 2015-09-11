package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public abstract class TitleInfoBuilder {
    protected Mods mods;
    
    public void setMods(Mods mods) {
        this.mods = mods;
    }

    protected abstract String getTitle();

    public TitleInfo createTitleInfo() {
        String title = getTitle();
        if (title != null) {
            TitleInfo titleInfo = new TitleInfo();
            titleInfo.setTitle(title);
            return titleInfo;
        } else {
            return null;
        }
    }
    
    protected String getTitleByType(String type) {
        if (mods.getTitleInfos() != null) {
            for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo title : mods.getTitleInfos()) {
                if (type.equalsIgnoreCase(title.getType())) {
                    return title.getTitle();
                }
            }
        }
        return null;
    }
    
    
}
