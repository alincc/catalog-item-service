package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class MetadataBuilderTest {

    @Test
    public void testBuild() {
        String id = "id1";
        Mods mods = new Mods();
        createTitleInfo(mods);
        FieldResource fields = new FieldResource();
        Item item = new Item.ItemBuilder(id).mods(mods).fields(fields).hasAccess(true).build();
        
        Metadata metadata = new MetadataBuilder(item).build();
        
        assertNotNull("Should have standard title", metadata.getTitleInfo());
        assertNotNull("Should have alternative title", metadata.getAlternativeTitleInfo());
    }

    private void createTitleInfo(Mods mods) {
        mods.setTitleInfos(Arrays.asList(createStandardTitleInfo(), createAlternativeTitleInfo()));
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo createAlternativeTitleInfo() {
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo alternativeTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        alternativeTitleInfo.setType("alternative");
        alternativeTitleInfo.setTitle("Supersonic");
        return alternativeTitleInfo;
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo createStandardTitleInfo() {
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo standardTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        standardTitleInfo.setTitle("Oasis");
        return standardTitleInfo;
    }

}
