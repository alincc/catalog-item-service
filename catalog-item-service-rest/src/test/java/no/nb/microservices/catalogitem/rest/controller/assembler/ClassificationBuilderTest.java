package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogmetadata.model.mods.v3.Classification;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassificationBuilderTest {

    @Test
    public void whenNoClassificationsItShouldReturnNull() {
        assertNull("classifications should be null", new ClassificationBuilder().withClassifications(null).build());
    }

    @Test
    public void testWithDdc() {
        Classification classification = new Classification();
        classification.setAuthority("ddc");
        classification.setValue("randomddc");
        assertEquals("Classifications should have 1 ddc", 1, new ClassificationBuilder().withClassifications(Arrays.asList(classification)).build().getDdc().size());
    }

    @Test
    public void testWithUdc() {
        Classification classification = new Classification();
        classification.setAuthority("udc");
        classification.setValue("randomudc");
        assertEquals("Classifications should have 1 udc", 1, new ClassificationBuilder().withClassifications(Arrays.asList(classification)).build().getUdc().size());
    }
}
