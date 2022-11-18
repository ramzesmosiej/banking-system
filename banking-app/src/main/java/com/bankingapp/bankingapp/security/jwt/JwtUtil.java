package com.bankingapp.bankingapp.security.jwt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;


@Service
public class JwtUtil {

    private final String jwtSecret = "CctlD5JL16m8wLTgsFNhzqjQP";

    private final UserRepository userRepository;

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public String generateAccessToken(String login) {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret.getBytes());

        String claims = userRepository.findUserByLogin(login).get().getGrantedAuthorities().stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.joining(","));

        System.out.println(claims);

        return JWT.create()
                .withSubject(login)
                .withClaim("auth", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
                .sign(algorithm);
    }

    public boolean validate(String token) {
        try {
            long expiresAt = JWT.decode(token).getExpiresAt().getTime();
            Calendar cal = Calendar.getInstance();
            if (expiresAt >= cal.getTime().getTime()) {
                return true;
            }
        } catch (IllegalArgumentException e) {
            System.out.printf("JWT is invalid - {%s}%n", e.getMessage());
        }

        return false;
    }

    public String getLogin(String token) {
        return JWT.decode(token).getSubject().trim();
    }

}