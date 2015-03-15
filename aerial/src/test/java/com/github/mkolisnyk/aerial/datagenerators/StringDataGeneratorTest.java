package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class StringDataGeneratorTest {

    private String format;
    private InputRecord record;
    private TypedDataGenerator generator;

    public StringDataGeneratorTest(String description,
            String formatValue,
            InputRecord recordValue) {
        this.format = formatValue;
        this.record = recordValue;
    }

    @Parameters(name = "Test String generation: {0}.")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Any character string",
                    "(.*)",
                    new InputRecord("Name", "string", "(.*)", "")
                },
                {"Any number string",
                    "(\\d+)",
                    new InputRecord("Name", "string", "(\\d+)", "")
                },
                {"Any word string",
                    "(\\w+)",
                    new InputRecord("Name", "string", "(\\w+)", "")
                },
                {"Any space string",
                    "(\\s+)",
                    new InputRecord("Name", "string", "(\\s+)", "")
                },
                {"Specific format string",
                    "This is just a string",
                    new InputRecord("Name", "string", "This is just a string", "")
                },
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
            Assert.assertTrue(
                    String.format("The generated string '%s' doesn't match the format '%s'",
                            actual.getValue(),
                            this.format),
                    actual.getValue().matches(format) == actual.isValidInput());
        }
    }

}
