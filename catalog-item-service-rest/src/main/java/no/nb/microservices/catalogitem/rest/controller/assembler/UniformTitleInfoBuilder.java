package no.nb.microservices.catalogitem.rest.controller.assembler;

public class UniformTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    protected String getTitle() {
        return getTitleByType("uniform");
    }

}
