package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.LoginRequest;
import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import com.bankingapp.bankingapp.service.AuthService;
import com.bankingapp.bankingapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequest inputUser) {
        return ResponseEntity.ok(authService.registerUser(inputUser));
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
