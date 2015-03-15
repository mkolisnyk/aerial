package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FieldsTableTest {

    private FieldsTable table;

    @Before
    public void setUp() throws Exception {
        table = new FieldsTable();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAddNullShouldReturnEmptyData() {
        table.add(null);
        Assert.assertEquals(0, table.getData().size());
    }

    @Test
    public void testAddOnEmptyRecordsShouldReturnEmptyData() {
        table.add(new ArrayList<FieldsRecord>());
        Assert.assertEquals(0, table.getData().size());
    }

    @Test
    public void testAddRecordsShouldReturnDataWithZeroValues() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                        put("Column 2", "Value 2");
                    }
                }));
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                        put("Column 2", "Value 2");
                        put("Column 3", "Value 3");
                    }
                }));
            }
        };
        table.add(records);
        for (FieldsRecord record : records) {
            Assert.assertTrue(table.getData().containsKey(record));
            Assert.assertEquals(new Integer(0), table.getData().get(record));
        }
    }

    @Test
    public void testIncrementExistingRecordShouldAddOneToValue() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
            }
        };
        table.add(records);
        FieldsRecord record = records.get(0);
        Assert.assertEquals(new Integer(0), table.getData().get(record));
        table.increment(record);
        Assert.assertEquals(new Integer(1), table.getData().get(record));
    }

    @Test
    public void testIncrementNonExistingRecordShouldAddNewRecord() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
            }
        };
        table.add(records);
        FieldsRecord record = new FieldsRecord(new HashMap<String, String>() {
            {
                put("Column 2", "Value 2");
            }
        });
        table.increment(record);
        Assert.assertEquals(new Integer(0), table.getData().get(records.get(0)));
        Assert.assertEquals(new Integer(0), table.getData().get(record));
    }

    @Test
    public void testIncrementExistingRecordInitializedSeparately() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
            }
        };
        table.add(records);
        FieldsRecord record = new FieldsRecord(new HashMap<String, String>() {
            {
                put("Column 1", "Value 1");
            }
        });
        table.increment(record);
        Assert.assertEquals(new Integer(1), table.getData().get(records.get(0)));
        Assert.assertEquals(new Integer(1), table.getData().get(record));
    }

    @Test
    public void testGetSorted() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 2", "Value 2");
                    }
                }));
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 3", "Value 3");
                    }
                }));
            }
        };
        table.add(records);
        Assert.assertEquals(new Integer(0), table.getData().get(records.get(0)));
        Assert.assertEquals(new Integer(0), table.getData().get(records.get(1)));
        table.increment(records.get(0));
        table.increment(records.get(2));
        table.increment(records.get(2));
        TreeMap<FieldsRecord, Integer> sorted = table.getSorted();
        Iterator<FieldsRecord> iter = sorted.keySet().iterator();
        Assert.assertTrue(iter.next().equals(records.get(1)));
        Assert.assertTrue(iter.next().equals(records.get(0)));
        Assert.assertTrue(iter.next().equals(records.get(2)));
    }

    @Test
    public void testAllNonZerosShouldReturnProperState() {
        List<FieldsRecord> records = new ArrayList<FieldsRecord>() {
            {
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 1", "Value 1");
                    }
                }));
                add(new FieldsRecord(new HashMap<String, String>() {
                    {
                        put("Column 2", "Value 2");
                    }
                }));
            }
        };
        table.add(records);
        table.increment(records.get(0));
        Assert.assertFalse(table.areAllNonZeros());
        table.increment(records.get(1));
        Assert.assertTrue(table.areAllNonZeros());
    }
}
