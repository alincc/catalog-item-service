package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogmetadata.model.mods.v3.*;
import org.junit.Test;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.rest.model.Metadata;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public class MetadataBuilderTest {

    @Test
    public void testBuild() {
        String id = "id1";
        Mods mods = new Mods();
        createTitleInfo(mods);
        
        mods.setOriginInfo(createPlace());
        mods.setAbstracts(createtSummary());
        mods.setNotes(createNotes());
        mods.setTypeOfResource("still image");
        mods.setGenre("drama");
        mods.setGenre("drama");

        FieldResource fields = new FieldResource();
        fields.setMediaTypes(createMediaTypes());
        Item item = new Item.ItemBuilder(id).mods(mods).fields(fields).hasAccess(true).build();
        
        Metadata metadata = new MetadataBuilder(item).build();

        assertEquals("Bøker",  metadata.getMediaTypes().get(0));
        assertEquals("Aviser", metadata.getMediaTypes().get(1));
        assertNotNull("Should have standard title", metadata.getTitleInfo());
        assertNotNull("Should have alternative title", metadata.getAlternativeTitleInfo());
        assertNotNull("Should have placeString", metadata.getGeographic().getPlaceString());
        assertEquals("It's a summary", metadata.getSummary());
        assertEquals("still image", metadata.getTypeOfResource());
        assertEquals("drama", metadata.getGenre());
        assertEquals(1, metadata.getNotes().size());
    }

    private List<Note> createNotes() {
        Note note1 = new Note();
        note1.setValue("Tittelinformasjon er hentet fra tilhørende dokumentasjonsmateriale. Widerøe Flyfoto A/S solgte i 1983 disse fotografiene til Kviteseid kommune.");
        return Arrays.asList(note1);
    }

    private List<String> createMediaTypes() {
        return Arrays.asList("Bøker", "Aviser");
    }

    private OriginInfo createPlace() {
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        return originInfo;
    }

    private List<Abstract> createtSummary() {
        List<Abstract> abstracts = new ArrayList<>();
        Abstract summary = new Abstract();
        summary.setValue("It's a summary");
        abstracts.add(summary);
        return abstracts;
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
