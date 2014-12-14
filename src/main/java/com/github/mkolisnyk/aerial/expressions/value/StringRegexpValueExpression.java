/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.util.ArrayList;
import java.util.List;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;
import com.mifmif.common.regex.Generex;

/**
 * @author Myk Kolisnyk
 *
 */
public class StringRegexpValueExpression extends ValueExpression {

    /**
     * @param inputValue
     */
    public StringRegexpValueExpression(InputRecord inputValue) {
        super(inputValue);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueType()
     */
    @Override
    public ValueExpressionType getValueType() {
        return ValueExpressionType.STRING;
    }


    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        // Generated valid string
        Generex generex = new Generex(this.getInput().getValue());
        String value = generex.random(1);
        InputRecord testRecord = this.getInput();
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        value,
                        testRecord.getCondition(),
                        value.matches(testRecord.getValue()))
        );
        // Empty string
        value = "";
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        value,
                        testRecord.getCondition(),
                        value.matches(testRecord.getValue()))
        );
        // Doubled string (repetitive pattern)
        value = generex.random(1);
        value = value.concat(value);
        result.add(
                new InputRecord(
                        testRecord.getName(),
                        testRecord.getType(),
                        value,
                        testRecord.getCondition(),
                        value.matches(testRecord.getValue()))
        );
        // Pattern itself
        result.add(
                new InputRecord(
                    testRecord.getName(),
                    testRecord.getType(),
                    testRecord.getValue(),
                    testRecord.getCondition(),
                    testRecord.getValue().matches(testRecord.getValue()))
                );
        return result;
    }
}
