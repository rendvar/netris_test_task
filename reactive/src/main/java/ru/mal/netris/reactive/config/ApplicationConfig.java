package ru.mal.netris.reactive.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.mal.netris.reactive.config.properties.RestProperties;

@Configuration
@EnableConfigurationProperties(value = {RestProperties.class})
public class ApplicationConfig {
}
