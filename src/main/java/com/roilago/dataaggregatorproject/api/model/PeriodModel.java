package com.roilago.dataaggregatorproject.api.model;

import javax.validation.constraints.NotNull;

public class PeriodModel {

    @NotNull
    private Integer period;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
