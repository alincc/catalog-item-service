package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class IdentifiersBuilderTest {

    @Test
    public void whenUrnsIsPresentTest() {
        FieldResource field = new FieldResource();
        field.setUrns(Arrays.asList("URN:NBN:no-nb_digibok_2014070158006"));
        
        Identifiers identifiers = new IdentifiersBuilder()
                .withField(field)
                .build();

        assertTrue("urns should not be empty", !identifiers.getUrns().isEmpty());
        assertEquals("URN:NBN:no-nb_digibok_2014070158006", identifiers.getUrns().get(0));
    }

    @Test
    public void whenUrnsIsEmptyTest() {
        FieldResource field = new FieldResource();
        
        Identifiers identifiers = new IdentifiersBuilder()
                .withField(field)
                .build();

        assertTrue("urn list should be empty not null", identifiers.getUrns().isEmpty());
    }

    @Test
    public void getSesamIdTest() {
        Mods mods = createDefaultMods();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("sesamId should not be empty", !identifiers.getSesamId().isEmpty());
        assertEquals("7e8cb1ea2f5d198e6f1b51e3e835c203", identifiers.getSesamId());
    }

    @Test
    public void getOaiIdTest() {
        Mods mods = createDefaultMods();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("oaiId should not be empty", !identifiers.getOaiId().isEmpty());
        assertEquals("oai:mavis.nb.no:1512271", identifiers.getOaiId());
    }


    private Mods createDefaultMods() {
        Mods mods = new Mods();
        List<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setType("sesamid");
        identifier.setValue("7e8cb1ea2f5d198e6f1b51e3e835c203");
        identifiers.add(identifier);
        Identifier identifier2 = new Identifier();
        identifier2.setType("oaiid");
        identifier2.setValue("oai:mavis.nb.no:1512271");
        identifiers.add(identifier2);
        mods.setIdentifiers(identifiers);
        return mods;
    }

}
