package com.gxa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    
    @Value("${spring.ai.api.url:https://api.deepseek.com/v1/chat/completions}")
    private String apiUrl;
    
    @Value("${spring.ai.api.key:}")
    private String apiKey;
    
    @Value("${spring.ai.api.model:deepseek-chat}")
    private String model;
    
    @Value("${spring.ai.api.timeout:30000}")
    private Integer timeout;
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getModel() {
        return model;
    }
    
    public Integer getTimeout() {
        return timeout;
    }
}
