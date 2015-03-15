package com.github.mkolisnyk.aerial.core.params;

public enum AerialInputFormat {
    PLAIN("plain"),
    CUSTOM("custom");

    private String name;
    private AerialInputFormat(String value) {
        this.name = value;
    }

    public String toString() {
        return name;
    }

    public void setCurrent() {
        System.setProperty("aerial.input.format", this.toString());
    }

    public static AerialInputFormat getCurrent() {
        String format = System.getProperty("aerial.input.format");
        if (format == null) {
            System.setProperty("aerial.input.format", AerialInputFormat.PLAIN.toString());
            format = System.getProperty("aerial.input.format");
        }
        return fromString(format);
    }

    public static AerialInputFormat fromString(String textValue) {
        AerialInputFormat[] values = AerialInputFormat.values();
        for (AerialInputFormat value:values) {
            if (textValue.equals(value.toString())) {
                return value;
            }
        }
        return CUSTOM;
    }
}
