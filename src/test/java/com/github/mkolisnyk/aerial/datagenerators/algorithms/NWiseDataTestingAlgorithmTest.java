package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NWiseDataTestingAlgorithmTest {

    private NWiseDataTestingAlgorithm algorithm;

    private Map<String, List<String>> testData = new HashMap<String, List<String>>() {
        {
            put("Column 1", new ArrayList<String>() {
                {
                    add("Value 1-1");
                    add("Value 1-1");
                    add("Value 1-1");
                    add("Value 1-1");
                    add("Value 1-1");
                    add("Value 1-1");
                    add("Value 1-2");
                    add("Value 1-2");
                    add("Value 1-2");
                    add("Value 1-2");
                    add("Value 1-2");
                    add("Value 1-2");
                }
            });
            put("Column 2", new ArrayList<String>() {
                {
                    add("Value 2-1");
                    add("Value 2-1");
                    add("Value 2-1");
                    add("Value 2-2");
                    add("Value 2-2");
                    add("Value 2-2");
                    add("Value 2-1");
                    add("Value 2-1");
                    add("Value 2-1");
                    add("Value 2-2");
                    add("Value 2-2");
                    add("Value 2-2");
                }
            });
            put("Column 3", new ArrayList<String>() {
                {
                    add("Value 3-1");
                    add("Value 3-2");
                    add("Value 3-3");
                    add("Value 3-1");
                    add("Value 3-2");
                    add("Value 3-3");
                    add("Value 3-1");
                    add("Value 3-2");
                    add("Value 3-3");
                    add("Value 3-1");
                    add("Value 3-2");
                    add("Value 3-3");
                }
            });
        }
    };

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUniqueCombinations() {

        List<FieldsRecord> expected = new ArrayList<FieldsRecord>() {
            {
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-1");
                                    put("Column 3", "Value 3-1");
                                }
                            }
                        )
                );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-1");
                                    put("Column 3", "Value 3-2");
                                }
                            }
                        )
                );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-1");
                                    put("Column 3", "Value 3-3");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-2");
                                    put("Column 3", "Value 3-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-2");
                                    put("Column 3", "Value 3-2");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-2");
                                    put("Column 3", "Value 3-3");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-1");
                                    put("Column 1", "Value 1-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-1");
                                    put("Column 1", "Value 1-2");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-2");
                                    put("Column 1", "Value 1-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 2", "Value 2-2");
                                    put("Column 1", "Value 1-2");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-1");
                                    put("Column 1", "Value 1-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-1");
                                    put("Column 1", "Value 1-2");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-2");
                                    put("Column 1", "Value 1-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-2");
                                    put("Column 1", "Value 1-2");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-3");
                                    put("Column 1", "Value 1-1");
                                }
                            }
                        )
                 );
                add(
                        new FieldsRecord(
                            new HashMap<String, String>() {
                                {
                                    put("Column 3", "Value 3-3");
                                    put("Column 1", "Value 1-2");
                                }
                            }
                        )
                 );
            }
        };
        algorithm = new NWiseDataTestingAlgorithm(testData, 2);
        List<FieldsRecord> actual = algorithm.getUniqueCombinations();
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGenerateTestData() {
        algorithm = new NWiseDataTestingAlgorithm(testData, 2);
        Map<String, List<String>> result = algorithm.generateTestData();
        int count = result.get(result.keySet().iterator().next()).size();
        for (Entry<String, List<String>> entry : result.entrySet()) {
            Assert.assertTrue(entry.getValue().size() > 0);
            Assert.assertEquals(count, entry.getValue().size());
        }
        Assert.assertTrue(count < testData.get(testData.keySet().iterator().next()).size());
    }
    @Test
    public void testGenerateTestFullRecordsData() {
        algorithm = new NWiseDataTestingAlgorithm(testData, testData.keySet().size());
        Map<String, List<String>> result = algorithm.generateTestData();
        int count = result.get(result.keySet().iterator().next()).size();
        for (Entry<String, List<String>> entry : result.entrySet()) {
            Assert.assertTrue(entry.getValue().size() > 0);
            Assert.assertEquals(count, entry.getValue().size());
        }
        Assert.assertEquals(count, testData.get(testData.keySet().iterator().next()).size());
    }
}
