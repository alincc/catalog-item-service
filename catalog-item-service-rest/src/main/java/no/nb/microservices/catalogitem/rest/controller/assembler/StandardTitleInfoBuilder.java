package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class StandardTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    public TitleInfo createTitleInfo() {
        if (item.getTitleInfo() != null) {
            TitleInfo titleInfo = new TitleInfo();
            titleInfo.setTitle(item.getTitleInfo().getTitle());
            return titleInfo;
        } else {
            return null;
        }
    }

}
