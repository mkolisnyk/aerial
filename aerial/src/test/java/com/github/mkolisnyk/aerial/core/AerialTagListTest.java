package com.github.mkolisnyk.aerial.core;

import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

public class AerialTagListTest {

    private AerialTagList tags;

    @Before
    public void setUp() throws Exception {
        tags = new AerialTagList();
    }

    @Test
    public void testHasNext() {
        Assert.assertFalse(tags.hasNext());
        tags.add("test");
        Assert.assertTrue(tags.hasNext());
        tags.next();
        Assert.assertFalse(tags.hasNext());
    }

    @Test
    public void testNext() {
        String value = "test";
        Assert.assertNull(tags.next());
        tags.add(value);
        String result = tags.next();
        Assert.assertNotNull(result);
        Assert.assertEquals(value, result);
        Assert.assertNull(tags.next());
    }

}
