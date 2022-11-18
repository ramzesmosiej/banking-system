package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.CashOperationRequest;
import com.bankingapp.bankingapp.service.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
@RequestMapping("api/operations")
@Validated
public class AccountOperationController {

    private final UserAccountService userAccountService;

    @PostMapping(value = "/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> payment(@RequestBody @Valid CashOperationRequest cashOperationRequest) {
        return ResponseEntity.ok(userAccountService.addCashToUser(
                cashOperationRequest.getUserId(),
                cashOperationRequest.getCash())
        );
    }

    @PostMapping(value = "/paycheck", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> paycheck(@RequestBody @Valid CashOperationRequest cashOperationRequest) {
        return ResponseEntity.ok(userAccountService.takeCashFromAccount(
                cashOperationRequest.getUserId(),
                cashOperationRequest.getCash())
        );
    }

}
