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

    public ValueExpressionType getValueType() {
        return ValueExpressionType.UNKNOWN;
    }

    public void validate() throws Exception {
        Assert.assertTrue(this.getValueType().isOfType(this.getInput().getType()));
        Assert.assertTrue(
                String.format(
                        "The \"%s\" input value doesn't match"
                        + " the expected pattern: \"%s\"",
                        this.getInput().getValue(),
                        this.getMatchPattern()),
                this.getInput().getValue().matches(getMatchPattern()));
    }

}
