package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.DTO.MoneyTransferRequest;
import com.bankingapp.bankingapp.config.KafkaTopicConfig;
import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.service.AccountService;
import com.bankingapp.bankingapp.service.AuthService;
import com.bankingapp.bankingapp.service.PropertiesCashMachineIdsConnector;
import com.bankingapp.bankingapp.service.PropertiesLanguageConnector;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    private final PropertiesCashMachineIdsConnector propertiesCashMachineIdsConnector;
    private final PropertiesLanguageConnector propertiesLanguageConnector;


    @KafkaListener(
            topics = "${account.cashmachine.pin.send}",
            groupId = "logging"
    )
    public void logInto(@Payload String credentials) {
        var loggingData = credentials.split(";");

        // check cashmachine
        var cashMachineId = loggingData[0];
        if (
                !cashMachineId.equals(propertiesCashMachineIdsConnector.getDominikanski()) &&
                !cashMachineId.equals(propertiesCashMachineIdsConnector.getDworzec_glowny()) &&
                !cashMachineId.equals(propertiesCashMachineIdsConnector.getGrunwaldzki())
        ) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (card.getPIN().equals(loggingData[2]))
                kafkaTemplate.send(kafkaTopicConfig.getReceivePinAnswear(), "OK");
        }
    }

    @KafkaListener(
            topics = "${account.cashmachine.payment.send}",
            groupId = "payment"
    )
    public void makeAPayment(@Payload String credentials) {
        var loggingData = credentials.split(";");

        // check cashmachine
        var cashMachineId = loggingData[0];
        if (
                !cashMachineId.equals(propertiesCashMachineIdsConnector.getDominikanski()) &&
                        !cashMachineId.equals(propertiesCashMachineIdsConnector.getDworzec_glowny()) &&
                        !cashMachineId.equals(propertiesCashMachineIdsConnector.getGrunwaldzki())
        ) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            var account = accountRepository.findById(card.getAccount().getId());

            if (account.isEmpty())
                return;

            var msg = accountService.addCashToAccount(
                    account.get().getId(),
                    Double.parseDouble(loggingData[2]),
                    Locale.forLanguageTag(loggingData[3]));
            kafkaTemplate.send(kafkaTopicConfig.getPaymentReceive(), msg);
        }
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
