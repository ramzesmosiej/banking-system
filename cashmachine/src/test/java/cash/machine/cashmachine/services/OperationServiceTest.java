package cash.machine.cashmachine.services;

import cash.machine.cashmachine.config.KafkaTopicConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    PropertiesConnector propertiesConnector;

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    KafkaTopicConfig kafkaTopicConfig;

    @InjectMocks
    OperationService operationService;

    @Test
    void logging() {
        operationService.logging("OK");

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("isLoggedIn");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        Boolean isPinOk;
        try {
            isPinOk = (Boolean) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(isPinOk).isTrue();
    }

    @Test
    void logInto_BadData() {
        var response = operationService.logInto(1234L, "1234");
        assertThat(response).isEqualTo("AUTH_ERROR");
    }

    @Test
    void logInto_OKData() {
        operationService.logging("OK");

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("isLoggedIn");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        Boolean isPinOk;
        try {
            isPinOk = (Boolean) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(isPinOk).isTrue();
        var response = operationService.logInto(1234L, "1234");
        assertThat(response).isEqualTo("OK");
    }

    @Test
    void paymentListening() {
        operationService.paymentListening("Payment_OK");

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("systemMsg");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        String systemMsg;
        try {
            systemMsg = (String) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(systemMsg).isEqualTo("Payment_OK");
    }

    @Test
    void makeAPayment() {

    }

    @Test
    void withdrawListening() {
        operationService.withdrawListening("Withdraw_OK");

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("systemMsg");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        String systemMsg;
        try {
            systemMsg = (String) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(systemMsg).isEqualTo("Withdraw_OK");
    }

    @Test
    void makeAWithdraw() {
    }

    @Test
    void showing() {
        operationService.showing("Account_status");

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("systemMsg");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        String systemMsg;
        try {
            systemMsg = (String) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(systemMsg).isEqualTo("Account_status");
    }

    @Test
    void showMoney() {
    }

    @Test
    void logOut() {
        operationService.logging("OK");
        operationService.logOut();

        Field privateField;
        try {
            privateField = OperationService.class.getDeclaredField("isLoggedIn");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        privateField.setAccessible(true);

        Boolean isPinOk;
        try {
            isPinOk = (Boolean) privateField.get(operationService);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertThat(isPinOk).isFalse();
    }
}