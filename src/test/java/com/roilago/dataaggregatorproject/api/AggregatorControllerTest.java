package com.roilago.dataaggregatorproject.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roilago.dataaggregatorproject.api.model.DataResponseErrorModel;
import com.roilago.dataaggregatorproject.api.model.DataResponseRootModel;
import com.roilago.dataaggregatorproject.api.model.PeriodModel;
import com.roilago.dataaggregatorproject.service.AggregatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.OptionalDouble;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AggregatorControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private AggregatorService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AggregatorController controller = new AggregatorController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void whenRequestIsCorrectButResponseIsNotPresentErrorMessageIsReturned() throws Exception {
        PeriodModel model = buildPeriodModel();
        mockMvc.perform(post("/aggregator")
                .content(mapper.writeValueAsString(model))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Could not build a response")));
    }

    @Test
    void whenRequestIsCorrectAndResponseIsCorrect() throws Exception {
        PeriodModel model = buildPeriodModel();

        DataResponseRootModel responseModel = new DataResponseRootModel();
        responseModel.getAssetId660().setTime(Collections.singletonList(LocalDateTime.of(2021, 1, 1, 0, 0)));
        responseModel.getAssetId660().setMetric("metric", Collections.singletonList(OptionalDouble.of(1d)));
        BDDMockito.given(service.getByPeriod(model.getPeriod())).willReturn(Optional.of(new DataResponseErrorModel(responseModel)));

        mockMvc.perform(post("/aggregator")
                .content(mapper.writeValueAsString(model))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$['660']['time']", hasSize(1)))
                .andExpect(jsonPath("$['660']['metric']", hasSize(1)));
    }

    @Test
    void whenRequestIsCorrectAndResponseContainsAnError() throws Exception {
        PeriodModel model = buildPeriodModel();

        BDDMockito.given(service.getByPeriod(model.getPeriod())).willReturn(Optional.of(new DataResponseErrorModel("Error message")));

        mockMvc.perform(post("/aggregator")
                .content(mapper.writeValueAsString(model))
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Error message")));
    }

    @Test
    void whenRequestIsIncorrect() throws Exception {
        mockMvc.perform(post("/aggregator")
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    private PeriodModel buildPeriodModel() {
        PeriodModel model = new PeriodModel();
        model.setPeriod(10);
        return model;
    }
}