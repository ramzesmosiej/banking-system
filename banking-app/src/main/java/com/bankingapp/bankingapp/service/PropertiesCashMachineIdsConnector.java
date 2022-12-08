package com.bankingapp.bankingapp.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "cash.machine")
@Configuration
@Getter
@Setter
public class PropertiesCashMachineIdsConnector {
    private String grunwaldzki;
    private String dominikanski;
    private String dworzec_glowny;
}
