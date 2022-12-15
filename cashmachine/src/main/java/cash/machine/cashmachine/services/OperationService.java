package cash.machine.cashmachine.services;

import cash.machine.cashmachine.config.KafkaTopicConfig;
import cash.machine.cashmachine.models.OperationEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Locale;

@AllArgsConstructor
@Service
public class OperationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final PropertiesConnector propertiesConnector;

    private static Boolean isLoggedIn = false;
    private static String systemMsg = "";

    /***
     * Logging method to cash machine
     * @param pinStatus
     */
    @Async
    @KafkaListener(
            topics = "${account.cashmachine.pin.receive}",
            groupId = "logging"
    )
    public void logging(@Payload String pinStatus) {
        if(pinStatus.equals("OK")) OperationService.isLoggedIn = true;
    }

    public String logInto(Long cardID, String cardPIN, Locale lang) {
        kafkaTemplate.send(kafkaTopicConfig.getSendPin(), propertiesConnector.getId() + ";" + cardID + ";" + cardPIN);

        communicationWithBank();

        if(Boolean.TRUE.equals(OperationService.isLoggedIn))
            return "OK";
        else
            return "AUTH_ERROR";
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
    public synchronized void paymentListening(@Payload String systemMsg) {
        if(!systemMsg.isEmpty()) OperationService.systemMsg = systemMsg;
    }

    public synchronized String makeAPayment(Long cardId, Double amountOfMoney, Locale lang) {
        if (Boolean.TRUE.equals(isLoggedIn)) {
            kafkaTemplate.send(
                    kafkaTopicConfig.getPaymentSend(),
                    propertiesConnector.getId() + ";" + cardId + ";" + amountOfMoney + ";" + lang
            );

            communicationWithBank();
            OperationService.isLoggedIn = false;

            if(systemMsg.isEmpty())
                return "";
            else
                return systemMsg;
        }
        else return "";
    }


    // Helper method
    private void communicationWithBank() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
