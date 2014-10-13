/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.NumericRangeValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.SingleNumericValueExpression;

/**
 * @author Myk Kolisnyk
 *
 */
public class NumericDataGenerator extends TypedDataGenerator {

    /**
     * @param inputValue
     */
    public NumericDataGenerator(InputRecord inputValue) {
        super(inputValue);
        // TODO Auto-generated constructor stub
    }

    @Override
    public ValueExpression[] getApplicableExpressions() {
        return new ValueExpression[]{
                new SingleNumericValueExpression(this.getInput()),
                new NumericRangeValueExpression(this.getInput()),
        };
    }

}
