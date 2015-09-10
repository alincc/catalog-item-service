package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public final class MetadataBuilder {

    private final FieldResource field;
    private final Mods mods;
    
    public MetadataBuilder(Item item) {
        super();
        this.field = item.getField();
        this.mods = item.getMods();
    }

    public Metadata build() {
        Metadata metadata = new Metadata();
        metadata.setCompositeTitle(getTitle());
        
        TitleInfoDirector titleInfoDirector = new TitleInfoDirector();
        TitleInfo standardTitleInfo = titleInfoDirector.createTitleInfo(new StandardTitleInfoBuilder(), mods);
        metadata.setTitleInfo(standardTitleInfo);
        TitleInfo alternativeTitleInfo = titleInfoDirector.createTitleInfo(new AlternativeTitleInfoBuilder(), mods);
        metadata.setAlternativeTitleInfo(alternativeTitleInfo);
        
        metadata.setPeople(new PersonsBuilder(mods.getNames()).buildList());
        metadata.setOriginInfo(new OriginInfoBuilder().mods(mods).build());
        metadata.setClassification(new ClassificationBuilder(mods.getClassifications()).build());
        
        return metadata;
    }
    
    private String getTitle() {
        if (field != null) {
            return field.getTitle();
        }
        return null;
    }

}
