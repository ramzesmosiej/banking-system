package com.bankingapp.bankingapp.service;

import lombok.AllArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class PropertiesLanguageConnector {
    private final ResourceBundleMessageSource resourceBundleMessageSource;

    public String getMessageOnLanguage(String code, Locale locale) {
        return resourceBundleMessageSource.getMessage(code, null, locale);
    }

}
