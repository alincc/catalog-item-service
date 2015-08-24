package no.nb.microservices.catalogitem.core.item.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservices.catalogitem.core.item.model.Item;
import no.nb.microservices.catalogitem.core.metadata.repository.MetadataRepository;
import no.nb.microservices.catalogitem.core.security.repository.SecurityRepository;
import no.nb.microservices.catalogmetadata.model.fields.Fields;
import no.nb.microservices.catalogmetadata.model.mods.v3.Mods;
import no.nb.microservices.catalogmetadata.model.mods.v3.Name;
import no.nb.microservices.catalogmetadata.model.mods.v3.Namepart;
import no.nb.microservices.catalogmetadata.model.mods.v3.Role;
import no.nb.microservices.catalogmetadata.model.mods.v3.TitleInfo;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    MetadataRepository metadataRepository;
    
    @Mock
    SecurityRepository securityRepository;

    @Before
    public void setup() {
        mockRequest();
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
        
        when(metadataRepository.getModsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(mods);
        when(metadataRepository.getFieldsById(eq(id), anyString(), anyString(), anyString(), anyString())).thenReturn(fields);
        when(securityRepository.hasAccess(eq(id), anyString(), anyString())).thenReturn(true);
        
        Item item = itemService.getItemById(id);
        
        assertNotNull("Item should not be null", item);
        assertEquals("Title should be \"Supersonic\"", title, item.getTitleInfo().getTitle());
        assertTrue("isDigital should be true", item.getAccessInfo().isDigital());
        assertTrue("isPublicDomain should be true", item.getAccessInfo().isPublicDomain());
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", item.getAccessInfo().accessAllowedFrom());
        assertNotNull("Should have list of people", item.getPersons());
        assertEquals("Viewability should be ALL", "ALL", item.getAccessInfo().getViewability());
    }
    
    private void mockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/v1/search?q=Junit");
        String ip = "123.45.123.123";
        request.addHeader(UserUtils.REAL_IP_HEADER, ip);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        RequestContextHolder.setRequestAttributes(attributes);
    }


}
