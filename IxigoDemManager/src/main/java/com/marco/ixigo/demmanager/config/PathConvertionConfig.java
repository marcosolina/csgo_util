package com.marco.ixigo.demmanager.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

/**
 * Custom converter to convert a String property into a Path and use it with DI
 * 
 * @author Marco
 *
 */
@SpringBootConfiguration
public class PathConvertionConfig {
    public class PathConverter implements Converter<String, Path> {

        @Override
        public Path convert(String path) {
            return Paths.get(path);
        }
    }

    @Bean
    @ConfigurationPropertiesBinding
    public PathConverter getStringToPathConverter() {
        return new PathConverter();
    }
}
