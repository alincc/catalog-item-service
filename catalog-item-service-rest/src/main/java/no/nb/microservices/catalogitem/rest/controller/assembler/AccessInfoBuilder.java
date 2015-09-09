package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.AccessInfo;

public final class AccessInfoBuilder {

    private final Item item;
    
    public AccessInfoBuilder(Item item) {
        super();
        this.item = item;
    }

    public AccessInfo build() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setDigital(item.getAccessInfo().isDigital());
        accessInfo.setPublicDomain(item.getAccessInfo().isPublicDomain());
        accessInfo.setAccessAllowedFrom(item.getAccessInfo().accessAllowedFrom());
        accessInfo.setViewability(item.getAccessInfo().getViewability());
        return accessInfo;
    }
    
    
}
