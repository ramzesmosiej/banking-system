package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.*;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.service.AccountService;
import com.bankingapp.bankingapp.service.AuthService;
import com.bankingapp.bankingapp.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@AllArgsConstructor
@Controller
@CrossOrigin("http://localhost:4200/")
@RequestMapping("api/auth")
@Validated
public class AuthController {

    private final AccountService accountService;
    private final AuthService authService;
    private final CardService cardService;

    @PostMapping("/changepin")
    public ResponseEntity<OperationResponse> changePin(@RequestBody @Valid ChangePinRequest changePinRequest) {
        return ResponseEntity.ok(new OperationResponse(
                cardService.changePIN(changePinRequest.getCardID(), changePinRequest.getNewPin())
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var token = authService.loginIntoSystem(loginRequest.getLogin(), loginRequest.getPassword());
        response.addCookie(new Cookie("token", token));
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> pingMainSystem() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegistrationRequest inputUser) throws URISyntaxException {
        User savedUser = authService.registerUser(inputUser);
        return ResponseEntity.created(new URI("/api/operations/" + savedUser.getId())).body("New user with id: "
                + savedUser.getId() + " and login: " + savedUser.getLogin() + " registered.");
    }

    @PostMapping("/register-with-account")
    public ResponseEntity<String> registerUserWithAccount(@RequestBody @Valid RegistrationRequest inputUser)
            throws URISyntaxException {

        var user = authService.registerUser(inputUser);
        var card = cardService.createCard("0000");
        var account = accountService.createAccountToUser(user.getId(), card.getId());

        return ResponseEntity.created(new URI("/api/operations/" + user.getId())).body("New user with id: "
                + user.getId() + " and login: " + user.getLogin() + " registered. New card is created with id: " +
                card.getId() + " and account with id: " + account.getId());
    }

}
