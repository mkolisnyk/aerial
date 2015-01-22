package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class NWiseDataTestingAlgorithmTest {

    private NWiseDataTestingAlgorithm algorithm;
    private String[] columnNames;
    private int recordSize;
    private List<Map<String, String>> expected;

    private Map<String, List<String>> testData = new HashMap<String, List<String>>() {
        {
            put("Column 1", new ArrayList<String>() {
                {
                    add("Value 1-1");
                }
            });
            put("Column 2", new ArrayList<String>() {
                {
                    add("Value 2-1");
                    add("Value 2-2");
                }
            });
            put("Column 3", new ArrayList<String>() {
                {
                    add("Value 3-1");
                    add("Value 3-2");
                    add("Value 3-3");
                }
            });
        }
    };

    public NWiseDataTestingAlgorithmTest(String description,
            String[] columnNamesValue,
            int recordSizeValue,
            List<Map<String, String>> expectedValue) {
        this.columnNames = columnNamesValue;
        this.recordSize = recordSizeValue;
        this.expected = expectedValue;
    }

    @Parameters(name = "Test String generation: {0}.")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                    "Column with 1 value vs Column with 2 values. 2 fields",
                    new String[] {"Column 1", "Column 2"},
                    2,
                    new ArrayList<Map<String, String>>() {
                        {
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-2");
                                        }
                                    }
                            );
                        }
                    }
                },
                {
                    "Column with 1 value vs Column with 3 values. 2 fields",
                    new String[] {"Column 1", "Column 3"},
                    2,
                    new ArrayList<Map<String, String>>() {
                        {
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 3", "Value 3-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 3", "Value 3-2");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 3", "Value 3-3");
                                        }
                                    }
                            );
                        }
                    }
                },
                {
                    "Column with 2 values vs Column with 3 values. 2 fields",
                    new String[] {"Column 2", "Column 3"},
                    2,
                    new ArrayList<Map<String, String>>() {
                        {
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-2");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-3");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-2");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-3");
                                        }
                                    }
                            );
                        }
                    }
                },
                {
                    "3 Columns. 2 fields",
                    new String[] {"Column 1", "Column 2", "Column 3"},
                    3,
                    new ArrayList<Map<String, String>>() {
                        {
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-2");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-1");
                                            put("Column 3", "Value 3-3");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-1");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-2");
                                        }
                                    }
                            );
                            add(
                                    new HashMap<String, String>() {
                                        {
                                            put("Column 1", "Value 1-1");
                                            put("Column 2", "Value 2-2");
                                            put("Column 3", "Value 3-3");
                                        }
                                    }
                            );

                        }
                    }
                }

        });
    }

    @Before
    public void setUp() throws Exception {
        algorithm = new NWiseDataTestingAlgorithm(testData, this.recordSize);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUniqueCombinations() {
        List<Map<String, String>> actual = algorithm.getUniqueCombinations(this.columnNames);
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i).size(), actual.get(i).size());
            for (String key : expected.get(i).keySet()) {
                Assert.assertTrue(actual.get(i).containsKey(key));
                Assert.assertEquals(expected.get(i).get(key), actual.get(i).get(key));
            }
        }
    }

    @Test
    public void testDistinct() {
        Map<String, List<String>> actual = algorithm.distinctTestData();
        Assert.assertTrue(actual.size() > 0);
        for (Entry<String, List<String>> item : actual.entrySet()) {
            Assert.assertTrue(item.getValue().size() > 0);
            Assert.assertTrue(item.getValue().size() <= this.testData.get(item.getKey()).size());
            List<String> distinctList = new ArrayList<String>();
            List<String> actualList = item.getValue();
            for (String value : actualList) {
                Assert.assertFalse(distinctList.contains(value));
                distinctList.add(value);
            }
        }
    }
}
