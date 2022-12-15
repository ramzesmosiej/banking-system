package cash.machine.cashmachine.services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    /*@Mock
    BankingAppClient bankingAppClient;

    @Mock
    PropertiesConnector propertiesConnector;

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
        when(bankingAppClient.addCashToAccount(anyLong(), anyDouble(), anyString(), any())).
                thenReturn(ResponseEntity.ok("Operation successful! " +
                        "Cash was added successfuly! Now you have: 1000.0"));

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
        when(bankingAppClient.withdrawCash(anyLong(), anyDouble(), anyString(), any())).
                thenReturn(ResponseEntity.ok("Operation successful! Now you have: 100.0"));

        var operationEntity = VALID_OPERATION_ENTITY();
        var response = operationService.withdrawMoney(operationEntity, Locale.US);

        assertThat(response).contains("Operation successful! Now you have:");
    }

    private void mockCardShouldReturn(Boolean shouldReturn) {
        when(propertiesConnector.getId()).thenReturn("3");
        when(bankingAppClient.isPINCorrect(anyLong(), anyString(), anyString()))
                .thenReturn(ResponseEntity.badRequest().body(shouldReturn));
    }
*/
}
