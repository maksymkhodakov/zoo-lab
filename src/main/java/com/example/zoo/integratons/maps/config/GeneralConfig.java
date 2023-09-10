package com.example.zoo.integratons.maps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GeneralConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
