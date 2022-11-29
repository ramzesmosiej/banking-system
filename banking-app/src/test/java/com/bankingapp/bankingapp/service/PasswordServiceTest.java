package com.bankingapp.bankingapp.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceTest {

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    PasswordService passwordService;

    @Test
    void hashPassword() {

        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(
                "$2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dq"
        );

        var result = passwordService.hashPassword("1234");
        assertThat(result).isEqualTo("$2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dq");

    }

}