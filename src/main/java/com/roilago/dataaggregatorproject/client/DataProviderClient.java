package com.roilago.dataaggregatorproject.client;

import com.roilago.dataaggregatorproject.config.TestDataClientConfiguration;
import com.roilago.dataaggregatorproject.service.model.DataProvidedRootModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class DataProviderClient {

    private static final String SERVICE_NAME = "data-provider-client";

    private final RestTemplate restTemplate;
    private final TestDataClientConfiguration clientConfiguration;

    @Autowired
    public DataProviderClient(RestTemplateBuilder restTemplateBuilder,
                              TestDataClientConfiguration clientConfiguration) {
        this.restTemplate = restTemplateBuilder.build();
        this.clientConfiguration = clientConfiguration;
    }

    public Optional<DataProvidedRootModel> getTestData() {
        try {
            DataProvidedRootModel response = restTemplate.getForObject(
                    clientConfiguration.getBaseUrl() + clientConfiguration.getTestDataEndpoint(),
                    DataProvidedRootModel.class);
            log.info("Returned response from getTestData={}", response);
            return Optional.ofNullable(response);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error in client={}: [Status code={}, response={}]", SERVICE_NAME, e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (RestClientException e) {
            log.error("I/O error calling client with name={} and endpoint={}",
                    SERVICE_NAME, clientConfiguration.getTestDataEndpoint());
            return Optional.empty();
        }
    }
}
