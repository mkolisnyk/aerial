/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.DateRangeValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.SingleDateValueExpression;
import com.github.mkolisnyk.aerial.util.Clock;
import com.github.mkolisnyk.aerial.util.SystemClock;

/**
 * @author Myk Kolisnyk
 *
 */
public class DateDataGenerator extends TypedDataGenerator {

    private Clock clock;

    /**
     * @param inputValue
     */
    public DateDataGenerator(InputRecord inputValue) {
        super(inputValue);
        this.clock = new SystemClock();
    }

    public void setClock(Clock clockValue) {
        this.clock = clockValue;
    }

    @Override
    public ValueExpression[] getApplicableExpressions() throws Exception {
        return new ValueExpression[]{
                new SingleDateValueExpression(this.getInput(), this.clock),
                new DateRangeValueExpression(this.getInput(), this.clock),
        };
    }

}
