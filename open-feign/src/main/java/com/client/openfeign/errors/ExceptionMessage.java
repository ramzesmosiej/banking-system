package com.client.openfeign.errors;

import lombok.Getter;

import java.util.List;

@Getter
public class ExceptionMessage {
    private String timestamp;
    private String status;
    private List<String> errors;
    private String message;
    private String path;

    @Override
    public String toString() {
        return "ExceptionMessage [timestamp=" + timestamp + ", status=" + status + ", error=" + errors + ", message=" + message + ", path=" + path + "]";
    }

}
