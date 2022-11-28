package com.client.openfeign.clients;

import com.client.openfeign.dto.CashOperationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@FeignClient(name = "banking-app", url = "http://localhost:8081")
public interface BankingAppClient {

    @GetMapping("/machine/auth/card")
    ResponseEntity<Boolean> isPINCorrect(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN
    );

    @PostMapping("/machine/add/cash")
    ResponseEntity<String> addCashToAccount(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "lang") Locale lang
    );

    @PostMapping("/withdraw/cash")
    ResponseEntity<String> withdrawCash(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader Locale lang
    );

}
