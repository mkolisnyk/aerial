package com.github.mkolisnyk.aerial.core.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class AerialTemplateMap {

    private Map<String, String> properties;

    public AerialTemplateMap() {
        properties = new HashMap<String, String>();
    }

    private static AerialTemplateMap instance;

    public String getProperty(String format, String name) throws IOException {
        String propName = getPropertyName(format, name);
        if (!this.properties.containsKey(propName)) {
            readResource(format);
        }
        return properties.get(propName);
    }

    private void readResource(String format) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                getDefaultResourcePath(format));
        Properties props = new Properties();
        props.load(in);

        for (Entry<Object, Object> property : props.entrySet()) {
            properties.put((String) property.getKey(), (String) property.getValue());
        }
    }

    public static String get(String format, String name) throws IOException {
        if (instance == null) {
            instance = new AerialTemplateMap();
        }
        return instance.getProperty(format, name);
    }

    public String getDefaultResourcePath(String format) {
        return "main/resources/generator/" + format + ".properties";
    }

    public String getPropertyName(String format, String name) {
        return "aerial." + format + "." + name;
    }
}
