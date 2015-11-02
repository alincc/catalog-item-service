package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class TitleInfoBuilder {
    private String title;
    private String type;
    private String partNumber;
    
    public TitleInfoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TitleInfoBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public TitleInfoBuilder withPartNumber(String partNumber) {
        this.partNumber = partNumber;
        return this;
    }
    
    public TitleInfo build() {
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle(title);
        titleInfo.setType(type);
        titleInfo.setPartNumber(partNumber);
        return titleInfo;
    }
    
}
