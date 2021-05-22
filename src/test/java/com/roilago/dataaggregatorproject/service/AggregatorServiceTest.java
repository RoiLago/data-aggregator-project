package com.roilago.dataaggregatorproject.service;

import com.roilago.dataaggregatorproject.api.model.DataResponseErrorModel;
import com.roilago.dataaggregatorproject.client.DataProviderClient;
import com.roilago.dataaggregatorproject.service.model.DataProvidedModel;
import com.roilago.dataaggregatorproject.service.model.DataProvidedRootModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AggregatorServiceTest {

    private static final LocalDateTime BASE_DATE = LocalDateTime.of(2021, 1, 1, 0, 0);

    @Mock
    private DataProviderClient client;

    private AggregatorService aggregatorService;

    @BeforeEach
    void setUp() {
        aggregatorService = new AggregatorService(client);
    }

    @Test
    void whenClientReturnsNoData() {
        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(10);

        assertThat(response).isPresent();
        assertThat(response.get().getErrorMessage()).isEqualTo("Data endpoint returned no response");
    }

    @Test
    void whenDataSizeDoesntMatchTimeSize() {
        DataProvidedRootModel rootModel = buildDataModel(3, "metric10");
        rootModel.getAssetId660().setTime(Collections.singletonList(LocalDateTime.now()));
        given(client.getTestData()).willReturn(Optional.of(rootModel));

        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(10);

        assertThat(response).isPresent();
        assertThat(response.get().getErrorMessage()).isEqualTo("Some metric list has an unexpected size");
    }

    @Test
    void whenDataDoesntCoverAllThePeriodItsNotIncluded() {
        DataProvidedRootModel rootModel = buildDataModel(20, "metric10");
        given(client.getTestData()).willReturn(Optional.of(rootModel));

        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(10);

        assertThat(response).isPresent();
        List<OptionalDouble> metric10 = response.get().getDataResponseRootModel().getAssetId660().getMetricMap().get("metric10");
        verifyMetric(metric10, 4.5d);
    }

    @Test
    void whenMultipleMetricsArePresentInTheRequestTheyAreAlsoInTheResponse() {
        DataProvidedRootModel rootModel = buildDataModel(11, "metric10", "metric20");
        given(client.getTestData()).willReturn(Optional.of(rootModel));

        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(10);

        assertThat(response).isPresent();
        List<OptionalDouble> metric10 = response.get().getDataResponseRootModel().getAssetId660().getMetricMap().get("metric10");
        verifyMetric(metric10, 4.5d);

        List<OptionalDouble> metric20 = response.get().getDataResponseRootModel().getAssetId660().getMetricMap().get("metric20");
        verifyMetric(metric20, 4.5d);
    }

    @Test
    void whenClientResponseContainsNullValues() {
        DataProvidedRootModel rootModel = buildDataModel(11, "metric10");
        rootModel.getAssetId660().getMetricMap().get("metric10").set(9, OptionalDouble.empty());
        given(client.getTestData()).willReturn(Optional.of(rootModel));

        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(10);

        assertThat(response).isPresent();
        List<OptionalDouble> metric10 = response.get().getDataResponseRootModel().getAssetId660().getMetricMap().get("metric10");
        verifyMetric(metric10, 4d);
    }

    private DataProvidedRootModel buildDataModel(int generatedSize, String... metricNames) {
        DataProvidedModel model = new DataProvidedModel();
        for (String metricName : metricNames) {
            List<OptionalDouble> dataList = IntStream.range(0, generatedSize)
                    .mapToObj(OptionalDouble::of)
                    .collect(Collectors.toList());
            model.setMetric(metricName, dataList);
        }

        List<LocalDateTime> dateList = IntStream.range(0, generatedSize)
                .mapToObj(BASE_DATE::plusMinutes)
                .collect(Collectors.toList());
        model.getTime().addAll(dateList);

        DataProvidedRootModel rootModel = new DataProvidedRootModel();
        rootModel.setAssetId660(model);
        return rootModel;
    }

    private void verifyMetric(List<OptionalDouble> metric10, double expectedAverage) {
        assertThat(metric10).hasSize(1);
        assertThat(metric10.get(0)).isPresent();
        assertThat(metric10.get(0).getAsDouble()).isEqualTo(expectedAverage);
    }

}