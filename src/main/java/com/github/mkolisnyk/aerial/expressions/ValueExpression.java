/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions;

import java.util.List;

import org.junit.Assert;

import com.github.mkolisnyk.aerial.AerialDataGenerator;
import com.github.mkolisnyk.aerial.document.InputRecord;

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class ValueExpression implements AerialDataGenerator {

    private InputRecord input;

    /**
     * .
     */
    public ValueExpression(InputRecord inputValue) {
        this.input = inputValue;
    }

    public abstract List<InputRecord> generate() throws Exception;

    /**
     * @return the input
     */
    public final InputRecord getInput() {
        return input;
    }

    public String getMatchPattern() {
        return "(.*)";
    }

    public String getValueTypeName() {
        return getValueType().toString();
    }

    public ValueExpressionType getValueType() {
        return ValueExpressionType.UNKNOWN;
    }

    public void validate() throws Exception {
        ValueExpressionType type = ValueExpressionType.fromString(this.getInput().getType());
        if (!type.isOfType(ValueExpressionType.UNKNOWN.toString())) {
            Assert.assertTrue(this.getValueType().isOfType(this.getInput().getType()));
        } else {
            Assert.assertEquals(
                    this.getValueTypeName().toLowerCase().trim(),
                    this.getInput().getType().toLowerCase().trim());
        }
        Assert.assertTrue(
                String.format(
                        "The \"%s\" input value doesn't match"
                        + " the expected pattern: \"%s\"",
                        this.getInput().getValue(),
                        this.getMatchPattern()),
                this.getInput().getValue().matches(getMatchPattern()));
    }

}
