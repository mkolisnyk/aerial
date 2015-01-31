package com.github.mkolisnyk.aerial.core;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.params.AerialInputFormat;

public class AerialInputFormatTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetCurrent() {
        for (AerialInputFormat value : AerialInputFormat.values()) {
            value.setCurrent();
            Assert.assertEquals(value, AerialInputFormat.getCurrent());
        }
    }

    @Test
    public void testGetCurrentShouldSetTheDefaultFormat() {
        System.getProperties().remove("aerial.input.format");
        Assert.assertEquals(AerialInputFormat.PLAIN, AerialInputFormat.getCurrent());
    }

    @Test
    public void testFromString() {
        Assert.assertEquals(AerialInputFormat.PLAIN, AerialInputFormat.fromString("plain"));
        Assert.assertEquals(AerialInputFormat.CUSTOM, AerialInputFormat.fromString("blablabla"));
    }

}
