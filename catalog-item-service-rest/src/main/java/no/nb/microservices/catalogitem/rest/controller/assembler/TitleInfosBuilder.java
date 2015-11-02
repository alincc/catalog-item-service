package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class TitleInfosBuilder {
    List<no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo> titleInfos;
    
    public TitleInfosBuilder() {
        super();
        this.titleInfos = Collections.emptyList();
    }
    
    public TitleInfosBuilder withTitleInfos(List<no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo> titleInfos) {
        if (titleInfos != null) {
            this.titleInfos = titleInfos;
        } 
        return this;
    }

    public List<TitleInfo> build() {
        List<TitleInfo> titles = new ArrayList<>();
        for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo titleInfo: titleInfos) {
            titles.add(new TitleInfoBuilder()
                .withTitle(titleInfo.getTitle())
                .withType(titleInfo.getType())
                .withPartNumber(titleInfo.getPartNumber())
                .build());
        }
        return titles;
    }

}
