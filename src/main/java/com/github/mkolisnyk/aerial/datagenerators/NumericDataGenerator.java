/**
 * 
 */
package com.github.mkolisnyk.aerial.datagenerators;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
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

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#generate()
     */
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
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

    @Override
    public ValueExpression[] getApplicableExpressions() {
        return new ValueExpression[]{
                new SingleNumericValueExpression(this.getInput())
        };
    }

}
