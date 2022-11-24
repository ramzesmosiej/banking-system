package com.bankingapp.bankingapp.service;

import lombok.AllArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@AllArgsConstructor
public class PropertiesLanguageConnector {
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    String getMessageOnLanguage(String code, Locale locale) {
        return resourceBundleMessageSource.getMessage(code, null, locale);
    }

}
