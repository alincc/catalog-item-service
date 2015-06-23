package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author ronnymikalsen
 * @author rolfmathisen
 *
 */
public class AccessInfoTest {

    @Test
    public void whenPublicThenReturnTrue() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("restricted", "public"));
        
        assertTrue(accessInfo.isPublicDomain());
    }

    @Test
    public void whenNotPublicThenReturnFalse() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("restricted", "bokhylla"));
        
        assertFalse(accessInfo.isPublicDomain());
    }
    
    @Test
    public void whenAccessAllowedFromNORWAY() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("bokhylla", "restricted"));
        
        assertEquals("Access should be \"NORWAY\"", "NORWAY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromLIBRARY() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("avisibibliotek"));
        
        assertEquals("Access should be \"LIBRARY\"", "LIBRARY", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromNB() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("restricted"));
        
        assertEquals("Access should be \"NB\"", "NB", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromUNIVERSALLYRESTRICTED() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList(""));
        
        assertEquals("Access should be \"UNIVERSALLY_RESTRICTED\"", "UNIVERSALLY_RESTRICTED", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREpublic() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("restricted", "public"));
        
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREmavispublic() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("", "mavispublic"));
        
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREstatfjordpublic() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("statfjordpublic", "public"));
        
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREfriggpublic() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("friggpublic"));
        
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
    
    @Test
    public void whenAccessAllowedFromEVERYWHEREshowonly() {
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.getContentClasses().addAll(Arrays.asList("showonly"));
        
        assertEquals("Access should be \"EVERYWHERE\"", "EVERYWHERE", accessInfo.accessAllowedFrom());
    }
}
