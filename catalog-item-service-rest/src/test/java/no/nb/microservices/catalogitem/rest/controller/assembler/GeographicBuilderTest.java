package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import no.nb.microservices.catalogsearchindex.Location;
import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;

public class GeographicBuilderTest {

    @Test
    public void whenPlaceStringIsValidTest() {
        Place place = new Place();
        place.setPlaceTerm("Norge;Telemark;Kviteseid;;;;;");
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        GeographicBuilder builder = new GeographicBuilder().withOriginInfo(originInfo);

        Geographic build = builder.build();
        assertEquals("Norge;Telemark;Kviteseid;;;;;", build.getPlaceString());
    }

    @Test
    public void whenPlaceStringIsInvalidTest() {
        Place place = new Place();
        place.setPlaceTerm(null);
        OriginInfo originInfo = new OriginInfo();
        originInfo.setPlace(place);
        GeographicBuilder builder = new GeographicBuilder().withOriginInfo(originInfo);

        Geographic build = builder.build();
        assertEquals(null, build);
    }

    @Test
    public void whenLocationIsNotNullGeographicShouldContainCoordinates() {
        Location location = new Location();
        location.setLat(123);
        location.setLon(321);

        Geographic geographic = new GeographicBuilder().withLocation(location).build();
        assertNotNull(geographic.getCoordinates());
    }
}
