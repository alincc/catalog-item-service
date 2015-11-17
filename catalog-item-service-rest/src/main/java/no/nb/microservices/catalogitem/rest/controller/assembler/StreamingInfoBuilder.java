package no.nb.microservices.catalogitem.rest.controller.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Extent;
import no.nb.microservices.catalogmetadata.model.mods.v3.Identifier;
import no.nb.microservices.catalogmetadata.model.mods.v3.Location;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Offset;

public class StreamingInfoBuilder {

    private Mods mods;
    
    public StreamingInfoBuilder withMods(final Mods mods) {
        this.mods = mods;
        return this;
    }

    public List<StreamingInfo> build() {
        List<StreamingInfo> streamingInfoList = new ArrayList<>();
        
        if (mods.getExtension() != null && mods.getExtension().getStreamingInfos() != null) {
            for (no.nb.microservices.catalogmetadata.model.mods.v3.StreamingInfo modsStreamingInfo : mods.getExtension().getStreamingInfos()) {
                String identifier = modsStreamingInfo.getIdentifier().getValue();
                int offset = getOffsetValue(modsStreamingInfo);
                int extent = getExtentValue(modsStreamingInfo);

                if (identifier == null) {
                    identifier = getIdentifierFromLocation();
                }
                if (identifier == null) {
                    identifier = getIdentifierFromIdentifier();
                }

                StreamingInfo streamingInfo = new StreamingInfo(identifier, offset, extent);
                if (!streamingInfo.isEmpty()) {
                    streamingInfoList.add(streamingInfo);
                }
            }
        }
        else {
            String identifier = getIdentifierFromLocation();
            if (identifier == null) {
                identifier = getIdentifierFromIdentifier();
            }
            StreamingInfo streamingInfo = new StreamingInfo(identifier, null, null);
            if (!streamingInfo.isEmpty()) {
                streamingInfoList.add(streamingInfo);
            }
        }

        return streamingInfoList;
    }

    private Integer getExtentValue(no.nb.microservices.catalogmetadata.model.mods.v3.StreamingInfo modsStreamingInfo) {
        Extent extent = modsStreamingInfo.getExtent();
        if (extent != null) {
            return Integer.valueOf(extent.getValue());
        }
        return null;
    }

    private Integer getOffsetValue(no.nb.microservices.catalogmetadata.model.mods.v3.StreamingInfo modsStreamingInfo) {
        Offset offset = modsStreamingInfo.getOffset();
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
             
             if (urn.isPresent()) {
                 identifier = urn.get().getValue();
             }
        }
        
        return identifier;
        
    }
    

}
