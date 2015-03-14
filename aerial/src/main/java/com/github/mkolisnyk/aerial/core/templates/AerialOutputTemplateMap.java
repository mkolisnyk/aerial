package com.github.mkolisnyk.aerial.core.templates;

import java.io.IOException;

public final class AerialOutputTemplateMap extends AerialTemplateMap {

    private static AerialOutputTemplateMap instance;

    private AerialOutputTemplateMap() {
        super();
    }

    public static String get(String format, String name) throws IOException {
        if (instance == null) {
            instance = new AerialOutputTemplateMap();
        }
        return instance.getProperty(format, name);
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.core.templates.AerialTemplateMap#getDefaultResourcePath(java.lang.String)
     */
    @Override
    public String getDefaultResourcePath(String format) {
        return "main/resources/generator/" + format + ".properties";
    }

    @Override
    public String getPropertyName(String format, String name) {
        return "aerial." + format + "." + name;
    }
}
