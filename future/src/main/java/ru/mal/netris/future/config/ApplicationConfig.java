package ru.mal.netris.future.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.mal.netris.future.config.properties.RestProperties;

@Configuration
@EnableConfigurationProperties(value = {RestProperties.class})
public class ApplicationConfig {
}
