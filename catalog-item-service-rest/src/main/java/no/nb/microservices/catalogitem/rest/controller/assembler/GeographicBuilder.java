package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Place;

/**
 * Created by andreasb on 11.09.15.
 */
public class GeographicBuilder {

    private OriginInfo originInfo;

    public GeographicBuilder(final OriginInfo originInfo) {
        this.originInfo = originInfo;
    }

    public Geographic build() {
        Geographic geographic = new Geographic();
        geographic.setPlaceString(getPlaceString());
        return geographic;
    }

    public String getPlaceString() {
        return (originInfo != null && originInfo.getPlace() != null) ? originInfo.getPlace().getPlaceTerm() : null;
    }
}
