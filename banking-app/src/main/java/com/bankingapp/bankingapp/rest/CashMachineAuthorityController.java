package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/machine")
public class CashMachineAuthorityController {

    private final AccountRepository accountRepository;
    private final AccountService userAccountService;
    private final CardRepository cardRepository;


    @GetMapping("/auth/card")
    public ResponseEntity<Boolean> isPINCorrect(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN
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
            @RequestHeader(name = "lang", required = false) Locale locale
    ) {
        var ownerAccount = cardRepository.findById(cardID).orElseThrow();
        // transactional method
        return ResponseEntity.ok(userAccountService.addCashToAccount(ownerAccount.getAccount().getId(), amount, locale));
    }

    /*@PostMapping("/withdraw/cash")
    public ResponseEntity<?> withdrawCash(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "lang", required = false) Locale locale
    ){
        User accountOwner = userRepository.findUserByCard(cardID);
        // transactional method
        return ResponseEntity.ok(userAccountService.takeCashFromAccount(accountOwner.getId(), amount, locale));
    }*/

    @PutMapping("/transfer/money")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest)
            throws InterruptedException {
        return ResponseEntity.ok(userAccountService.transferMoney(
                transferRequest.getSenderId(),
                transferRequest.getReceiverId(),
                transferRequest.getAmount()
        ));
    }
}
