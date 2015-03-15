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
    private StringRegexpValueExpression generator;

    public StringRegexpValueExpressionTest(
            String description,
            InputRecord recordValue) {
        this.record = recordValue;
    }

    @Parameters(name = "Test generate regexp string: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Excercising e-mail format",
                    new InputRecord("Name", "string", "(\\w+)[@](\\w+)[.](\\w+)", ""),
                },
                {"Excercising phone number format",
                    new InputRecord("Name", "string", "\\+\\(\\d{1,3}\\) \\d{8}", ""),
                },
                {"Excercising enumerations",
                    new InputRecord("Name", "string", "(JAN|FEB|MAR|APR|MAY|JUN)", ""),
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
        Assert.assertTrue(actualList.size() > 1);
        for (InputRecord actual : actualList) {
            Assert.assertEquals(String.format(
                        "Inconsistent valid/invalid format for pattern: %s , value: %s , valid: %s",
                        record.getValue(),
                        actual.getValue(),
                        "" + actual.isValidInput()),
                    actual.getValue().matches(record.getValue()), actual.isValidInput());
        }
    }
}
