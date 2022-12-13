package cash.machine.cashmachine.config;

import lombok.Getter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Getter
public class KafkaTopicConfig {

    @Value("${account.operation.payment}")
    private String payment;

    @Value("${account.operation.withdraw}")
    private String withdraw;

    @Bean
    public NewTopic doPayment() {
        return TopicBuilder.name(payment).build();
    }

    @Bean
    public NewTopic doWithdraw() {
        return TopicBuilder.name(withdraw).build();
    }

}
