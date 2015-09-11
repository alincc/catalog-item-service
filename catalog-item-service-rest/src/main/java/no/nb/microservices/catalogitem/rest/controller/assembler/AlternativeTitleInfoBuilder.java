package no.nb.microservices.catalogitem.rest.controller.assembler;

public class AlternativeTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    protected String getTitle() {
        return getTitleByType("alternative");
    }

}
