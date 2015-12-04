package no.nb.microservices.catalogitem.core.utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import org.junit.Test;

public class ItemFieldsTest {

    @Test
    public void testEmpty() {
        assertThat(ItemFields.show(Arrays.asList(), "metadata"), is(true));
    }

    @Test
    public void testAny() {
        assertThat(ItemFields.show(Arrays.asList("*"), "metadata"), is(true));
    }

    @Test
    public void testMatch() {
        assertThat(ItemFields.show(Arrays.asList("accessInfo", "metadata"), "metadata"), is(true));
    }

    @Test
    public void testNoMatch() {
        assertThat(ItemFields.show(Arrays.asList("accessInfo", "metadata"), "title"), is(false));
    }

}
