package com.github.mkolisnyk.aerial.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;

public class AerialParamKeysTest {

    private AerialParamKeys source;

    @Test
    public void testToStringValidKeysShouldReturnProperValues() {
        Map<AerialParamKeys, String> inputs = new HashMap<AerialParamKeys, String>() {
            {
                put(AerialParamKeys.INPUT_TYPE, "-i");
                put(AerialParamKeys.SOURCE, "-s");
                put(AerialParamKeys.OUTPUT_TYPE, "-o");
                put(AerialParamKeys.DESTINATION, "-d");
                put(AerialParamKeys.FORMAT, "-f");
                put(AerialParamKeys.OTHER, "");
            }
        };
        for (Entry<AerialParamKeys, String> item : inputs.entrySet()) {
            source = item.getKey();
            Assert.assertEquals(source.toString(), item.getValue());
        }
    }

    @Test
    public void testIsValueValidKeysShouldReturnTrueForMatchingKey() {
        source = AerialParamKeys.INPUT_TYPE;
        Assert.assertTrue(source.isValue(AerialParamKeys.INPUT_TYPE.toString()));
    }

    @Test
    public void testIsValueWrongKeysShouldReturnFalseForNotMatchingKey() {
        source = AerialParamKeys.INPUT_TYPE;
        Assert.assertFalse(source.isValue(AerialParamKeys.DESTINATION.toString()));
    }

    @Test
    public void testFromStringValidKeysShouldReturnProperValues() {
        Map<AerialParamKeys, String> inputs = new HashMap<AerialParamKeys, String>() {
            {
                put(AerialParamKeys.INPUT_TYPE, "-i");
                put(AerialParamKeys.SOURCE, "-s");
                put(AerialParamKeys.OUTPUT_TYPE, "-o");
                put(AerialParamKeys.DESTINATION, "-d");
                put(AerialParamKeys.FORMAT, "-f");
                put(AerialParamKeys.OTHER, "");
            }
        };
        for (Entry<AerialParamKeys, String> item : inputs.entrySet()) {
            source = AerialParamKeys.fromString(item.getValue());
            Assert.assertEquals(item.getKey(), source);
        }
    }

    @Test
    public void testFromStringInvalidKeyShouldReturnOtherValue() {
        Assert.assertEquals(AerialParamKeys.OTHER, AerialParamKeys.fromString("Wrong String"));
    }
}
