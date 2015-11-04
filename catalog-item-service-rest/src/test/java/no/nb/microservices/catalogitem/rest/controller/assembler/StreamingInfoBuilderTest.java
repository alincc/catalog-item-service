package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

import org.junit.Test;

import java.util.List;

public class StreamingInfoBuilderTest {

    @Test
    public void testFromLocation() {
        Mods mods = TestMods.aDefaultMusicTrack().build();
        StreamingInfo expected = new StreamingInfo(getFirstUrnLocation(mods), null, null);

        List<StreamingInfo> result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result.get(0));
    }

    @Test
    public void testFromIdentifier() {
        Mods mods = TestMods.aDefaultRadioHourMods().build();
        StreamingInfo expected = new StreamingInfo(getFirstUrnIdentifier(mods), null, null);

        List<StreamingInfo> result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result.get(0));
    }
    
    @Test
    public void testFromExtension() {
        Mods mods = TestMods.aDefaultRadioProgramMods().build();
        StreamingInfo expected = new StreamingInfo(getUrnFromExtension(mods), 100, 200);

        List<StreamingInfo> result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result.get(0));
    }

    private String getFirstUrnLocation(Mods mods) {
        return mods.getLocation().getUrls().get(0).getValue();
    }

    private String getFirstUrnIdentifier(Mods mods) {
        return mods.getIdentifiers().stream()
                .filter(identifier -> "urn".equals(identifier.getType()))
                .findFirst()
                .get()
                .getValue();
    }
    
    private String getUrnFromExtension(Mods mods) {
        return mods.getExtension().getStreamingInfos().get(0).getIdentifier().getValue();
    }
}
