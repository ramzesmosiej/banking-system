package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.AuthenticationEntity;
import cash.machine.cashmachine.models.MoneyEntity;
import cash.machine.cashmachine.models.OperationEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "banking-app")
public interface SystemAppProxy {

    @PostMapping("/machine/auth/card")
    ResponseEntity<Boolean> verifyCardAndTransaction(
            @RequestBody AuthenticationEntity authenticationEntity
    );

    @PostMapping("/machine/add/cash")
    ResponseEntity<String> addCashToAccount(
            @RequestBody MoneyEntity moneyEntity
    );

}
