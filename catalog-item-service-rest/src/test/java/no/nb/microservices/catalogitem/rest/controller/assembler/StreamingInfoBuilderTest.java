package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import no.nb.microservices.catalogitem.rest.model.StreamingInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.test.mods.v3.TestMods;

import org.junit.Test;

public class StreamingInfoBuilderTest {

    @Test
    public void testFromLocation() {
        Mods mods = TestMods.aDefaultMusicMods().build();
        StreamingInfo expected = new StreamingInfo(getFirstUrnLocation(mods), null, null);
        
        StreamingInfo result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result);
    }

    @Test
    public void testFromIdentifier() {
        Mods mods = TestMods.aDefaultRadioHourMods().build();
        StreamingInfo expected = new StreamingInfo(getFirstUrnIdentifier(mods), null, null);
        
        StreamingInfo result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFromExtension() {
        Mods mods = TestMods.aDefaultRadioProgramMods().build();
        StreamingInfo expected = new StreamingInfo(getUrnFromExtension(mods), 100, 200);
        
        StreamingInfo result = new StreamingInfoBuilder()
            .withMods(mods)
            .build();
        
        assertEquals(expected, result);
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
        return mods.getExtension().getStreamingInfo().getIdentifier().getValue();
    }
}
