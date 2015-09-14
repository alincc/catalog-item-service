package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class OriginInfoBuilderTest {

    @Test
    public void whenOriginInfoIsValidTest() {
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPublisher("Publisher");
        originInfo.setIssuance("Issuance");
        originInfo.setFrequency("Frequency");
        originInfo.setEdition("Edition");
        DateMods dateMods = new DateMods();
        dateMods.setEncoding("w3cdtf");
        dateMods.setKeyDate("yes");
        dateMods.setValue("1969-04-01");
        DateMods issued = new DateMods();
        issued.setKeyDate("yes");
        issued.setValue("1969-04-01");
        originInfo.setDateModified(dateMods);
        originInfo.setDateCreated(Arrays.asList(dateMods));
        originInfo.setDateCaptured(dateMods);
        originInfo.setDateIssuedList(Arrays.asList(issued));
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        originInfo.setPlace(place);
        Mods mods = new Mods();
        mods.setOriginInfo(originInfo);

        OriginInfoBuilder builder = new OriginInfoBuilder().mods(mods);
        no.nb.microservices.catalogitem.rest.model.OriginInfo build = builder.build();

        assertEquals("1969-04-01", build.getCaptured());
        assertEquals("1969-04-01", build.getCreated());
        assertEquals("1969-04-01", build.getModified());
        assertEquals("Frequency", build.getFrequency());
        assertEquals("Edition", build.getEdition());
        assertEquals("1969-04-01", build.getIssued());
        assertEquals("Publisher", build.getPublisher());
    }
}
