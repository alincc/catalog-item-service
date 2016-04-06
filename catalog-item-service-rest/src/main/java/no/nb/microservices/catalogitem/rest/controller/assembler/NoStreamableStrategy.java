package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

import java.util.Collections;
import java.util.List;

public class NoStreamableStrategy implements StreamingInfoStrategy {

    @Override
    public List<StreamingInfo> getStreamingInfo(Mods mods) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasStreamingLink() {
        return false;
    }

}
