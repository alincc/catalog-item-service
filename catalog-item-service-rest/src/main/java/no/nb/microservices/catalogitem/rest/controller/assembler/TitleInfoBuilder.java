package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public abstract class TitleInfoBuilder {
    protected Item item;
    
    public void setItem(Item item) {
        this.item = item;
    }

    public abstract TitleInfo createTitleInfo();

    
}
