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
public class SingleNumericValueExpressionTest {

    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private SingleNumericValueExpression generator;
    private boolean validationPass;

    public SingleNumericValueExpressionTest(
            String description,
            InputRecord recordValue,
            List<InputRecord> expectedRecordsValue,
            boolean validationPassValue) {
        this.record = recordValue;
        this.expectedRecords = expectedRecordsValue;
        this.validationPass = validationPassValue;
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
                    },
                    true
                },
                {"Single negative value field should return value or 0 for negative case",
                    new InputRecord("Name", "int", "-3", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "-3", "", true));
                            add(new InputRecord("Name", "int", "0", "", false));
                        }
                    },
                    true
                },
                {"Single 0 value field should return 0 or -1 for negative case", 
                    new InputRecord("Name", "int", "0", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "int", "0", "", true));
                            add(new InputRecord("Name", "int", "-1", "", false));
                        }
                    },
                    true
                },
                {"Improper format shouldn't pass validation", 
                    new InputRecord("Name", "int", "A", ""),
                    new ArrayList<InputRecord>(),
                    false
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new SingleNumericValueExpression(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGenerate() throws Exception {
        List<InputRecord> actualList = new ArrayList<InputRecord>();
        try {
            actualList = generator.generate();
        } catch (Throwable e) {
            Assert.assertFalse(
                    "This expression should pass validation",
                    this.validationPass);
            return;
        }
        Assert.assertTrue(
                "This expression should fail validation",
                this.validationPass);
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
