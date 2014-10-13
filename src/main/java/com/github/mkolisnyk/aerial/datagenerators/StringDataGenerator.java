/**
 * .
 */
package com.github.mkolisnyk.aerial.datagenerators;

import com.github.mkolisnyk.aerial.document.InputRecord;
import com.github.mkolisnyk.aerial.expressions.ValueExpression;
import com.github.mkolisnyk.aerial.expressions.value.StringRegexpValueExpression;

/**
 * @author Myk Kolisnyk
 *
 */
public class StringDataGenerator extends TypedDataGenerator {

    /**
     * @param inputValue
     */
    public StringDataGenerator(InputRecord inputValue) {
        super(inputValue);
    }

    @Override
    public ValueExpression[] getApplicableExpressions() {
        return new ValueExpression[]{
                new StringRegexpValueExpression(this.getInput()),
        };
    }

}
