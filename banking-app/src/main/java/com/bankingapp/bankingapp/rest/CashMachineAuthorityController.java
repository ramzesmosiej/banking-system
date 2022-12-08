package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.service.AccountService;
import com.bankingapp.bankingapp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@AllArgsConstructor
@RestController
@RequestMapping("/machine")
public class CashMachineAuthorityController {

    private final AuthService authService;
    private final AccountService accountService;
    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;


    @GetMapping("/auth/login")
    public ResponseEntity<String> checkCard(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN
    ) {
        var optionalCard = cardRepository.findById(cardID);

        if (optionalCard.isEmpty() || !cardPIN.equals(optionalCard.get().getPIN()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        else {
            var user = optionalCard.get().getAccount().getUser();
            return ResponseEntity.ok(authService.loginIntoSystem(cardID.toString(), cardPIN));
        }
    }

    @GetMapping("/auth/card")
    public ResponseEntity<Boolean> isPINCorrect(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN,
            @RequestHeader(name = "cash-machine") String auth
    ) {
        var optionalCard = cardRepository.findById(cardID);

        if (optionalCard.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(cardPIN.equals(optionalCard.get().getPIN()));
    }

    @PostMapping("/add/cash")
    public ResponseEntity<?> addCashToAccount(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "cash-machine") String auth,
            @RequestHeader(name = "lang", required = false) Locale locale
    ) {
        var ownerAccount = cardRepository.findById(cardID).orElseThrow();
        // transactional method
        return ResponseEntity.ok(accountService.addCashToAccount(ownerAccount.getAccount().getId(), amount, locale));
    }

    @PostMapping("/withdraw/cash")
    public ResponseEntity<?> withdrawCash(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "cash-machine") String auth,
            @RequestHeader(name = "lang", required = false) Locale locale
    ){
        var ownerAccount = cardRepository.findById(cardID).orElseThrow();
        // transactional method
        return ResponseEntity.ok(accountService.takeCashFromAccount(ownerAccount.getAccount().getId(), amount, locale));
    }

    @PutMapping("/transfer/money")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest)
            throws InterruptedException {
        return ResponseEntity.ok(accountService.transferMoney(
                transferRequest.getSenderId(),
                transferRequest.getReceiverId(),
                transferRequest.getAmount()
        ));
    }
}
