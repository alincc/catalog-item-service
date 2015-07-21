package no.nb.microservices.catalogitem.utils;

import no.nb.microservices.catalogitem.core.item.model.Origin;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ModsOriginInfoExtractorTest {

    @Test
    public void whenOriginInfoHasPublisherItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        mods.getOriginInfo().setPublisher("Oslo nye teater");

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("Oslo nye teater",origin.getPublisher());
    }

    @Test
    public void whenOriginInfoHasFrequencyItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        mods.getOriginInfo().setFrequency("Annual");

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("Annual",origin.getFrequency());
    }

    @Test
    public void whenOriginInfoHasEditionItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        mods.getOriginInfo().setEdition("7th ed.");

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("7th ed.", origin.getEdition());
    }

    @Test
    public void whenOriginInfoHasDateCapturedItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        DateMods dateCaptured = new DateMods();
        dateCaptured.setEncoding("iso8601");
        dateCaptured.setValue("20010712");
        mods.getOriginInfo().setDateCaptured(dateCaptured);

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("20010712", origin.getDateCaptured());
    }

    @Test
    public void whenOriginInfoHasDateModifiedItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        DateMods dateModified = new DateMods();
        dateModified.setEncoding("iso8601");
        dateModified.setValue("20031008");
        mods.getOriginInfo().setDateModified(dateModified);

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("20031008", origin.getDateModified());
    }

    @Test
    public void whenOriginInfoHasDateIssuedItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        DateMods dateIssuedString = new DateMods();
        dateIssuedString.setValue("1998-");

        DateMods dateIssuedStart = new DateMods();
        dateIssuedStart.setEncoding("marc");
        dateIssuedStart.setPoint("start");
        dateIssuedStart.setValue("1998");

        DateMods dateIssuedEnd = new DateMods();
        dateIssuedEnd.setEncoding("marc");
        dateIssuedEnd.setPoint("end");
        dateIssuedEnd.setValue("9999");

        mods.getOriginInfo().setDateIssuedList(Arrays.asList(dateIssuedString, dateIssuedStart, dateIssuedEnd));

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("1998-", origin.getDateIssued());
    }

    @Test
    public void whenOriginInfoHasDateCreatedItShouldBeExtracted() {
        Mods mods = new Mods();
        mods.setOriginInfo(new OriginInfo());
        DateMods dateCreated = new DateMods();
        dateCreated.setValue("1972-10-08");
        dateCreated.setKeyDate("yes");
        mods.getOriginInfo().setDateCreated(Arrays.asList(dateCreated));

        Origin origin = ModsOriginInfoExtractor.extractOriginInfo(mods);
        assertEquals("1972-10-08", origin.getDateCreated());
    }
}
