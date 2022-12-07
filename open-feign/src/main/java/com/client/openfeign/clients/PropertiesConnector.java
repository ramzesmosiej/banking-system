package com.client.openfeign.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertiesConnector {

    @Value("${banking-app.name}")
    public static final String BANK_APP_NAME = "banking-app";

    @Value("${banking-app.address}")
    public static final String BANK_APP_ADDRESS = "http://localhost:8080";

}
