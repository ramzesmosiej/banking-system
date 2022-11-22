package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.LoginRequest;
import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.security.jwt.JwtUtil;
import com.bankingapp.bankingapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegistrationRequest inputUser) {
        return ResponseEntity.ok(userService.registerUser(inputUser));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getLogin(), loginRequest.getPassword()
        );
        authenticationManager.authenticate(token);
        String jwtToken = jwtUtil.generateAccessToken(loginRequest.getLogin());
        System.out.println(jwtUtil.parseClaims(jwtToken));
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/ping/admin")
    public ResponseEntity<String> pingAdmin() {
        return ResponseEntity.ok("Hello from admin");
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Hello from secured request");
    }


}
