package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static com.bankingapp.bankingapp.TestConsts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginIntoSystem() {
        var token = authService.loginIntoSystem("login", "password");
        System.out.println(token);
    }

    @Test
    void registerUser() {
        when(userService.registerUser(any())).thenReturn(VALID_USER);
        var registeredUser = authService.registerUser(VALID_USER_REQUEST());

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getLastName()).isEqualTo("Nowak");
    }

}
