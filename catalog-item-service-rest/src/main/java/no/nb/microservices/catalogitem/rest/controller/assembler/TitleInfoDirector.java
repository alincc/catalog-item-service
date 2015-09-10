package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class TitleInfoDirector {
    
    public TitleInfo createTitleInfo(TitleInfoBuilder builder, Mods mods) {
        builder.setMods(mods);
        return builder.createTitleInfo();
    }
}
