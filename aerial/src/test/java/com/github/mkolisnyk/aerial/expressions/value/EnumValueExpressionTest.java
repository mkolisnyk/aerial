package com.github.mkolisnyk.aerial.expressions.value;

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
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;

@RunWith(Parameterized.class)
public class EnumValueExpressionTest {

    private InputRecord record;
    private List<String> enumValues;
    private EnumValueExpression generator;

    public EnumValueExpressionTest(
            String description,
            InputRecord recordValue,
            List<String> enumValuesParam) {
        this.record = recordValue;
        this.enumValues = enumValuesParam;
    }

    @Before
    public void setUp() throws Exception {
        generator = new EnumValueExpression(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Parameters(name = "Test generate enum: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"Excercising simple valid enum",
                    new InputRecord("Value", "enum", "Winter;Spring;Summer;Autumn", ""),
                    new ArrayList<String>() {
                        {
                            add("Winter");
                            add("Spring");
                            add("Summer");
                            add("Autumn");
                            add("retniW");
                        }
                    }
                },
                {"Excercising delimiter masking",
                    new InputRecord("Value", "enum", "Win\\;er;Spring;Summer;Autumn", ""),
                    new ArrayList<String>() {
                        {
                            add("Win;er");
                            add("Spring");
                            add("Summer");
                            add("Autumn");
                            add("re;niW");
                        }
                    }
                },
                {"Excercising enum and check trim spaces",
                    new InputRecord("Value", "enum", "   Winter  ;  Spring ;   Summer ;      Autumn  ", ""),
                    new ArrayList<String>() {
                        {
                            add("Winter");
                            add("Spring");
                            add("Summer");
                            add("Autumn");
                            add("retniW");
                        }
                    }

                },
                {"Excercising empty enum",
                    new InputRecord("Value", "enum", "", ""),
                    new ArrayList<String>()
                },
        });
    }

    @Test
    public void testGenerateShouldHandleEnumsProperly() throws Exception {
        List<InputRecord> results = generator.generate();
        Assert.assertEquals(ValueExpressionType.ENUM, generator.getValueType());
        Assert.assertEquals(this.enumValues.size(), results.size());
        for (InputRecord result : results) {
            Assert.assertTrue(
                    String.format("Value \"%s\" wasn't found in expected list", result.getValue()),
                    this.enumValues.contains(result.getValue()));
        }
    }

    @Test
    public void testValidateShouldProcessCorrectly() throws Exception {
        generator.validate();
    }

}
