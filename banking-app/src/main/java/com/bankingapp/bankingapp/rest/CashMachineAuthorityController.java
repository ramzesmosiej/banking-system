package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/machine")
public class CashMachineAuthorityController {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    private final UserAccountService userAccountService;

    public CashMachineAuthorityController(CardRepository cardRepository, UserRepository userRepository, UserAccountService userAccountService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.userAccountService = userAccountService;
    }

    @GetMapping("/auth/card")
    public ResponseEntity<Boolean> isPINCorrect(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN
    ) {
        Optional<Card> optionalCard = cardRepository.findById(cardID);
        if (optionalCard.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(cardPIN.equals(optionalCard.get().getPIN()));
    }

    @PostMapping("/add/cash")
    public ResponseEntity<?> addCashToAccount(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader Locale lang
    ) {
        User accountOwner = userRepository.findUserByCard(cardID);
        // transactional method
        return ResponseEntity.ok(userAccountService.addCashToUser(accountOwner.getId(), amount, lang));
    }

    @PostMapping("/withdraw/cash")
    public ResponseEntity<?> withdrawCash(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader Locale lang
    ) {
        User accountOwner = userRepository.findUserByCard(cardID);
        // transactional method
        return ResponseEntity.ok(userAccountService.takeCashFromAccount(accountOwner.getId(), amount, lang));
    }
}
