package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Identifiers;
import no.nb.microservices.catalogmetadata.model.fields.FieldResource;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IdentifiersBuilderTest {

    @Test
    public void whenUrnsIsPresentTest() {
        FieldResource field = new FieldResource();
        field.setUrns(Arrays.asList("URN:NBN:no-nb_digibok_2014070158006"));
        IdentifiersBuilder builder = new IdentifiersBuilder(field);

        Identifiers identifiers = builder.build();

        assertTrue("urns should not be empty", !identifiers.getUrns().isEmpty());
        assertEquals("URN:NBN:no-nb_digibok_2014070158006", identifiers.getUrns().get(0));
    }

    @Test
    public void whenUrnsIsEmptyTest() {
        FieldResource field = new FieldResource();
        Identifiers identifiers = new IdentifiersBuilder(field).build();

        assertTrue("urn list should be empty not null", identifiers.getUrns().isEmpty());
    }
}
