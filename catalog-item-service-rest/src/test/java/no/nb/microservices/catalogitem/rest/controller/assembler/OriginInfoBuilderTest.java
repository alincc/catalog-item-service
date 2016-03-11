package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.DateMods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;
import no.nb.microservices.catalogmetadata.test.mods.v3.DateModsBuilder;
import no.nb.microservices.catalogmetadata.test.mods.v3.ModsBuilder;
import no.nb.microservices.catalogsearchindex.ItemResource;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OriginInfoBuilderTest {

    @Test
    public void whenNoOriginInfoItShouldReturnNull() {
        OriginInfo originInfo = new OriginInfoBuilder().withOriginInfo(null).withExpand().build();
        assertNull("originInfo should be null", originInfo);
    }

    @Test
    public void whenOriginInfoIsValidTest() {
        no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo originInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo();
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

        ItemResource itemResource = new ItemResource();
        itemResource.setFirstIndexTime("2015-05-05");

        OriginInfo build = new OriginInfoBuilder().withOriginInfo(mods.getOriginInfo()).withItemResource(getItemResource()).withExpand().build();

        assertEquals("1969-04-01", build.getCaptured());
        assertEquals("1969-04-01", build.getCreated());
        assertEquals("1969-04-01", build.getModified());
        assertEquals("Frequency", build.getFrequency());
        assertEquals("Edition", build.getEdition());
        assertEquals("1969-04-01", build.getIssued());
        assertEquals("Publisher", build.getPublisher());
        assertEquals("2015-05-05", build.getFirstIndexTime());
    }
    
    @Test
    public void testW3cdtfFormat() {
        DateMods noEncoding = new DateModsBuilder()
                .withValue("2008")
                .build();
        DateMods w3cdtf = new DateModsBuilder()
                .withEncoding("w3cdtf")
                .withValue("2009")
                .build();
        DateMods marc = new DateModsBuilder()
                .withEncoding("marc")
                .withValue("2010")
                .build();
        Mods mods = new ModsBuilder()
                .withOriginInfo(new no.nb.microservices.catalogmetadata.test.mods.v3.OriginInfoBuilder()
                        .withDateIssued(noEncoding, w3cdtf, marc)
                        .build())
                .build();


        OriginInfo originInfo = new OriginInfoBuilder()
                .withOriginInfo(mods.getOriginInfo())
                .withExpand()
                .build();        
        
        assertEquals("2009", originInfo.getIssued());
    }
    
    @Test
    public void testMarchFormat() {
        DateMods noEncoding = new DateModsBuilder()
                .withValue("2008")
                .build();
        DateMods marc = new DateModsBuilder()
                .withEncoding("marc")
                .withValue("2009")
                .build();
        DateMods w3cdtf = new DateModsBuilder()
                .withValue("2010")
                .build();
        Mods mods = new ModsBuilder()
                .withOriginInfo(new no.nb.microservices.catalogmetadata.test.mods.v3.OriginInfoBuilder()
                        .withDateIssued(noEncoding, w3cdtf, marc)
                        .build())
                .build();
        
        OriginInfo originInfo = new OriginInfoBuilder()
                .withOriginInfo(mods.getOriginInfo())
                .withExpand()
                .build();        
        
        assertEquals("2009", originInfo.getIssued());
    }

    @Test
    public void testIllegalEncodingFormat() {
        DateMods noEncoding = new DateModsBuilder()
                .withValue("2008")
                .build();
        DateMods marc = new DateModsBuilder()
                .withEncoding("marc")
                .withValue("uuuu")
                .build();

        Mods mods = new ModsBuilder()
                .withOriginInfo(new no.nb.microservices.catalogmetadata.test.mods.v3.OriginInfoBuilder()
                        .withDateIssued(noEncoding, marc)
                        .build())
                .build();
        
        OriginInfo originInfo = new OriginInfoBuilder()
                .withOriginInfo(mods.getOriginInfo())
                .withExpand()
                .build();        
        
        assertEquals("2008", originInfo.getIssued());
    }

    @Test
    public void testNoEncodingFormat() {
        DateMods noEncoding = new DateModsBuilder()
                .withValue("2009")
                .build();
        Mods mods = new ModsBuilder()
                .withOriginInfo(new no.nb.microservices.catalogmetadata.test.mods.v3.OriginInfoBuilder()
                        .withDateIssued(noEncoding)
                        .build())
                .build();
        
        OriginInfo originInfo = new OriginInfoBuilder()
                .withOriginInfo(mods.getOriginInfo())
                .withExpand()
                .build();        
        
        assertEquals("2009", originInfo.getIssued());
    }

    private ItemResource getItemResource() {
        ItemResource itemResource = new ItemResource();
        itemResource.setFirstIndexTime("2015-05-05");
        itemResource.setDateIssued("1969-04-01");
        return itemResource;
    }
}
