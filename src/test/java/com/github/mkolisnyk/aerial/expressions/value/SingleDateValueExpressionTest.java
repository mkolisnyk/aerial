package com.github.mkolisnyk.aerial.expressions.value;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.tools.ant.taskdefs.XSLTProcess.Param;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class SingleDateValueExpressionTest {
    private String format;
    private InputRecord record;
    private List<InputRecord> expectedRecords;
    private SingleDateValueExpression generator;
    private boolean validationPass;


    public SingleDateValueExpressionTest(
            String description,
            String formatValue,
            InputRecord recordValue,
            List<InputRecord> expectedRecordsValue,
            boolean validationPassValue) {
        this.format = formatValue;
        this.record = recordValue;
        this.expectedRecords = expectedRecordsValue;
        this.validationPass = validationPassValue;
    }

    @Parameters(name = "Test Date input record: {0}. Format: {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Simple date format: ",
                    "dd/mm/yyyy",
                    new InputRecord("Name", "date", "dd/mm/yyyy", ""),
                    new ArrayList<InputRecord>()
                    {
                        {
                            add(new InputRecord("Name", "date", "29/10/2014", "", true));
                            add(new InputRecord("Name", "date", "11/09/2001", "", false));
                        }
                    },
                    true
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        generator = new SingleDateValueExpression(record);
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
