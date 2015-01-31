package com.github.mkolisnyk.aerial.core.templates;

public final class AerialOutputTemplateMap extends AerialTemplateMap {

    private AerialOutputTemplateMap() {
        super();
    }

    /* (non-Javadoc)
     * @see com.github.mkolisnyk.aerial.core.templates.AerialTemplateMap#getDefaultResourcePath(java.lang.String)
     */
    @Override
    public String getDefaultResourcePath(String format) {
        return "main/resources/generator/" + format + ".properties";
    }
}
