package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class TitleInfoDirector {
    
    public TitleInfo createTitleInfo(TitleInfoBuilder builder, Item item) {
        builder.setItem(item);
        return builder.createTitleInfo();
    }
}
