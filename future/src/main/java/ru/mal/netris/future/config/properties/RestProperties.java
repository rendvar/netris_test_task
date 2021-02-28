package ru.mal.netris.future.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("netris.rest")
public class RestProperties {

    private String cameraInfoUrl;

}
