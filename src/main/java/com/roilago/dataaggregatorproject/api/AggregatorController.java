package com.roilago.dataaggregatorproject.api;

import com.roilago.dataaggregatorproject.api.model.DataResponseErrorModel;
import com.roilago.dataaggregatorproject.api.model.PeriodModel;
import com.roilago.dataaggregatorproject.service.AggregatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/aggregator")
@Slf4j
public class AggregatorController {

    private final AggregatorService aggregatorService;

    @Autowired
    public AggregatorController(AggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @PostMapping
    public ResponseEntity<?> getAllRequestLogsByExternalReference(@RequestBody @Valid PeriodModel periodModel) {
        log.info("Received request to aggregate by minutes={}", periodModel.getPeriod());

        Optional<DataResponseErrorModel> response = aggregatorService.getByPeriod(periodModel.getPeriod());
        return buildResponse(response);
    }

    private ResponseEntity<?> buildResponse(Optional<DataResponseErrorModel> response) {
        if (response.isPresent()) {
            if (response.get().getErrorMessage() != null) {
                String errorMessage = response.get().getErrorMessage();
                log.error("Response contained an error={}", errorMessage);
                return ResponseEntity.ok(errorMessage);
            }
            log.info("Response was successful");
            return ResponseEntity.ok(response.get().getDataResponseRootModel());
        } else {
            log.error("Response was not present");
            return ResponseEntity.ok("Could not build a response");
        }
    }

}
