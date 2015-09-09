package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class AlternativeTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    public TitleInfo createTitleInfo() {
        if (item.getTitleAlternativeInfo() != null) {
            TitleInfo titleInfo = new TitleInfo();
            titleInfo.setTitle(item.getTitleAlternativeInfo().getTitle());
            return titleInfo;
        } else {
            return null;
        }
    }

}
