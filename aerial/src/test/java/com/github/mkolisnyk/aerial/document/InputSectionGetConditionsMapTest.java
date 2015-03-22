package com.github.mkolisnyk.aerial.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputSectionGetConditionsMapTest {
    private InputSection section;

    @Before
    public void setUp() throws Exception {
        section = new InputSection(null);
    }

    @Test
    public void testGetConditionsMapForValidConditionSet() throws Exception {
        String ls = System.lineSeparator();
        String inputText = "| Name | Type | Value | Condition |" + ls
                + "| nameValue | int | [0;100) | a > 0 |" + ls
                + "| nameValue | int | [0;100) | a <= 0 |" + ls
                + "| a | int | [-100;100) | |";
        section.parse(inputText);
        Map<String, List<String>> expected = new HashMap<String, List<String>>() {
            {
                put("nameValue", new ArrayList() {
                    {
                        add("a > 0");
                        add("a <= 0");
                    }
                });
            }
        };
        Map<String, List<String>> actual = InputSection.getNameConditionMap(section.getInputs());
        Assert.assertEquals(expected.entrySet().size(), actual.entrySet().size());
        for (Entry<String, List<String>> entry : expected.entrySet()) {
            Assert.assertTrue(actual.containsKey(entry.getKey()));
            for (String value : entry.getValue()) {
                Assert.assertTrue(actual.get(entry.getKey()).contains(value));
            }
        }
        for (Entry<String, List<String>> entry : actual.entrySet()) {
            Assert.assertTrue(expected.containsKey(entry.getKey()));
            for (String value : entry.getValue()) {
                Assert.assertTrue(expected.get(entry.getKey()).contains(value));
            }
        }
    }
}
