package com.roilago.dataaggregatorproject.service.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@ToString
@EqualsAndHashCode
public class DataProvidedModel {

    private List<LocalDateTime> time = new ArrayList<>();
    private final Map<String, List<OptionalDouble>> unknownMetrics = new HashMap<>();

    public List<LocalDateTime> getTime() {
        return time;
    }

    public void setTime(List<LocalDateTime> time) {
        this.time = time;
    }

    @JsonAnyGetter
    public Map<String, List<OptionalDouble>> getMetricMap() {
        return unknownMetrics;
    }

    @JsonAnySetter
    public void setMetric(String name, List<OptionalDouble> value) {
        unknownMetrics.put(name, value);
    }
}
