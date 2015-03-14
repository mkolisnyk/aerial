/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions.value;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.ValueExpressionType;

/**
 * @author Myk Kolisnyk
 *
 */
public class EnumValueExpression extends ValueExpression {

    private List<String> values;

    /**
     * @param inputValue
     */
    public EnumValueExpression(InputRecord inputValue) {
        super(inputValue);
        parse();
    }

    private void parse() {
        values = new ArrayList<String>();
        for (String value : this.getInput().getValue().trim().split("(?<!\\\\);")) {
            if (!value.trim().equals("")) {
                values.add(value.trim());
            }
        }
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#generate()
     */
    @Override
    public List<InputRecord> generate() throws Exception {
        List<InputRecord> result = new ArrayList<InputRecord>();
        InputRecord testRecord = (InputRecord) this.getInput().clone();
        this.validate();
        for (String value : values) {
            result.add(
                    new InputRecord(
                            testRecord.getName(),
                            testRecord.getType(),
                            value.replaceAll("\\\\;", ";"),
                            testRecord.getCondition(),
                            true));
        }
        String negativeValue = "";
        for (String value : values) {
            negativeValue = StringUtils.reverse(negativeValue.concat(value.replaceAll("\\\\;", ";")));
            if (!values.contains(negativeValue)) {
                result.add(
                    new InputRecord(
                            testRecord.getName(),
                            testRecord.getType(),
                            negativeValue,
                            testRecord.getCondition(),
                            false));
                break;
            }
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.expressions.ValueExpression#getValueType()
     */
    @Override
    public ValueExpressionType getValueType() {
        return ValueExpressionType.ENUM;
    }
}
