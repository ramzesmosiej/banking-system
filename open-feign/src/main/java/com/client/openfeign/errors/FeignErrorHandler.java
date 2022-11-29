package com.client.openfeign.errors;

import com.client.openfeign.clients.BankingAppClient;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public class FeignErrorHandler implements FallbackFactory<BankingAppClient> {
    @Override
    public BankingAppClient create(Throwable cause) {

        String httpStatus = cause instanceof FeignException ? Integer.toString(((FeignException) cause).status()) : "";

        return new BankingAppClient() {

            @Override
            public ResponseEntity<Boolean> isPINCorrect(Long cardID, String cardPIN) {
                return ResponseEntity.status(666).body(false);
            }

            @Override
            public ResponseEntity<String> addCashToAccount(Long cardID, Double amount, Locale lang) {
                return ResponseEntity.status(666).body("abc");
            }

            @Override
            public ResponseEntity<String> withdrawCash(Long cardID, Double amount, Locale lang) {
                return ResponseEntity.status(666).body("def");
            }
        };
    }
}
