package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class IdentifiersBuilderTest {

    @Test
    public void whenNoIdentifiersItShouldReturnNull() {
        Identifiers identifiers = new IdentifiersBuilder().withIdentifiers(null).build();
        assertNull("Identifiers should be null", identifiers);
    }

    @Test
    public void urnIsEmptyTest() {
        Identifier sesamid = new Identifier();
        sesamid.setType("sesamid");
        sesamid.setValue("sesamid1234");

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(Arrays.asList(sesamid))
                .withExpand()
                .build();

        assertNull("urn should be empty not null", identifiers.getUrn());
    }

    @Test
    public void getUrnTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("URN should not be empty", !identifiers.getUrn().isEmpty());
        assertEquals("URN:NBN:no-nb_digibok_2014070158006", identifiers.getUrn());
    }

    @Test
    public void getSesamIdTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("sesamId should not be empty", !identifiers.getSesamId().isEmpty());
        assertEquals("f17f2bf12cc19b377f7ce992faa40a0c", identifiers.getSesamId());
    }

    @Test
    public void getOaiIdTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("oaiId should not be empty", !identifiers.getOaiId().isEmpty());
        assertEquals("oai:bibsys.no:biblio:96192845x", identifiers.getOaiId());
    }


    @Test
    public void getIsbn13Test() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("isbn13 should not be empty", !identifiers.getIsbn13().isEmpty());
        assertEquals("9788203341091", identifiers.getIsbn13().get(1));
    }

    @Test
    public void getIsbn10Test() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("isbn10 should not be empty", !identifiers.getIsbn10().isEmpty());
        assertTrue(identifiers.getIsbn10().contains("9788203340"));
        assertTrue(identifiers.getIsbn10().contains("9788203341"));
    }

    @Test
    public void getIssnTest() {
        Mods mods = TestMods.aDefaultBookMods().build();

        Identifiers identifiers = new IdentifiersBuilder()
                .withIdentifiers(mods.getIdentifiers())
                .withExpand()
                .build();

        assertTrue("isbn10 should not be empty", !identifiers.getIssn().isEmpty());
        assertEquals("97882033", identifiers.getIssn().get(0));
    }

}
