package com.github.mkolisnyk.aerial.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

public final class AerialTemplateMap {

    private Map<String, String> properties;

    private AerialTemplateMap() {
        properties = new HashMap<String, String>();
    }

    private static AerialTemplateMap instance;

    public String getProperty(String format, String name) throws IOException {
        String propName = "aerial." + format + "." + name;
        if (!this.properties.containsKey(propName)) {
            readResource(format);
        }
        return properties.get(propName);
    }

    private void readResource(String format) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                "main/resources/generator/" + format + ".properties");
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
}
