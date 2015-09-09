package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;

public class TitleInfoTest {

    @Test
    public void whenModsHasAlternativeTitleThenReturnIt() {
        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo> titleInfos = new ArrayList<>();
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo titleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        titleInfo.setTitle("Supersonic");
        titleInfo.setType("alternative");
        titleInfos.add(titleInfo);
        mods.setTitleInfos(titleInfos);

        TitleInfo alternative = new TitleInfo.AlternativeTitleBuilder(mods).build();
        
        assertEquals("Supersonic", alternative.getTitle());
    }
    
    @Test
    public void whenModsNotHaveAlternativeTitleThenReturnNull() {
        Mods mods = new Mods();
        List<no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo> titleInfos = new ArrayList<>();
        no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo titleInfo = new no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo();
        titleInfo.setTitle("Supersonic");
        titleInfos.add(titleInfo);
        mods.setTitleInfos(titleInfos);

        TitleInfo alternative = new TitleInfo.AlternativeTitleBuilder(mods).build();
        
        assertNull(alternative);
    }
    
}
