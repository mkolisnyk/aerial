/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

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
     * @see com.github.mkolisnyk.aerial.AerialDataGenerator#validate()
     */
    public void validate() {
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
    }

    public abstract ValueExpression[] getApplicableExpressions();
}
