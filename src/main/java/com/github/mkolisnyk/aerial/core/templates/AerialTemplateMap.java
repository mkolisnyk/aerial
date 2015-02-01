package com.github.mkolisnyk.aerial.core.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public abstract class AerialTemplateMap {

    private Map<String, String> properties;

    public AerialTemplateMap() {
        properties = new HashMap<String, String>();
    }

    public String getProperty(String format, String name) throws IOException {
        String propName = this.getPropertyName(format, name);
        if (!this.properties.containsKey(propName)) {
            readResource(format);
        }
        return properties.get(propName);
    }

    public void readResource(String format) throws IOException {
        String resourcePath = this.getDefaultResourcePath(format);
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
        Properties props = new Properties();
        props.load(in);
        in.close();
        for (Entry<Object, Object> property : props.entrySet()) {
            properties.put((String) property.getKey(), (String) property.getValue());
        }
    }
    public abstract String getDefaultResourcePath(String format);
    public abstract String getPropertyName(String format, String name);
}
