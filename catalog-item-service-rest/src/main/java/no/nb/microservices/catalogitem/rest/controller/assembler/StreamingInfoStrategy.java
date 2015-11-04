package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

import java.util.List;

public interface StreamingInfoStrategy {

    List<StreamingInfo> getStreamingInfo(Mods mods);
    
    boolean hasStreamingLink();
    
}
