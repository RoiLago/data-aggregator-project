package com.roilago.dataaggregatorproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DataAggregatorProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataAggregatorProjectApplication.class, args);
    }

}
