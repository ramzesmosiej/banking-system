package com.client.openfeign.errors;

import com.client.openfeign.clients.BankingAppClient;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class FileUploadClientFallbackFactory implements FallbackFactory<BankingAppClient> {
    @Override
    public BankingAppClient create(Throwable cause) {
        return new BankingAppClient() {
            @Override
            public ResponseEntity<Boolean> isPINCorrect(Long cardID, String cardPIN) {
                return null;
            }

            @Override
            public ResponseEntity<String> addCashToAccount(Long cardID, Double amount, Locale lang) {
                return null;
            }

            @Override
            public ResponseEntity<String> withdrawCash(Long cardID, Double amount, Locale lang) {
                return null;
            }
        };
    }
}