package no.nb.microservices.catalogitem.rest.controller.assembler;

public class AlternativeTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    protected String getTitle() {
        if (mods.getTitleInfos() != null) {
            for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo title : mods.getTitleInfos()) {
                if ("alternative".equalsIgnoreCase(title.getType())) {
                    return title.getTitle();
                }
            }
        }
        return null;
    }

}
