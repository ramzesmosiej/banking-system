package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.config.KafkaTopicConfig;
import com.bankingapp.bankingapp.domain.CashMachineList;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class CashMachineService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final CardRepository cardRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final Logger logger = LoggerFactory.getLogger(CashMachineService.class);
    private final PropertiesCashMachineIdsConnector propertiesCashMachineIdsConnector;

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
        var cashMachineLocalization = isCashMachineNumberValid(cashMachineId);
        if (cashMachineLocalization == null) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (Boolean.TRUE.equals(card.getIsActive()) && card.getPIN().equals(loggingData[2])) {
                kafkaTemplate.send(kafkaTopicConfig.getReceivePinAnswear(), "OK");
                logger.info("Cash machine {}: Card with id {} logged into.", cashMachineLocalization, cardId);
            }
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
        var cashMachineLocalization = isCashMachineNumberValid(cashMachineId);
        if (cashMachineLocalization == null) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (Boolean.FALSE.equals(card.getIsActive())) return;

            var account = accountRepository.findById(card.getAccount().getId());
            if (account.isEmpty()) return;

            var msg = accountService.addCashToAccount(
                    account.get().getId(),
                    Double.parseDouble(loggingData[2]),
                    Locale.forLanguageTag(loggingData[3]));
            kafkaTemplate.send(kafkaTopicConfig.getPaymentReceive(), msg);
            logger.info("Cash machine: {} Card with id: {} makes a payment {}", cashMachineLocalization, cardId, loggingData[2]);
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
        var cashMachineLocalization = isCashMachineNumberValid(cashMachineId);
        if (cashMachineLocalization == null) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (Boolean.FALSE.equals(card.getIsActive())) return;

            var account = accountRepository.findById(card.getAccount().getId());
            if (account.isEmpty()) return;

            var msg = accountService.takeCashFromAccount(
                    account.get().getId(),
                    Double.parseDouble(loggingData[2]),
                    Locale.forLanguageTag(loggingData[3]));
            kafkaTemplate.send(kafkaTopicConfig.getWithdrawReceive(), msg);
            logger.info("Cash machine: {} Card with id: {} makes a withdraw {}", cashMachineLocalization, cardId, loggingData[2]);
        }
    }

    /***
     * Withdraw method
     * @param credentials
     */
    @KafkaListener(
            topics = "${account.cashmachine.show.send}",
            groupId = "show"
    )
    public void showMoney(@Payload String credentials) {
        var loggingData = credentials.split(";");

        // check cashmachine
        var cashMachineId = loggingData[0];
        var cashMachineLocalization = isCashMachineNumberValid(cashMachineId);
        if (cashMachineLocalization == null) return;

        // check card
        var cardId = Long.parseLong(loggingData[1]);
        var optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isPresent()) {
            var card = optionalCard.get();
            if (Boolean.FALSE.equals(card.getIsActive())) return;

            var account = accountRepository.findById(card.getAccount().getId());
            if (account.isEmpty()) return;

            var msg = accountService.showMoney(account.get().getId(), Locale.forLanguageTag(loggingData[2]));
            kafkaTemplate.send(kafkaTopicConfig.getShowReceive(), msg);
            logger.info("Cash machine {}: Card with id {} checks its account.", cashMachineLocalization, cardId);
        }
    }

    // helper methods
    private String isCashMachineNumberValid(String cashMachineNumber) {
        if (cashMachineNumber.equals(propertiesCashMachineIdsConnector.getDominikanski()))
            return CashMachineList.PLAC_DOMINIKANSKI;
        else if (cashMachineNumber.equals(propertiesCashMachineIdsConnector.getDworzec_glowny()))
            return CashMachineList.DWORZEC_GLOWNY;
        else if (cashMachineNumber.equals(propertiesCashMachineIdsConnector.getGrunwaldzki()))
            return CashMachineList.PLAC_GRUNWALDZKI;
        else
            return null;
    }

}
