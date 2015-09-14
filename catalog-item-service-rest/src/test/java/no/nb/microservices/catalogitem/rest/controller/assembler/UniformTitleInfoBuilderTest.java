package no.nb.microservices.catalogitem.rest.controller.assembler;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogitem.rest.model.TitleInfo;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class UniformTitleInfoBuilderTest {

    @Test
    public void test() {
        UniformTitleInfoBuilder builder = new UniformTitleInfoBuilder();
        Mods mods = new Mods();
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo standardTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        standardTitleInfo.setTitle("Oasis");
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo uniformTitleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        uniformTitleInfo.setType("uniform");
        uniformTitleInfo.setTitle("Supersonic");
        mods.setTitleInfos(Arrays.asList(standardTitleInfo, uniformTitleInfo));
        builder.setMods(mods);

        TitleInfo result = builder.createTitleInfo();
        
        assertEquals("Supersonic", result.getTitle());
        
    }

}
