package no.nb.microservices.catalogitem.core.utils;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import org.junit.Test;

public class ItemUtilsTest {

    @Test
    public void testEmptyField() {
        assertThat(ItemUtils.showField(Arrays.asList(), "metadata"), is(true));
    }

    @Test
    public void testAnyField() {
        assertThat(ItemUtils.showField(Arrays.asList("*"), "metadata"), is(true));
    }

    @Test
    public void testMatchField() {
        assertThat(ItemUtils.showField(Arrays.asList("accessInfo", "metadata"), "metadata"), is(true));
    }

    @Test
    public void testNoMatchField() {
        assertThat(ItemUtils.showField(Arrays.asList("accessInfo", "metadata"), "title"), is(false));
    }

    @Test
    public void testNoExpand() {
        assertThat(ItemUtils.isExpand(null,"metadata"),is(false));
    }

    @Test
    public void testExpand() {
        assertThat(ItemUtils.isExpand("metadata","metadata"), is(true));
    }

}
