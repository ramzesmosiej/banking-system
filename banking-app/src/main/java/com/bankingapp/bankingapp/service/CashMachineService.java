package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.config.KafkaTopicConfig;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class CashMachineService {

    private final AccountService accountService;
    private final CardRepository cardRepository;

    private final AccountRepository accountRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    private final PropertiesCashMachineIdsConnector propertiesCashMachineIdsConnector;
    private final PropertiesLanguageConnector propertiesLanguageConnector;

    /***
     * Logging method
     * @param credentials
     */
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

    /***
     * Payment method
     * @param credentials
     */
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

    /***
     * Withdraw method
     * @param credentials
     */
    @KafkaListener(
            topics = "${account.cashmachine.withdraw.send}",
            groupId = "withdraw"
    )
    public void makeAWithdraw(@Payload String credentials) {
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

            var msg = accountService.takeCashFromAccount(
                    account.get().getId(),
                    Double.parseDouble(loggingData[2]),
                    Locale.forLanguageTag(loggingData[3]));
            kafkaTemplate.send(kafkaTopicConfig.getWithdrawReceive(), msg);
        }
    }

}
