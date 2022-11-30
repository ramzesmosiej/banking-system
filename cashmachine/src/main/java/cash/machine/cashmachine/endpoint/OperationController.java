package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.OperationEntity;
import com.client.openfeign.clients.BankingAppClient;
import com.client.openfeign.dto.CashOperationRequest;
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

    private final BankingAppClient bankingAppClient;

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody @Valid OperationEntity operationEntity,
            @RequestHeader(required = false) Locale lang
    ) {
        var language = lang == null ? Locale.US : lang;

        var verifying = bankingAppClient.isPINCorrect(
                operationEntity.getCardID(), operationEntity.getCardPIN()
        ).getBody();

        if (verifying == null || !verifying)
            return ResponseEntity.status(403).build();

        var response = Objects.requireNonNull(bankingAppClient.addCashToAccount(
                operationEntity.getCardID(), operationEntity.getAmountOfMoney(), language
        ).getBody());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawMoney(
            @RequestBody @Valid OperationEntity operationEntity,
            @RequestHeader(required = false) Locale lang
    ) {
        var language = lang == null ? Locale.US : lang;

        var verifying = bankingAppClient.isPINCorrect(
                operationEntity.getCardID(), operationEntity.getCardPIN()
        ).getBody();

        if (verifying == null || !verifying)
            return ResponseEntity.status(403).build();

        var response = Objects.requireNonNull(bankingAppClient.withdrawCash(
                operationEntity.getCardID(),operationEntity.getAmountOfMoney(), language
        ).getBody());

        return ResponseEntity.ok(response);
    }
}
