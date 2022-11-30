package com.client.openfeign.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
        case 400:
            return new IllegalArgumentException("Bad Request");
        case 404:
            return new IllegalStateException("Not found");
        default:
            return errorDecoder.decode(methodKey, response);
        }
    }
}