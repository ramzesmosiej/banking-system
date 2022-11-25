package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.OperationEntity;
import com.client.openfeign.clients.BankingAppClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.http.HttpClient;
import java.util.Objects;

@AllArgsConstructor
@Controller
@RequestMapping("/cash-machine-api")
public class OperationController {

    private final BankingAppClient bankingAppClient;

    // private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody OperationEntity operationEntity
            ) {

        var verifying = bankingAppClient.isPINCorrect(
                operationEntity.getCardID(), operationEntity.getCardPIN()
        ).getBody();

        if (verifying == null || !verifying)
            return ResponseEntity.status(403).build();

        var response = Objects.requireNonNull(bankingAppClient.addCashToAccount(
                operationEntity.getCardID(), operationEntity.getAmountOfMoney()
        ).getBody()).toString();

        return ResponseEntity.ok(response);
    }

}
