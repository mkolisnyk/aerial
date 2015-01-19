package com.github.mkolisnyk.aerial.core.params;

public enum AerialOutputFormat {
    CUCUMBER("cucumber"),
    JBEHAVE("jbehave"),
    JUNIT("junit"),
    CUSTOM("custom");

    private String name;
    private AerialOutputFormat(String value) {
        this.name = value;
    }

    public String toString() {
        return name;
    }

    public boolean isValue(String value) {
        return value.equals(name);
    }

    public static AerialOutputFormat fromString(String textValue) {
        AerialOutputFormat[] values = AerialOutputFormat.values();
        for (AerialOutputFormat value:values) {
            if (textValue.equals(value.toString())) {
                return value;
            }
        }
        return CUSTOM;
    }
}
