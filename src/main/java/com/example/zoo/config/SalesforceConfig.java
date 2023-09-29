package com.example.zoo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("salesforceConfig")
@ConfigurationProperties(prefix = "salesforce")
public class SalesforceConfig {
    private String username;

    private String password;

    private String consumerKey;

    private String consumerSecret;

    private String tokenUrl;
}
