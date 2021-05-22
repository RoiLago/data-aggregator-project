package com.roilago.dataaggregatorproject.api.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DataResponseErrorModel {

    private String errorMessage;

    private DataResponseRootModel dataResponseRootModel;

    public DataResponseErrorModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DataResponseErrorModel(DataResponseRootModel dataResponseModel) {
        this.dataResponseRootModel = dataResponseModel;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DataResponseRootModel getDataResponseRootModel() {
        return dataResponseRootModel;
    }

    public void setDataResponseRootModel(DataResponseRootModel dataResponseRootModel) {
        this.dataResponseRootModel = dataResponseRootModel;
    }
}
