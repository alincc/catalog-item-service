package no.nb.microservices.catalogitem.rest.controller.assembler;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.*;

import java.util.Optional;

public class StreamingInfoBuilder {

    private Mods mods;
    
    public StreamingInfoBuilder withMods(final Mods mods) {
        this.mods = mods;
        return this;
    }

    public StreamingInfo build() {
        
        String identifier = null;
        Integer offset = null;
        Integer extent = null;
        
        if (mods.getExtension() != null && mods.getExtension().getStreamingInfo() != null) {
            identifier = mods.getExtension().getStreamingInfo().getIdentifier().getValue();
            offset = getOffsetValue();
            extent = getExtentValue();
        }
        
        if (identifier == null) {
            identifier = getIdentifierFromLocation();
        }
        if (identifier == null) {
            identifier = getIdentifierFromIdentifier();
        }
        
        return new StreamingInfo(identifier, offset, extent);
    }

    private Integer getExtentValue() {
        Extent extent = mods.getExtension().getStreamingInfo().getExtent();
        if (extent != null) {
            return Integer.valueOf(extent.getValue());
        }
        return null;
    }

    private Integer getOffsetValue() {
        Offset offset = mods.getExtension().getStreamingInfo().getOffset();
        if (offset != null) {
            return Integer.valueOf(offset.getValue());
        }
        return null;
    }

    private String getIdentifierFromLocation() {
        Location location = mods.getLocation();
        if (location != null && location.getUrls() != null) {
            return location.getUrls().get(0).getValue();
        } else {
            return null;
        }
    }
    
    private String getIdentifierFromIdentifier() {
        String identifier = null;
        if (mods.getIdentifiers() != null) {
             Optional<Identifier> urn = mods.getIdentifiers().stream()
                .filter(i -> "urn".equals(i.getType()))
                .findFirst();
             
             if (urn != null) {
                 identifier = urn.get().getValue();
             }
        }
        
        return identifier;
        
    }
    

}
