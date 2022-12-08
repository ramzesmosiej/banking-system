package com.client.openfeign.clients;

import com.client.openfeign.errors.FeignSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

import static com.client.openfeign.clients.PropertiesConnector.BANK_APP_ADDRESS;
import static com.client.openfeign.clients.PropertiesConnector.BANK_APP_NAME;

@FeignClient(name = BANK_APP_NAME, url = BANK_APP_ADDRESS, configuration = FeignSupportConfig.class)
public interface BankingAppClient {

    @GetMapping("/machine/auth/card")
    ResponseEntity<Boolean> isPINCorrect(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "cardPIN") String cardPIN,
            @RequestHeader(name = "cash-machine") String auth
    );

    @PostMapping("/machine/add/cash")
    ResponseEntity<String> addCashToAccount(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "cash-machine") String auth,
            @RequestHeader(name = "lang") Locale lang
    );

    @PostMapping("/machine/withdraw/cash")
    ResponseEntity<String> withdrawCash(
            @RequestParam(name = "cardID") Long cardID,
            @RequestParam(name = "amount") Double amount,
            @RequestHeader(name = "cash-machine") String auth,
            @RequestHeader(name = "lang") Locale lang
    );

}
