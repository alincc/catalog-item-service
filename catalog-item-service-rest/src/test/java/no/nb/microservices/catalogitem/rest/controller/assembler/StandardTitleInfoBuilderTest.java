package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class StandardTitleInfoBuilderTest {

    @Test
    public void whenTilteInfoIsStandardThenReturnIt() {
        StandardTitleInfoBuilder builder = new StandardTitleInfoBuilder();
        Mods mods = new Mods();
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo standardTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        standardTitleInfo.setTitle("Oasis");
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo alternativeTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        alternativeTitleInfo.setType("alternative");
        alternativeTitleInfo.setTitle("Supersonic");
        mods.setTitleInfos(Arrays.asList(standardTitleInfo, alternativeTitleInfo));
        builder.setMods(mods);

        TitleInfo result = builder.createTitleInfo();
        
        assertEquals("Oasis", result.getTitle());
    }
}
