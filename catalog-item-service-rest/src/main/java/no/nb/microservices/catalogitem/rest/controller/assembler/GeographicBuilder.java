package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Coordinates;
import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogsearchindex.Location;

public class GeographicBuilder {

    private OriginInfo originInfo;
    private Location location;

    public GeographicBuilder withOriginInfo(OriginInfo originInfo) {
        this.originInfo = originInfo;
        return this;
    }

    public GeographicBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public Geographic build() {
        Geographic geographic = new Geographic(getPlaceString(),getCoordinates());
        return geographic.isEmpty() ? null : geographic;
    }

    public String getPlaceString() {
        return (originInfo != null && originInfo.getPlace() != null) ? originInfo.getPlace().getPlaceTerm() : null;
    }

    public Coordinates getCoordinates() {
        if (location == null) {
            return null;
        }
        return new Coordinates(location.getLat(),location.getLon());
    }
}
