package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class NumericDataGeneratorTest {

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private TypedDataGenerator generator;

    public NumericDataGeneratorTest(String description,
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
                {"Single 0 value field should return 0 or -1 for negative case", 
                    new InputRecord("Name", "int", "0", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "0", "", true));
                            add(new InputRecord("Name", "int", "-1", "", false));
                        }
                }},
                {"Inclusive range", 
                    new InputRecord("Name", "int", "[0;10)", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "5", "", true));// In range value
                            add(new InputRecord("Name", "int", "0", "", true));// Border value
                            add(new InputRecord("Name", "int", "10", "", false));// In range value
                            add(new InputRecord("Name", "int", "11", "", false));// Out of range value
                            add(new InputRecord("Name", "int", "-1", "", false));// Out of range value
                        }
                }},
                {"All Inclusive range", 
                    new InputRecord("Name", "int", "[0;10]", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "5", "", true));// In range value
                            add(new InputRecord("Name", "int", "0", "", true));// Border value
                            add(new InputRecord("Name", "int", "10", "", true));// In range value
                            add(new InputRecord("Name", "int", "11", "", false));// Out of range value
                            add(new InputRecord("Name", "int", "-1", "", false));// Out of range value
                        }
                }},
                {"All exclusive range", 
                    new InputRecord("Name", "int", "(0;10)", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "5", "", true));// In range value
                            add(new InputRecord("Name", "int", "0", "", false));// Border value
                            add(new InputRecord("Name", "int", "10", "", false));// In range value
                            add(new InputRecord("Name", "int", "11", "", false));// Out of range value
                            add(new InputRecord("Name", "int", "-1", "", false));// Out of range value
                        }
                }},
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new TypedDataGenerator(record);
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
