package cash.machine.cashmachine.services;

import cash.machine.cashmachine.config.KafkaTopicConfig;
import cash.machine.cashmachine.models.OperationEntity;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Locale;

@AllArgsConstructor
@Service
public class OperationService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;
    private final PropertiesConnector propertiesConnector;

    public String makeAPayment(OperationEntity operationEntity, Locale lang) {
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
    }

}
