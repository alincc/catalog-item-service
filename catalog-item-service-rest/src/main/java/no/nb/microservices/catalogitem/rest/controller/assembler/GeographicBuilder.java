package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.Geographic;
import no.nb.microservices.catalogmetadata.model.mods.v3.OriginInfo;

public class GeographicBuilder {

    private OriginInfo originInfo;

    public GeographicBuilder(final OriginInfo originInfo) {
        this.originInfo = originInfo;
    }

    public Geographic build() {
        if (getPlaceString() != null) {
            Geographic geographic = new Geographic();
            geographic.setPlaceString(getPlaceString());
            return geographic;
        }
        return null;
    }

    public String getPlaceString() {
        return (originInfo != null && originInfo.getPlace() != null) ? originInfo.getPlace().getPlaceTerm() : null;
    }
}
