package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public String loginIntoSystem(String login, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
        authenticationManager.authenticate(token);
        String jwtToken = jwtUtil.generateAccessToken(login);
        System.out.println(jwtUtil.parseClaims(jwtToken));
        return jwtToken;
    }

    public User registerUser(RegistrationRequest inputUser) {
        return userService.registerUser(inputUser);
    }

    public String sendPingToAdmin() {
        return "Hello from admin";
    }

    public String sendPing() {
        return "Hello from secured request";
    }

}
