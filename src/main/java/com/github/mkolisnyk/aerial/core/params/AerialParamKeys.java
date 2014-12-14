package com.github.mkolisnyk.aerial.core.params;

public enum AerialParamKeys {
    INPUT_TYPE("-i"),
    SOURCE("-s"),
    OUTPUT_TYPE("-o"),
    DESTINATION("-d"),
    OTHER("");

    private String name;
    private AerialParamKeys(String value) {
        this.name = value;
    }

    public String toString() {
        return name;
    }

    public boolean isValue(String value) {
        return value.equals(name);
    }

    public static AerialParamKeys fromString(String textValue) {
        AerialParamKeys[] values = AerialParamKeys.values();
        for (AerialParamKeys value:values) {
            if (textValue.equals(value.toString())) {
                return value;
            }
        }
        return OTHER;
    }
}
