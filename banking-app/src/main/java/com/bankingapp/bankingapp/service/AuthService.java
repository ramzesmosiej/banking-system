package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public String loginIntoSystem(String login, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
        authenticationManager.authenticate(token);
        String jwtToken = jwtUtil.generateAccessToken(login);
        System.out.println(jwtUtil.parseClaims(jwtToken));
        return jwtToken;
    }

}
