package com.github.mkolisnyk.aerial.core.params;

public enum AerialSourceType {
    NONE("none"),
    STRING("text"),
    FILE("file"),
    JIRA("jira");

    private String name;
    private AerialSourceType(String value) {
        this.name = value;
    }

    public String toString() {
        return name;
    }

    public boolean isValue(String value) {
        return value.equals(name);
    }

    public static AerialSourceType fromString(String textValue) {
        AerialSourceType[] values = AerialSourceType.values();
        for (AerialSourceType value:values) {
            if (textValue.equals(value.toString())) {
                return value;
            }
        }
        return NONE;
    }
}
