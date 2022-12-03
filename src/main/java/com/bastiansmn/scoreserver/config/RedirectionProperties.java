package com.bastiansmn.scoreserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "redirection", ignoreUnknownFields = false)
public class RedirectionProperties {

    private String url;

}
