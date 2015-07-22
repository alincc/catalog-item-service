package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class ClassificationTest {

    @Test
    public void testDdc() {
        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications = new ArrayList<>();
        classifications.add(createDdcClassification("123[S]"));
        classifications.add(createUdcClassification("456[S]"));
        mods.setClassifications(classifications);
        Classification classification = new Classification.Builder(mods).build();
        assertEquals("123[S]", classification.getDdc().get(0));
    }

    @Test
    public void noDdcClassificationsThenReturnEmpty() {
        Mods mods = new Mods();
        Classification classification = new Classification.Builder(mods).build();
        assertEquals(0, classification.getDdc().size());
    }

    @Test
    public void testUdc() {
        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.Classification> classifications = new ArrayList<>();
        classifications.add(createDdcClassification("123[S]"));
        classifications.add(createUdcClassification("456[S]"));
        mods.setClassifications(classifications);
        Classification classification = new Classification.Builder(mods).build();
        assertEquals("456[S]", classification.getUdc().get(0));
    }

    @Test
    public void noUdcClassificationsThenReturnEmpty() {
        Mods mods = new Mods();
        Classification classification = new Classification.Builder(mods).build();
        assertEquals(0, classification.getDdc().size());
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.Classification createDdcClassification(
            String value) {
        no.nb.microservices.catalogmetadata.model.mods.v3.Classification classification = new no.nb.microservices.catalogmetadata.model.mods.v3.Classification();
        classification.setAuthority("ddc");
        classification.setValue(value);
        return classification;
    }

    private no.nb.microservices.catalogmetadata.model.mods.v3.Classification createUdcClassification(
            String value) {
        no.nb.microservices.catalogmetadata.model.mods.v3.Classification classification = new no.nb.microservices.catalogmetadata.model.mods.v3.Classification();
        classification.setAuthority("udc");
        classification.setValue(value);
        return classification;
    }

}
