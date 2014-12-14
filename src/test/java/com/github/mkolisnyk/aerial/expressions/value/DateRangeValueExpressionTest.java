/**
 * .
 */
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

/**
 * @author Myk Kolisnyk
 *
 */
@RunWith(Parameterized.class)
public class DateRangeValueExpressionTest {

    private DateRangeValueExpression expression;
    private InputRecord record;
    private boolean validationPass;

    public DateRangeValueExpressionTest(
            String description,
            InputRecord recordValue,
            boolean validationPassValue) {
        this.record = recordValue;
        this.validationPass = validationPassValue;
    }

    @Parameters(name = "Test date range value: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"All inclusive range",
                    new InputRecord("Name", "date", "[01-01-2000;02-10-2010], Format: dd-MM-yyyy", ""),
                    true
                },
                {"Upper exclusive range",
                    new InputRecord("Name", "date", "[01-01-2000;02-10-2010), Format: dd-MM-yyyy", ""),
                    true
                },
                {"All exclusive range",
                    new InputRecord("Name", "date", "(01-01-2000;02-10-2010), Format: dd-MM-yyyy", ""),
                    true
                },
                {"Lower exclusive range",
                    new InputRecord("Name", "date", "(01-01-2000;02-10-2010], Format: dd-MM-yyyy", ""),
                    true
                },
                {"Spaces should be handled",
                    new InputRecord("Name", "date", "( 01-01-2000 ; 02-10-2010 ], Format:   dd-MM-yyyy", ""),
                    true
                },
                {"Invalid order range",
                    new InputRecord("Name", "date", "[01-01-2010;02-10-2000], Format: dd-MM-yyyy", ""),
                    false
                },
                {"Empty range should cause the failure",
                    new InputRecord("Name", "date", "[02-10-2010;02-10-2010], Format: dd-MM-yyyy", ""),
                    false
                },
                {"Wrong format should cause the failure",
                    new InputRecord("Name", "date", "[10-02-2010;02-10-2010], Format: MM/dd/yyyy", ""),
                    false
                },
                {"Wrong but matching format should cause the failure",
                    new InputRecord("Name", "date", "[10-02-2010;02-10-2010], Format: MM-dd-yyyy", ""),
                    false
                },
        });
    }

    @Before
    public void setUp() throws Exception {
        expression = new DateRangeValueExpression(record);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testParse() throws Exception {
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
