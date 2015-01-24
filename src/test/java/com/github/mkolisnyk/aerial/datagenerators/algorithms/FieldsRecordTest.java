package com.github.mkolisnyk.aerial.datagenerators.algorithms;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FieldsRecordTest {

    private FieldsRecord record;
    private FieldsRecord biggerRecord;
    private FieldsRecord differentRecord;
    private FieldsRecord copyRecord;

    @Before
    public void setUp() throws Exception {
        Map<String, String> input = new HashMap<String, String>() {
            {
                put("Column 1", "Value 1");
                put("Column 2", "Value 2");
                put("Column 3", "Value 3");
            }
        };
        record = new FieldsRecord(input);
        copyRecord = new FieldsRecord(input);
        Map<String, String> biggerInput = new HashMap<String, String>() {
            {
                put("Column 0", "Value 0");
                put("Column 1", "Value 1");
                put("Column 2", "Value 2");
                put("Column 3", "Value 3");
                put("Column 4", "Value 4");
                put("Column 5", "Value 5");
            }
        };
        Map<String, String> differentInput = new HashMap<String, String>() {
            {
                put("Column 1", "Value 1");
                put("Column 2", "Value 2-1");
                put("Column 3", "Value 3");
            }
        };
        biggerRecord = new FieldsRecord(biggerInput);
        differentRecord = new FieldsRecord(differentInput);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHashCode() {
        Map<String, String> input = new HashMap<String, String>() {
            {
                put("Column 1", "Value 1");
                put("Column 2", "Value 2");
                put("Column 3", "Value 3");
            }
        };
        copyRecord = new FieldsRecord(input);
        Assert.assertEquals(record.hashCode(), copyRecord.hashCode());
        Assert.assertNotEquals(record.hashCode(), biggerRecord.hashCode());
        Assert.assertNotEquals(record.hashCode(), differentRecord.hashCode());
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(this.record.matches(biggerRecord));
        Assert.assertFalse(biggerRecord.matches(this.record));
        Assert.assertFalse(differentRecord.matches(this.record));
        Assert.assertFalse(this.record.matches(differentRecord));
    }

    @Test
    public void testEqualsShouldReturnTrueForSimilarAndSameRecords() {
        Assert.assertTrue(this.record.equals(this.record));
        Assert.assertTrue(this.record.equals(copyRecord));
        Assert.assertTrue(copyRecord.equals(this.record));
    }

    @Test
    public void testEqualsShouldReturnFalseForDifferentRecords() {
        Assert.assertFalse(this.record.equals(differentRecord));
        Assert.assertFalse(differentRecord.equals(this.record));
        Assert.assertFalse(this.record.equals(biggerRecord));
        Assert.assertFalse(biggerRecord.equals(this.record));
    }

    @Test
    public void testEqualsShouldReturnFalseForNullRecord() {
        Assert.assertFalse(this.record.equals(null));
    }

    @Test
    public void testEqualsShouldReturnFalseForDifferentType() {
        Assert.assertFalse(this.record.equals(new Integer(0)));
    }

    @Test
    public void testCompate() {
        Assert.assertEquals(0, this.record.compareTo(record));
        Assert.assertEquals(-1, this.record.compareTo(null));
        Assert.assertEquals(-1, this.record.compareTo(new Integer(0)));
        Assert.assertEquals(1, this.record.compareTo(differentRecord));
        Assert.assertEquals(-1, this.record.compareTo(biggerRecord));
    }
}
