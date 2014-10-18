/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.DateRangeValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.SingleDateValueExpression;

/**
 * @author Myk Kolisnyk
 *
 */
public class DateDataGenerator extends TypedDataGenerator {

    /**
     * @param inputValue
     */
    public DateDataGenerator(InputRecord inputValue) {
        super(inputValue);
        // TODO Auto-generated constructor stub
    }

    @Override
    public ValueExpression[] getApplicableExpressions() throws Exception {
        return new ValueExpression[]{
                new SingleDateValueExpression(this.getInput()),
                new DateRangeValueExpression(this.getInput()),
        };
    }

}
