/**
 * .
 */
package com.github.mkolisnyk.aerial.expressions;

/**
 * @author Myk Kolisnyk
 *
 */
public enum ValueExpressionType {

    UNKNOWN("unknown"),
    INTEGER("int"),
    STRING("string"),
    DATE("date");

    private String valueType;

    ValueExpressionType(String value) {
        this.valueType = value;
    }

    public boolean isOfType(String inputType) {
        return this.valueType.equalsIgnoreCase(inputType.trim());
    }
}
