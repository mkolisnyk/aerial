package com.github.mkolisnyk.aerial.core.templates;

import java.io.IOException;

public final class AerialInputTemplateMap extends AerialTemplateMap {

    private AerialInputTemplateMap() {
        super();
    }
    private static AerialInputTemplateMap instance;

    public static String get(String format, String name) throws IOException {
        if (instance == null) {
            instance = new AerialInputTemplateMap();
        }
        return instance.getProperty(format, name);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.core.templates.AerialTemplateMap#getDefaultResourcePath(java.lang.String)
     */
    @Override
    public String getDefaultResourcePath(String format) {
        return "main/resources/input/" + format + ".properties";
    }

    @Override
    public String getPropertyName(String format, String name) {
        return "aerial." + format + "." + name;
    }
}
