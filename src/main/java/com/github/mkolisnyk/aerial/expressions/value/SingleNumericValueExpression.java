/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;

/**
 * @author Myk Kolisnyk
 *
 */
public class SingleNumericValueExpression extends ValueExpression {

    /**
     * @param inputValue
     */
    public SingleNumericValueExpression(InputRecord inputValue) {
        super(inputValue);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        InputRecord testRecord = (InputRecord) this.getInput().clone();
        this.validate();
        // Value is set explicitly
        result.add(testRecord);
        result.add(nonCurrentValue(this.getInput()));
        return result;
    }

    private InputRecord nonCurrentValue(InputRecord input) {
        InputRecord testRecord;
        Integer intValue = new Integer(input.getValue());
        String updatedValue = "";
        if (intValue == 0) {
            updatedValue = "-1";
        } else {
            updatedValue = "0";
        }
        testRecord = new InputRecord(
                input.getName(),
                input.getType(),
                updatedValue,
                input.getCondition(),
                false);
        return testRecord;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueType()
     */
    @Override
    public ValueExpressionType getValueType() {
        return ValueExpressionType.INTEGER;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getMatchPattern()
     */
    @Override
    public String getMatchPattern() {
        return "(([-0-9])(\\d*))";
    }
}
