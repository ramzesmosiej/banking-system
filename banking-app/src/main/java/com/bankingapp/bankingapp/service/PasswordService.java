package com.bankingapp.bankingapp.service;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder;


    public PasswordService(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }


    public String hashPassword(String raw) {
        return encoder.encode(raw);
    }
}
