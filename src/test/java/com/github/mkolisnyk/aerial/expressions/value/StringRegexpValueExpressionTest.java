package com.github.mkolisnyk.aerial.expressions.value;

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
public class StringRegexpValueExpressionTest {

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private StringRegexpValueExpression generator;

    public StringRegexpValueExpressionTest(
            String description,
            InputRecord recordValue,
            List<InputRecord> expectedRecordsValue) {
        this.record = recordValue;
        this.expectedRecords = expectedRecordsValue;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Single value field should return value or 0 for negative case",
                    new InputRecord("Name", "string", "([a-z0-9]+)[@]([a-z0-9]+)[.]([a-z0-9]+)", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "string", "3", "", true));
                        }
                    }
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new StringRegexpValueExpression(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerate() throws Exception {
        List<InputRecord> actualList = new ArrayList<InputRecord>();
        actualList = generator.generate();
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
