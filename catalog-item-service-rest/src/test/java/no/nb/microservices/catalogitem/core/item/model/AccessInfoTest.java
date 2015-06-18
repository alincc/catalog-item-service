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
}
