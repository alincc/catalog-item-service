package no.nb.microservices.catalogitem.model;

import no.nb.microservices.catalogitem.core.item.model.Origin;
import no.nb.microservices.catalogitem.core.item.model.Summary;
import no.nb.microservices.catalogmetadata.model.mods.v3.Abstract;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by andreasb on 10.09.15.
 */
public class SummaryTest {
    @Test
    public void whenAbstractsHasValueItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setAbstracts(Arrays.asList(new Abstract() {{
            setValue("This is a summary of the book");
        }}));

        Summary summary = new Summary.Builder(mods).build();
        assertEquals("This is a summary of the book",summary.getValue());
    }

    @Test
    public void whenAbstractsHasMultipleValuesItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setAbstracts(Arrays.asList(new Abstract() {{ setValue("This is a summary of the book"); }}, new Abstract() {{ setValue("This is a second summary of the book"); }}));

        Summary summary = new Summary.Builder(mods).build();
        assertEquals("This is a summary of the book",summary.getValue());
    }

    @Test
    public void whenAbstractsHasNoValuesItShouldNotBeExtracted() {
        Mods mods = new Mods();
        mods.setAbstracts(new ArrayList<>());

        Summary summary = new Summary.Builder(mods).build();
        assertEquals("",summary.getValue());
    }

    @Test
    public void whenAbstractsIsNullItShouldNotBeExtracted() {
        Mods mods = new Mods();
        mods.setAbstracts(null);

        Summary summary = new Summary.Builder(mods).build();
        assertEquals("",summary.getValue());
    }
}
