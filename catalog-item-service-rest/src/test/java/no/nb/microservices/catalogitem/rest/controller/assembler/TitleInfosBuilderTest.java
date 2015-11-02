package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import no.nb.microservices.catalogitem.rest.model.TitleInfo;

public class TitleInfosBuilderTest {

    @Test
    public void test() {
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo titleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        titleInfo.setTitle("Supersonic");
        titleInfo.setPartNumber("1");
        titleInfo.setType("track");

        List<TitleInfo> titleInfos = new TitleInfosBuilder().withTitleInfos(Arrays.asList(titleInfo)).build();
        
        TitleInfo expected = new TitleInfo();
        expected.setTitle("Supersonic");
        expected.setType("track");
        expected.setPartNumber("1");
        assertEquals(Arrays.asList(expected), titleInfos);
    }

}
