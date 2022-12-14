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
        )
            return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (card.getPIN().equals(loggingData[2]))
                kafkaTemplate.send(kafkaTopicConfig.getReceivePinAnswear(), "OK");
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
