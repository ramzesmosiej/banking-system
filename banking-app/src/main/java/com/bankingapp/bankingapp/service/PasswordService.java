package com.bankingapp.bankingapp.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder;

    public String hashPassword(String raw) {
        return encoder.encode(raw);
    }

}
