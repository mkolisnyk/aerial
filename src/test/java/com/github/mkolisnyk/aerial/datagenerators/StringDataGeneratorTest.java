package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class StringDataGeneratorTest {

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private StringDataGenerator generator;

    public StringDataGeneratorTest(String description,
            InputRecord recordValue, List<InputRecord> expectedRecordsValue) {
        this.record = recordValue;
        this.expectedRecords = expectedRecordsValue;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Single value field should return value or 0 for negative case",
                    new InputRecord("Name", "int", "3", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "3", "", true));
                            add(new InputRecord("Name", "int", "0", "", false));
                        }
                }},
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new StringDataGenerator(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerate() throws Exception {
        List<InputRecord> actualList = generator.generate();
        for (InputRecord actual : actualList) {
            Assert.assertTrue("Unexpected record found: " + actual,
                    this.expectedRecords.contains(actual));
        }
        for (InputRecord expected : this.expectedRecords) {
            Assert.assertTrue("Expected record wasn't found: " + expected,
                    actualList.contains(expected));
        }
    }

}
