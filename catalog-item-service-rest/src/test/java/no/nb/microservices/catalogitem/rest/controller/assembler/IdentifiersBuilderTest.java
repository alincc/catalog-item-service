package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

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
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("sesamId should not be empty", !identifiers.getSesamId().isEmpty());
        assertEquals("f17f2bf12cc19b377f7ce992faa40a0c", identifiers.getSesamId());
    }

    @Test
    public void getOaiIdTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("oaiId should not be empty", !identifiers.getOaiId().isEmpty());
        assertEquals("oai:bibsys.no:biblio:96192845x", identifiers.getOaiId());
    }


    @Test
    public void getIsbn13Test() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("isbn13 should not be empty", !identifiers.getIsbn13().isEmpty());
        assertEquals("9788203341091", identifiers.getIsbn13().get(1));
    }

    @Test
    public void getIsbn10Test() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("isbn10 should not be empty", !identifiers.getIsbn10().isEmpty());
        assertTrue(identifiers.getIsbn10().contains("9788203340"));
        assertTrue(identifiers.getIsbn10().contains("9788203341"));
    }

    @Test
    public void getIssnTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withMods(mods)
                .build();

        assertTrue("isbn10 should not be empty", !identifiers.getIssn().isEmpty());
        assertEquals("97882033", identifiers.getIssn().get(0));
    }

}
