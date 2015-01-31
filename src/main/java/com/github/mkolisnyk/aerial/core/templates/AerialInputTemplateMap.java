package com.github.mkolisnyk.aerial.core.templates;

public final class AerialInputTemplateMap extends AerialTemplateMap {

    private AerialInputTemplateMap() {
        super();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.core.templates.AerialTemplateMap#getDefaultResourcePath(java.lang.String)
     */
    @Override
    public String getDefaultResourcePath(String format) {
        return "main/resources/input/" + format + ".properties";
    }
}
