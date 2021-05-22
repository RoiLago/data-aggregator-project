package com.roilago.dataaggregatorproject.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DataResponseRootModel {

    @JsonProperty("660")
    private DataResponseModel assetId660 = new DataResponseModel();

    public DataResponseModel getAssetId660() {
        return assetId660;
    }

    public void setAssetId660(DataResponseModel assetId660) {
        this.assetId660 = assetId660;
    }
}
