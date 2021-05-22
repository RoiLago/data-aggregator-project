package com.roilago.dataaggregatorproject.api.model;

import java.io.Serializable;

public class ErrorResource implements Serializable {

    private static final long serialVersionUID = -5461279964892856133L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                '}';
    }
}
