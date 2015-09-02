package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import no.nb.microservices.catalogmetadata.model.fields.FieldResource;

public class AccessInfoTest {

    @Test
    public void whenPublicThenReturnTrue() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();
        
        assertTrue(accessInfo.isPublicDomain());
    }

    @Test
    public void whenNotPublicThenReturnFalse() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("restricted", "pubokhyllablic"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();
        
        assertFalse(accessInfo.isPublicDomain());
    }
    
    @Test
    public void whenAccessAllowedFromNORWAY() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("bokhylla", "restricted"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();
        
        assertEquals("Access should be \"NORWAY\"", "NORWAY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromLIBRARY() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("avisibibliotek"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();
        
        assertEquals("Access should be \"LIBRARY\"", "LIBRARY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromNB() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("restricted"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"NB\"", "NB", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromUNIVERSALLYRESTRICTED() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList(""));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();
        
        assertEquals("Access should be \"UNIVERSALLY_RESTRICTED\"", "UNIVERSALLY_RESTRICTED", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREpublic() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("restricted", "public"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREmavispublic() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("", "mavispublic"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREstatfjordpublic() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("statfjordpublic", "public"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREfriggpublic() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("friggpublic"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREshowonly() {
        FieldResource fields = new FieldResource();
        fields.setContentClasses(Arrays.asList("showonly"));
        AccessInfo accessInfo = new AccessInfo.Builder().fields(fields).build();

        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
}
