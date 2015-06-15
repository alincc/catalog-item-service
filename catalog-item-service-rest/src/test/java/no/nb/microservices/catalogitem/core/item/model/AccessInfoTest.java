package no.nb.microservices.catalogitem.core.item.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

/**
 * 
 * @author ronnymikalsen
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

}
