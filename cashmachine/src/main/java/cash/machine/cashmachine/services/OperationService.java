package cash.machine.cashmachine.services;

import cash.machine.cashmachine.config.KafkaTopicConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class OperationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final Logger logger = LoggerFactory.getLogger(OperationService.class);
    private final PropertiesConnector propertiesConnector;

    /***
     * variables that take care about the user credentials
     */
    private Long actualCard;
    private Boolean isLoggedIn;
    private String systemMsg;

    public OperationService(
            KafkaTemplate<String, String> kafkaTemplate,
            KafkaTopicConfig kafkaTopicConfig,
            PropertiesConnector propertiesConnector
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicConfig = kafkaTopicConfig;
        this.propertiesConnector = propertiesConnector;
        actualCard = 0L;
        this.isLoggedIn = false;
        this.systemMsg = "";
    }

    /***
     * Logging method to cash machine
     * @param pinStatus - If the pin is correct this value equals OK
     */
    @Async
    @KafkaListener(
            topics = "${account.cashmachine.pin.receive}",
            groupId = "logging"
    )
    public void logging(@Payload String pinStatus) {
        if(pinStatus.equals("OK")) isLoggedIn = true;
    }

    public String logInto(Long cardID, String cardPIN, Locale lang) {
        kafkaTemplate.send(kafkaTopicConfig.getSendPin(), propertiesConnector.getId() + ";" + cardID + ";" + cardPIN);

        communicationWithBank();

        if(Boolean.TRUE.equals(isLoggedIn)) {
            logger.info(String.format("Card with id %s logged into.", cardID));
            this.actualCard = cardID;
            return "OK";
        }

        else {
            logger.info(String.format("Card with id %s didn't log into.", cardID));
            return "AUTH_ERROR";
        }
    }

    /***
     * Payment method
     * @param systemMsg
     */
    @Async
    @KafkaListener(
            topics = "${account.cashmachine.payment.receive}",
            groupId = "payment"
    )
    public void paymentListening(@Payload String systemMsg) {
        if(!systemMsg.isEmpty()) this.systemMsg = systemMsg;
    }

    public String makeAPayment(Long cardId, Double amountOfMoney, Locale lang) {
        if (Boolean.TRUE.equals(isLoggedIn)) {
            kafkaTemplate.send(
                    kafkaTopicConfig.getPaymentSend(),
                    propertiesConnector.getId() + ";" + cardId + ";" + amountOfMoney + ";" + lang
            );

            communicationWithBank();
            isLoggedIn = false;

            if(this.systemMsg.isEmpty())
                return "";
            else
                return systemMsg;
        }
        else return "";
    }

    /***
     * Withdraw method
     * @param systemMsg
     */
    @Async
    @KafkaListener(
            topics = "${account.cashmachine.withdraw.receive}",
            groupId = "withdraw"
    )
    public void withdrawListening(@Payload String systemMsg) {
        if(!systemMsg.isEmpty()) this.systemMsg = systemMsg;
    }
    public String makeAWithdraw(Long cardId, Double amountOfMoney, Locale lang) {
        if (Boolean.TRUE.equals(isLoggedIn)) {
            kafkaTemplate.send(
                    kafkaTopicConfig.getWithdrawSend(),
                    propertiesConnector.getId() + ";" + cardId + ";" + amountOfMoney + ";" + lang
            );

            communicationWithBank();
            isLoggedIn = false;

            if(this.systemMsg.isEmpty())
                return "";
            else
                return systemMsg;
        }
        else return "";
    }

    /***
     * Show money method
     * @param money
     */
    @Async
    @KafkaListener(
            topics = "${account.cashmachine.show.receive}",
            groupId = "show"
    )
    public void showing(@Payload String money) {
        if(!money.isEmpty()) this.systemMsg = money;
    }

    public String showMoney(Long cardID, Locale lang) {
        if (Boolean.TRUE.equals(isLoggedIn)) {
            kafkaTemplate.send(
                    kafkaTopicConfig.getShowSend(),
                    propertiesConnector.getId() + ";" + cardID + ";" + lang
            );

            communicationWithBank();
            isLoggedIn = false;

            if(this.systemMsg.isEmpty())
                return "";
            else
                return systemMsg;
        }
        else return "";
    }


    @Async
    @Scheduled(fixedRate = 5000)
    public void logOut() {
        if (Boolean.TRUE.equals(isLoggedIn)) isLoggedIn = false;
    }


    // Helper method
    private void communicationWithBank() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
