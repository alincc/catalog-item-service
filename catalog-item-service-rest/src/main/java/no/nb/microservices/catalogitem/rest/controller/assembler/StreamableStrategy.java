package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class StreamableStrategy implements StreamingInfoStrategy {

    @Override
    public StreamingInfo getStreamingInfo(Mods mods) {
        return new StreamingInfoBuilder()
                .withMods(mods)
                .build();
    }

    @Override
    public boolean hasStreamingLink() {
        return true;
    }

}
