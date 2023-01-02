package com.example.mainsystemconnector.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/connection")
public class ConnectionController {

    @GetMapping
    public void pingMainSystem() {

    }

}
