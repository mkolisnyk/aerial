package com.github.mkolisnyk.aerial.expressions.value;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.mkolisnyk.aerial.document.InputRecord;

@RunWith(Parameterized.class)
public class NumericRangeValueExpressionTest {

    private NumericRangeValueExpression expression;
    private InputRecord record;
    private boolean validationPass;

    public NumericRangeValueExpressionTest(
            String description,
            InputRecord recordValue,
            boolean validationPassValue) {
        this.record = recordValue;
        this.validationPass = validationPassValue;
    }

    @Parameters(name = "Test numeric range value: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"All inclusive range",
                    new InputRecord("Name", "int", "[10;15]", ""),
                    true
                },
                {"Negative range",
                    new InputRecord("Name", "int", "[-15;-10]", ""),
                    true
                },
                {"Upper exclusive range",
                    new InputRecord("Name", "int", "[10;15)", ""),
                    true
                },
                {"All exclusive range",
                    new InputRecord("Name", "int", "(10;15)", ""),
                    true
                },
                {"Lower exclusive range",
                    new InputRecord("Name", "int", "(10;15]", ""),
                    true
                },
                /*{"Spaces should be handled",
                    new InputRecord("Name", "int", "(10 ; 15 ]", ""),
                    true
                },*/
                {"Invalid order range",
                    new InputRecord("Name", "int", "[15;10]", ""),
                    false
                },
                {"Empty range should cause the failure",
                    new InputRecord("Name", "int", "(10;10)", ""),
                    false
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        expression = new NumericRangeValueExpression(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() {
        try {
            expression.validate();
        } catch (Throwable e) {
            Assert.assertFalse("This validation was supposed to pass",
                    this.validationPass);
            return;
        }
        Assert.assertTrue(
                "This validation was supposed to fail",
                this.validationPass);
        expression.parse();
        Assert.assertEquals(record.getValue().replaceAll(" ", ""),
                expression.toString());
    }

}
