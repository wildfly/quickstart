package org.wildfly.quickstarts.microprofile.config.custom;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class CustomPropertiesFileProvider implements ConfigSourceProvider {

    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        List<ConfigSource> configSources = new ArrayList<>();

        configSources.add(new ConfigSource() {
            @Override
            public Map<String, String> getProperties() {
                return reloadPropertiesFile();
            }

            @Override
            public String getValue(String propertyName) {
                return reloadPropertiesFile().get(propertyName);
            }

            @Override
            public String getName() {
                return "Custom dynamic configuration source";
            }
        });

        return configSources;
    }

    private Map<String, String> reloadPropertiesFile() {
        Properties properties = new Properties();
        Path customPropertiesPath = Paths.get(getJBossHome() + "/custom.properties");

        if (!Files.exists(customPropertiesPath)) {
            return new HashMap<>();
        }

        try (FileInputStream is = new FileInputStream(customPropertiesPath.toFile())) {
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return properties.entrySet().stream().collect(
            Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> entry.getValue().toString()
            )
        );
    }

    public static String getJBossHome() {
        String jbossHome = System.getenv("JBOSS_HOME");

        if (jbossHome == null) {
            jbossHome = System.getProperty("jbossHome");
        }

        return jbossHome;
    }
}
