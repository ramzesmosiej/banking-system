package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.OperationEntity;
import cash.machine.cashmachine.services.OperationService;
import com.client.openfeign.clients.BankingAppClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Controller
@RequestMapping("/cash-machine-api")
@Validated
public class OperationController {

    private final OperationService operationService;

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody @Valid OperationEntity operationEntity,
            @RequestHeader(required = false) Locale lang
    ) {
        var result = operationService.makeAPayment(operationEntity, lang);
        return Objects.equals(result, "AUTH_ERROR") ?
                ResponseEntity.status(403).build() : ResponseEntity.ok(result);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawMoney(
            @RequestBody @Valid OperationEntity operationEntity,
            @RequestHeader(required = false) Locale lang
    ) {
        var result = operationService.withdrawMoney(operationEntity, lang);
        return Objects.equals(result, "AUTH_ERROR") ?
                ResponseEntity.status(403).build() : ResponseEntity.ok(result);
    }
    
}
