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

/**
 * @author Myk Kolisnyk
 *
 */
public abstract class TypedDataGenerator implements
        AerialDataGenerator {

    private InputRecord input;

    /**
     * .
     */
    public TypedDataGenerator(InputRecord inputValue) {
        this.input = inputValue;
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
        boolean validated = false;
        for (ValueExpression expression : this.getApplicableExpressions()) {
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

    public abstract ValueExpression[] getApplicableExpressions() throws Exception;
}
