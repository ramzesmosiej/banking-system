package com.bankingapp.bankingapp.config;

import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Getter
public class KafkaTopicConfig {

    @Value("${account.cashmachine.pin.send}")
    private String sendPin;
    @Value("${account.cashmachine.pin.receive}")
    private String receivePinAnswear;

    @Value("${account.cashmachine.payment.send}")
    private String paymentSend;

    @Value("${account.cashmachine.payment.receive}")
    private String paymentReceive;

    @Value("${account.cashmachine.withdraw.send}")
    private String withdrawSend;

    @Value("${account.cashmachine.withdraw.receive}")
    private String withdrawReceive;

    @Bean
    public NewTopic sendPinToVerification() {
        return TopicBuilder.name(sendPin).build();
    }

    @Bean
    public NewTopic receivePinVerification() {
        return TopicBuilder.name(receivePinAnswear).build();
    }

    @Bean
    public NewTopic sendPayment() {
        return TopicBuilder.name(paymentSend).build();
    }

    @Bean
    public NewTopic receivePayment() {
        return TopicBuilder.name(paymentReceive).build();
    }

    @Bean
    public NewTopic sendWithdraw() {
        return TopicBuilder.name(withdrawSend).build();
    }

    @Bean
    public NewTopic receiveWithdraw() {
        return TopicBuilder.name(withdrawReceive).build();
    }

}
