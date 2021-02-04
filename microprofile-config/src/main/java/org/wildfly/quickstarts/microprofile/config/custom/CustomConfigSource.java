package org.wildfly.quickstarts.microprofile.config.custom;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public CustomConfigSource() {
        properties = new HashMap<>();
        properties.put("custom.config.source.prop", "MyCustomValue");
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return "Custom Config Source with predefined values";
    }

    @Override
    public int getOrdinal() {
        return 400;
    }
}
