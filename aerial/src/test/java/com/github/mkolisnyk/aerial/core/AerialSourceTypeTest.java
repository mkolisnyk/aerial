package com.github.mkolisnyk.aerial.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

public class AerialSourceTypeTest {

    AerialSourceType source;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testToStringValidKeysShouldReturnProperValues() {
        Map<AerialSourceType, String> inputs = new HashMap<AerialSourceType, String>() {
            {
                put(AerialSourceType.NONE, "none");
                put(AerialSourceType.STRING, "text");
                put(AerialSourceType.FILE, "file");
                put(AerialSourceType.JIRA, "jira");
                put(AerialSourceType.CUSTOM, "custom");
            }
        };
        for (Entry<AerialSourceType, String> item : inputs.entrySet()) {
            source = item.getKey();
            Assert.assertEquals(source.toString(), item.getValue());
        }
    }

    @Test
    public void testIsValueValidKeysShouldReturnTrueForMatchingKey() {
        source = AerialSourceType.STRING;
        Assert.assertTrue(source.isValue(AerialSourceType.STRING.toString()));
    }

    @Test
    public void testIsValueWrongKeysShouldReturnFalseForNotMatchingKey() {
        source = AerialSourceType.STRING;
        Assert.assertFalse(source.isValue(AerialSourceType.FILE.toString()));
    }

    @Test
    public void testFromStringValidKeysShouldReturnProperValues() {
        Map<AerialSourceType, String> inputs = new HashMap<AerialSourceType, String>() {
            {
                put(AerialSourceType.NONE, "none");
                put(AerialSourceType.STRING, "text");
                put(AerialSourceType.FILE, "file");
                put(AerialSourceType.JIRA, "jira");
                put(AerialSourceType.CUSTOM, "custom");
            }
        };
        for (Entry<AerialSourceType, String> item : inputs.entrySet()) {
            source = AerialSourceType.fromString(item.getValue());
            Assert.assertEquals(item.getKey(), source);
        }
    }

    @Test
    public void testFromStringInvalidKeyShouldReturnNoneValue() {
        Assert.assertEquals(AerialSourceType.NONE, AerialSourceType.fromString("Wrong String"));
    }
}
