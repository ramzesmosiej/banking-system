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

    private void communicationWithBank() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /*public String makeAPayment(OperationEntity operationEntity, Locale lang) {
        var operationStatus = kafkaTemplate.send(kafkaTopicConfig.getPayment(), returnCredentials(operationEntity, lang));

        operationStatus.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("FAILURE");
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println(result);
            }
        });

        return "";
    }

    public String withdrawMoney(OperationEntity operationEntity, Locale lang) {
        return "";
    }

    private String returnCredentials(OperationEntity operationEntity, Locale lang) {
        return operationEntity.getCardID() + operationEntity.getCardPIN() + operationEntity.getAmountOfMoney() + lang;
    }*/


}
