package com.thales.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    /**
     * Provides a RestTemplate bean for calling external HTTP APIs.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}