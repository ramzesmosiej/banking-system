package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        when(userService.registerUser(any())).thenReturn(VALID_USER());
        var registeredUser = authService.registerUser(VALID_USER_REQUEST());

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getLastName()).isEqualTo("Nowak");
    }

    @Test
    void sendPingToAdmin() {
        assertThat(authService.sendPingToAdmin()).isEqualTo("Hello from admin");
    }

    @Test
    void sendPing() {
        assertThat(authService.sendPing()).isEqualTo("Hello from secured request");
    }

    private RegistrationRequest VALID_USER_REQUEST() {
        var requestUser = new RegistrationRequest();
        requestUser.setFirstName("Jan");
        requestUser.setLastName("Nowak");
        requestUser.setLogin("janN");
        requestUser.setEmail("jan.nowak@gmail.com");
        requestUser.setPassword("3214");
        return requestUser;
    }

    private User VALID_USER() {
        return User.builder()
                .firstName("Jan")
                .lastName("Nowak")
                .login("janN")
                .email("jan.nowak@gmail.com")
                .build();
    }

}
