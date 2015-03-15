package com.github.mkolisnyk.aerial.core;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.params.AerialOutputFormat;

public class AerialOutputFormatTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsValue() {
        Assert.assertTrue(AerialOutputFormat.CUCUMBER.isValue("cucumber"));
        Assert.assertFalse(AerialOutputFormat.JBEHAVE.isValue("cucumber"));
    }

    @Test
    public void testGetCurrent() {
        for (AerialOutputFormat value : AerialOutputFormat.values()) {
            value.setCurrent();
            Assert.assertEquals(value, AerialOutputFormat.getCurrent());
        }
    }

    @Test
    public void testGetCurrentShouldSetTheDefaultFormat() {
        System.getProperties().remove("aerial.output.format");
        Assert.assertEquals(AerialOutputFormat.CUCUMBER, AerialOutputFormat.getCurrent());
    }

    @Test
    public void testFromString() {
        Assert.assertEquals(AerialOutputFormat.CUCUMBER, AerialOutputFormat.fromString("cucumber"));
        Assert.assertEquals(AerialOutputFormat.JBEHAVE, AerialOutputFormat.fromString("jbehave"));
        Assert.assertEquals(AerialOutputFormat.JUNIT, AerialOutputFormat.fromString("junit"));
        Assert.assertEquals(AerialOutputFormat.CUSTOM, AerialOutputFormat.fromString("blablabla"));
    }

}
