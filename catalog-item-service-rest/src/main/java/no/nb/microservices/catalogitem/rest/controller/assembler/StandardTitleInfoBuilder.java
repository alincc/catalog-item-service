package no.nb.microservices.catalogitem.rest.controller.assembler;

public class StandardTitleInfoBuilder extends TitleInfoBuilder {

    @Override
    protected String getTitle() {
        if (mods.getTitleInfos() != null) {
            for (no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo title : mods.getTitleInfos()) {
                if (title.getType() == null) {
                    return title.getTitle();
                }
            }
        }
        return null;
    }

}
