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
    ENUM("enum"),
    DATE("date");

    private String valueType;

    ValueExpressionType(String value) {
        this.valueType = value;
    }

    public static ValueExpressionType fromString(String textValue) {
        for (ValueExpressionType value : ValueExpressionType.values()) {
            if (textValue.equals(value.toString())) {
                return value;
            }
        }
        return UNKNOWN;
    }

    public String toString() {
        return valueType;
    }

    public boolean isOfType(String inputType) {
        return this.valueType.equalsIgnoreCase(inputType.trim());
    }
}
