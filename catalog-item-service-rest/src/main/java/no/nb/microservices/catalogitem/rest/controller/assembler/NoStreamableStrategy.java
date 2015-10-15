package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class NoStreamableStrategy implements StreamingInfoStrategy {

    @Override
    public StreamingInfo getStreamingInfo(Mods mods) {
        return null;
    }

}
