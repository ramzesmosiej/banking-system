package com.client.openfeign.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class RetreiveMessageErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        ExceptionMessage message;

        if (response == null || response.body() == null)
            return new IllegalStateException("Not found");

        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException(message.getMessage() != null ? message.getMessage() : "Bad Request");
            case 404 -> new IllegalStateException(message.getMessage() != null ? message.getMessage() : "Not found");
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}