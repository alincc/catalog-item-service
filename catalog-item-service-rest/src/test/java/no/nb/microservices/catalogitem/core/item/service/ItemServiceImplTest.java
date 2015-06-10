package no.nb.microservices.catalogitem.core.item.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogmetadata.model.fields.Field;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author ronnymikalsen
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    private IItemService itemService;

    @Mock
    MetadataRepository metadataRepository;

    @Before
    public void setup() {
        itemService = new ItemServiceImpl(metadataRepository);
    }

    @Test
    public void whenGetItemByIdSuccessThenReturnItem() {
        String id = "id1";
        String title = "Supersonic";
        
        Mods mods = new Mods();
        List<TitleInfo> titleInfos = new ArrayList<>();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle(title);
        titleInfos.add(titleInfo);
        mods.setTitleInfos(titleInfos);
        
        List<Field> fields = Arrays.asList(new Field("digital", "Ja"));
        
        when(metadataRepository.getModsById(id)).thenReturn(mods);
        when(metadataRepository.getFieldsById(id)).thenReturn(fields);
        
        Item item = itemService.getItemById(id);
        
        assertNotNull("Item should not be null", item);
        assertEquals("Title should be \"Supersonic\"", title, item.getTitle());
        assertTrue("isDigital should be true", item.getAccessInfo().isDigital());
        
    }
    

}
