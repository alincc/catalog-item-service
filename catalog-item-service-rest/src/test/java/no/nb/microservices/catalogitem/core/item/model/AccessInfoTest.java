package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.fields.Fields;

public class AccessInfoTest {

    @Test
    public void whenPublicThenReturnTrue() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();
        
        assertTrue(accessInfo.isPublicDomain());
    }

    @Test
    public void whenNotPublicThenReturnFalse() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("restricted", "pubokhyllablic"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();
        
        assertFalse(accessInfo.isPublicDomain());
    }
    
    @Test
    public void whenAccessAllowedFromNORWAY() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("bokhylla", "restricted"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();
        
        assertEquals("Access should be \"NORWAY\"", "NORWAY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromLIBRARY() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("avisibibliotek"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();
        
        assertEquals("Access should be \"LIBRARY\"", "LIBRARY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromNB() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("restricted"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"NB\"", "NB", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromUNIVERSALLYRESTRICTED() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList(""));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();
        
        assertEquals("Access should be \"UNIVERSALLY_RESTRICTED\"", "UNIVERSALLY_RESTRICTED", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREpublic() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREmavispublic() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("", "mavispublic"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREstatfjordpublic() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("statfjordpublic", "public"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREfriggpublic() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("friggpublic"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREshowonly() {
        Fields fields = new Fields();
        fields.setContentClasses(Arrays.asList("showonly"));
        AccessInfo accessInfo = new AccessInfo.AccessInfoBuilder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
}
