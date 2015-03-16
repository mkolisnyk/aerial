package com.github.mkolisnyk.aerial.document;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InputSectionTest {

    private InputSection section;
    private static String ls = System.lineSeparator();

    private String inputText;
    private boolean shouldPass;
    private int recordsCount;

    public InputSectionTest(String description, String inputTextValue, boolean shouldPassValue, int recordsCountValue) {
        this.inputText = inputTextValue;
        this.shouldPass = shouldPassValue;
        this.recordsCount = recordsCountValue;
    }

    @Parameters(name = "Test read input record: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Normal set of fields", "| Name | Type | Value | Condition |" + ls + "| nameValue | int | [0;100) | a > 0 |", true, 1},
                {"Only header is defined",  "| Name | Type | Value | Condition |", false, 0},
                {"No header defined", "| nameValue2 | int | [0;100) | a > 0 |" + ls + "| nameValue | int | [0;100) | a > 0 |", false, 0},
        });
    }

    @Before
    public void setUp() throws Exception {
        section = new InputSection(null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParseString() throws Exception {
        try {
            section.parse(inputText);
        } catch (Throwable e) {
            Assert.assertFalse("This was supposed to fail", shouldPass);
            return;
        }
        Assert.assertEquals(
                "The records count doesn't match",
                this.recordsCount, section.getInputs().size());
    }
}
