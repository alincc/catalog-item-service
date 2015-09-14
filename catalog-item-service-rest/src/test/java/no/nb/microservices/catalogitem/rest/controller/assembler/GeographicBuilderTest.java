package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by andreasb on 14.09.15.
 */
public class GeographicBuilderTest {

    @Test
    public void whenPlaceStringIsValidTest() {
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        GeographicBuilder builder = new GeographicBuilder(originInfo);

        Geographic build = builder.build();
        assertEquals("Norge;Telemark;Kviteseid;;;;;", build.getPlaceString());
    }

    @Test
    public void whenPlaceStringIsInvalidTest() {
        Place place = new Place();
        place.setPlaceTerm(null);
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        GeographicBuilder builder = new GeographicBuilder(originInfo);

        Geographic build = builder.build();
        assertEquals(null, build.getPlaceString());
    }
}
