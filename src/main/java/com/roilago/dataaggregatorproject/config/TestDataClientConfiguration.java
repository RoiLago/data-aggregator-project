package com.roilago.dataaggregatorproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "test-data-client")
@Validated
public class TestDataClientConfiguration {

    @NotBlank
    private String baseUrl;

    @NotBlank
    private String testDataEndpoint;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getTestDataEndpoint() {
        return testDataEndpoint;
    }

    public void setTestDataEndpoint(String testDataEndpoint) {
        this.testDataEndpoint = testDataEndpoint;
    }
}
