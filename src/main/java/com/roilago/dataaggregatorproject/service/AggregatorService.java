package com.roilago.dataaggregatorproject.service;

import com.roilago.dataaggregatorproject.api.model.DataResponseErrorModel;
import com.roilago.dataaggregatorproject.api.model.DataResponseModel;
import com.roilago.dataaggregatorproject.api.model.DataResponseRootModel;
import com.roilago.dataaggregatorproject.client.DataProviderClient;
import com.roilago.dataaggregatorproject.service.model.DataProvidedModel;
import com.roilago.dataaggregatorproject.service.model.DataProvidedRootModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
@Slf4j
public class AggregatorService {

    private final DataProviderClient client;

    @Autowired
    public AggregatorService(DataProviderClient client) {
        this.client = client;
    }

    public Optional<DataResponseErrorModel> getByPeriod(int period) {
        Optional<DataProvidedRootModel> providedJson = client.getTestData();
        if (!providedJson.isPresent()) {
            log.error("Response from test data client is not present");
            return Optional.of(new DataResponseErrorModel("Data endpoint returned no response"));
        }

        DataProvidedModel dataProvided = providedJson.get().getAssetId660();
        if (dataSizesUnequal(dataProvided)) {
            log.error("Some metric doesn't match the expected size from the local date time list");
            return Optional.of(new DataResponseErrorModel("Some metric list has an unexpected size"));
        }

        DataResponseErrorModel responseDataModel = replicateDataModel(dataProvided);

        LocalDateTime finalExpectedDate = dataProvided.getTime().get(0).plusMinutes(period);
        int initialIndex = 0;

        List<LocalDateTime> timeList = dataProvided.getTime();
        for (int i = 0; i < timeList.size(); i++) {

            LocalDateTime dateTime = timeList.get(i);
            if (dateTime.compareTo(finalExpectedDate) >= 0) {
                calculateMetrics(dataProvided, initialIndex, i - initialIndex, responseDataModel);
                initialIndex = i;
                finalExpectedDate = dateTime.plusMinutes(period);
            }
        }

        log.info("Response={}", responseDataModel);

        return Optional.of(responseDataModel);
    }

    private boolean dataSizesUnequal(DataProvidedModel dataProvided) {
        return dataProvided.getMetricMap().entrySet()
                .parallelStream()
                .anyMatch(entry -> entry.getValue().size() != dataProvided.getTime().size());
    }

    private DataResponseErrorModel replicateDataModel(DataProvidedModel assetId660) {
        DataResponseRootModel responseDataModel = new DataResponseRootModel();
        for (String metricNames : assetId660.getMetricMap().keySet()) {
            responseDataModel.getAssetId660().setMetric(metricNames, new ArrayList<>());
        }
        return new DataResponseErrorModel(responseDataModel);
    }

    private void calculateMetrics(DataProvidedModel dataProvided, int startIndex, int dataSize, DataResponseErrorModel responseRootData) {
        DataResponseModel responseData = responseRootData.getDataResponseRootModel().getAssetId660();
        responseData.getTime().add(dataProvided.getTime().get(startIndex));

        for (Map.Entry<String, List<OptionalDouble>> metricSet : dataProvided.getMetricMap().entrySet()) {

            OptionalDouble average = metricSet.getValue()
                    .subList(startIndex, startIndex + dataSize)
                    .parallelStream()
                    .filter(OptionalDouble::isPresent)
                    .mapToDouble(OptionalDouble::getAsDouble)
                    .average();
            responseData.getMetricMap().get(metricSet.getKey()).add(average);

            log.debug("Calculated average of {} values starting from index={} for metric={}", dataSize, startIndex, metricSet.getKey());
        }
    }

}
