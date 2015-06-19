package no.nb.microservices.catalogitem.core.item.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author ronnymikalsen
 * @author rolfmathisen
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    private IItemService itemService;

    @Mock
    MetadataRepository metadataRepository;
    
    @Mock
    SecurityRepository securityRepository;

    @Before
    public void setup() {
        itemService = new ItemServiceImpl(metadataRepository, securityRepository);
    }

    @Test
    public void whenGetItemByIdThenReturnItem() {
        String id = "id1";
        String title = "Supersonic";
        
        Mods mods = new Mods();
        List<TitleInfo> titleInfos = new ArrayList<>();
        TitleInfo titleInfo = new TitleInfo();
        titleInfo.setTitle(title);
        titleInfos.add(titleInfo);
        mods.setTitleInfos(titleInfos);

        Name name = new Name();
        name.setType("personal");
        Namepart namepart = new Namepart();
        namepart.setValue("Kurt Josef");
        Role role = new Role();
        role.setRoleTerms(Arrays.asList("creator"));
        name.setRole(Arrays.asList(role));
        name.setNameParts(Arrays.asList(namepart));
        mods.setNames(Arrays.asList(name));

        Fields fields = new Fields();
        fields.setDigital(true);
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        
        when(metadataRepository.getModsById(id)).thenReturn(mods);
        when(metadataRepository.getFieldsById(id)).thenReturn(fields);
        when(securityRepository.hasAccess(id)).thenReturn(true);
        
        Item item = itemService.getItemById(id);
        
        assertNotNull("Item should not be null", item);
        assertEquals("Title should be \"Supersonic\"", title, item.getTitle());
        assertTrue("isDigital should be true", item.getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", item.getAccessInfo().isPublicDomain());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", item.getAccessInfo().accessAllowedFrom());
        assertNotNull("Should have list of people", item.getPersons());
        assertEquals("Viewability should be ALL", "ALL", item.getAccessInfo().getViewability());
    }
    

}
