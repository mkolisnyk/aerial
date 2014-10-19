/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialDataGenerator;
import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.DateRangeValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.NumericRangeValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.SingleDateValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.SingleNumericValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.StringRegexpValueExpression;
import com.github.mkolisnyk.aerial.util.Clock;
import com.github.mkolisnyk.aerial.util.SystemClock;

/**
 * @author Myk Kolisnyk
 *
 */
public class TypedDataGenerator implements
        AerialDataGenerator {

    private InputRecord input;

    private Clock clock;

    /**
     * @param inputValue
     */
    public TypedDataGenerator(InputRecord inputValue) {
        this.input = inputValue;
        this.clock = new SystemClock();
    }

    public void setClock(Clock clockValue) {
        this.clock = clockValue;
    }

    /**
     * @return the input
     */
    public final InputRecord getInput() {
        return input;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#generate()
     */
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        this.validate();
        for (ValueExpression expression : this.getApplicableExpressions()) {
            try {
                expression.validate();
            } catch (Throwable e) {
                continue;
            }
            result.addAll(expression.generate());
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#validate()
     */
    public void validate() throws Exception {
        boolean validated = true;
        ValueExpression[] expressions = this.getApplicableExpressions();
        for (ValueExpression expression : expressions) {
            try {
                expression.validate();
                validated = true;
            } catch (Throwable e) {
                validated = false;
            }
            if (validated) {
                break;
            }
        }
        Assert.assertTrue("At least one expression type should match the input", validated);
    }

    public ValueExpression[] getApplicableExpressions() throws Exception {
        return new ValueExpression[]{
                new StringRegexpValueExpression(this.getInput()),
                new SingleDateValueExpression(this.getInput(), this.clock),
                new DateRangeValueExpression(this.getInput(), this.clock),
                new SingleNumericValueExpression(this.getInput()),
                new NumericRangeValueExpression(this.getInput()),
        };
    }
}
