package com.roilago.dataaggregatorproject.client;

import com.roilago.dataaggregatorproject.config.TestDataClientConfiguration;
import com.roilago.dataaggregatorproject.service.model.DataProvidedRootModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DataProviderClientTest {

    private static final String BASE_URL = "base-url";
    private static final String DATA_ENDPOINT = "/data-endpoint";

    @Mock
    private RestTemplateBuilder templateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private DataProviderClient client;

    @BeforeEach
    void setUp() {
        TestDataClientConfiguration configuration = new TestDataClientConfiguration();
        configuration.setBaseUrl(BASE_URL);
        configuration.setTestDataEndpoint(DATA_ENDPOINT);

        given(templateBuilder.build()).willReturn(restTemplate);

        client = new DataProviderClient(templateBuilder, configuration);
    }

    @Test
    void whenRequestReturnsClientExceptionErrorIsThrown() {
        given(restTemplate.getForObject(BASE_URL + DATA_ENDPOINT, DataProvidedRootModel.class))
            .willThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Optional<DataProvidedRootModel> response = client.getTestData();
        assertThat(response).isNotPresent();
    }

    @Test
    void whenRequestReturnsServerExceptionErrorIsThrown() {
        given(restTemplate.getForObject(BASE_URL + DATA_ENDPOINT, DataProvidedRootModel.class))
            .willThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Optional<DataProvidedRootModel> response = client.getTestData();
        assertThat(response).isNotPresent();
    }

    @Test
    void whenRequestReturnsRestExceptionErrorIsThrown() {
        given(restTemplate.getForObject(BASE_URL + DATA_ENDPOINT, DataProvidedRootModel.class))
                .willThrow(new RestClientException("error"));

        Optional<DataProvidedRootModel> response = client.getTestData();
        assertThat(response).isNotPresent();
    }

    @Test
    void whenResponseIsEmptyOptionalEmptyIsReturned() {
        given(restTemplate.getForObject(BASE_URL + DATA_ENDPOINT, DataProvidedRootModel.class))
                .willReturn(null);

        Optional<DataProvidedRootModel> response = client.getTestData();

        assertThat(response).isNotPresent();
    }
}