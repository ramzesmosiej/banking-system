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

    @Bean
    public NewTopic takePin() {
        return TopicBuilder.name(receivePinAnswear).build();
    }

    @Bean
    public NewTopic returnStatus() {
        return TopicBuilder.name(sendPin).build();
    }

}