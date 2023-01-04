package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.DTO.LoginResponse;
import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;


    public String loginIntoSystem(String login, String password) {
        var token = new UsernamePasswordAuthenticationToken(login, password);
        authenticationManager.authenticate(token);
        String jwtToken = jwtUtil.generateAccessToken(login);
        logger.info("Generate JWT token for user :" + login);
        return jwtToken;
    }

    public User registerUser(RegistrationRequest inputUser) {
        return userService.registerUser(inputUser);
    }

}
