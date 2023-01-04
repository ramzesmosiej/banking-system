package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.domain.Account;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/api/userthings")
@Validated
public class UserThingsController {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @CrossOrigin
    @GetMapping("/{login}")
    public ResponseEntity<List<Account>> userAccounts(@PathVariable String login) {
        var user = userRepository.findUserByLogin(login).orElseThrow(
                () -> new UserNotFoundException("")
        );
        var accounts = user.getAccounts().stream().toList();
        return ResponseEntity.ok(accounts);
    }

}
