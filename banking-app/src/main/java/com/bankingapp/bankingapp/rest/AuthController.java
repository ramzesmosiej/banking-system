package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.LoginRequest;
import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;

@AllArgsConstructor
@Controller
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest inputUser) throws URISyntaxException {
        User savedUser = authService.registerUser(inputUser);
        return ResponseEntity.created(new URI("/api/operations/" + savedUser.getId())).body("New user with id: " + savedUser.getId() + " and login: " + savedUser.getLogin() + " registered.");
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginIntoSystem(loginRequest.getLogin(), loginRequest.getPassword()));
    }

    @GetMapping("/ping/admin")
    public ResponseEntity<String> pingAdmin() {
        return ResponseEntity.ok(authService.sendPingToAdmin());
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok(authService.sendPing());
    }


}
