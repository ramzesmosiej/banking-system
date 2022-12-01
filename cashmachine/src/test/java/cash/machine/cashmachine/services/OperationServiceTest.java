package cash.machine.cashmachine.services;

import cash.machine.cashmachine.models.OperationEntity;
import com.client.openfeign.clients.BankingAppClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    BankingAppClient bankingAppClient;

    @InjectMocks
    OperationService operationService;

    @Test
    void makeAPayment_noCardWithTheGivenId() {
        when(bankingAppClient.isPINCorrect(anyLong(), anyString()))
                .thenReturn(ResponseEntity.badRequest().body(false));

        var operationEntity = new OperationEntity();
        operationEntity.setCardID(1234L);
        operationEntity.setCardPIN("1234");
        operationEntity.setAmountOfMoney(1000.0);

        var response = operationService.makeAPayment(operationEntity, null);

        assertThat(response).isEqualTo("AUTH_ERROR");
    }



    @Test
    void withdrawMoney() {
    }
}