package com.roilago.dataaggregatorproject.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DataProvidedRootModel {

    @JsonProperty("660")
    private DataProvidedModel assetId660;

    public DataProvidedModel getAssetId660() {
        return assetId660;
    }

    public void setAssetId660(DataProvidedModel assetId660) {
        this.assetId660 = assetId660;
    }
}
