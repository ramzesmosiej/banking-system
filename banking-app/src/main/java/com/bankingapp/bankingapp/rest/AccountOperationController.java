package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.CashOperationRequest;
import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@AllArgsConstructor
@Controller
@RequestMapping("/api/operations")
@Validated
public class AccountOperationController {

    private final AccountService accountService;


    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getUser(userId));
    }

    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> payment(
            @RequestHeader(name = "lang", required = false) Locale locale,
            @RequestBody @Valid CashOperationRequest cashOperationRequest
    ) {
        return ResponseEntity.ok(accountService.addCashToAccount(
                cashOperationRequest.getAccountId(),
                cashOperationRequest.getCash(),
                locale)
        );
    }

    @PostMapping(value = "/paycheck", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> paycheck(
            @RequestHeader(name = "lang", required = false) Locale locale,
            @RequestBody @Valid CashOperationRequest cashOperationRequest
    ) {
        return ResponseEntity.ok(accountService.takeCashFromAccount(
                cashOperationRequest.getAccountId(),
                cashOperationRequest.getCash(),
                locale)
        );
    }

    @PutMapping("/transfer/money")
    public ResponseEntity<String> transferMoney(@RequestBody @Valid MoneyTransferRequest transferRequest)
            throws InterruptedException {
        return ResponseEntity.ok(accountService.transferMoney(
                transferRequest.getSenderId(),
                transferRequest.getReceiverId(),
                transferRequest.getAmount()
        ));
    }

}
