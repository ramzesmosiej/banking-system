package cash.machine.cashmachine.services;

import com.client.openfeign.clients.BankingAppClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static cash.machine.cashmachine.TestConsts.VALID_OPERATION_ENTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    BankingAppClient bankingAppClient;

    @InjectMocks
    OperationService operationService;

    @Test
    void makeAPayment_noCardWithTheGivenId() {
        mockCardShouldReturn(false);
        var operationEntity = VALID_OPERATION_ENTITY();
        var response = operationService.makeAPayment(operationEntity, null);
        assertThat(response).isEqualTo("AUTH_ERROR");
    }

    @Test
    void makeAPayment_validData() {
        mockCardShouldReturn(true);
        when(bankingAppClient.addCashToAccount(anyLong(), anyDouble(), any())).
                thenReturn(ResponseEntity.ok("Operation successful! Cash was added successfuly! Now you have: 1000.0"));

        var operationEntity = VALID_OPERATION_ENTITY();
        var response = operationService.makeAPayment(operationEntity, Locale.US);

        assertThat(response).contains("Operation successful! Cash was added successfuly! Now you have:");
    }

    @Test
    void withdrawMoney_noCardWithTheGivenId() {
        mockCardShouldReturn(false);
        var operationEntity = VALID_OPERATION_ENTITY();
        var response = operationService.withdrawMoney(operationEntity, null);
        assertThat(response).isEqualTo("AUTH_ERROR");
    }


    @Test
    void withdrawMoney_validData() {
        mockCardShouldReturn(true);
        when(bankingAppClient.withdrawCash(anyLong(), anyDouble(), any())).
                thenReturn(ResponseEntity.ok("Operation successful! Now you have: 100.0"));

        var operationEntity = VALID_OPERATION_ENTITY();
        var response = operationService.withdrawMoney(operationEntity, Locale.US);

        assertThat(response).contains("Operation successful! Now you have:");
    }

    private void mockCardShouldReturn(Boolean shouldReturn) {
        when(bankingAppClient.isPINCorrect(anyLong(), anyString()))
                .thenReturn(ResponseEntity.badRequest().body(shouldReturn));
    }

}
