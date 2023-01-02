package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.CheckMoney;
import cash.machine.cashmachine.models.OperationEntity;
import cash.machine.cashmachine.models.PinEntity;
import cash.machine.cashmachine.services.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Controller
@RequestMapping("/cash-machine-api")
@Validated
public class OperationController {

    private final OperationService operationService;
    private final WebClient webClient = WebClient.create("http://localhost:8080/api/auth/ping");

    @PostMapping("/login")
    public ResponseEntity<String> logIntoSystem(
            @RequestBody PinEntity pinEntity,
            @RequestHeader Locale lang
    ) {
        String response;
        try {
            response = webClient.get().exchangeToMono(clientResponse ->
                    clientResponse.bodyToMono(String.class)
            ).block();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Main server isn't working");
        }
        if (response != null)
            return Objects.equals(operationService.logInto(pinEntity.getCardID(), pinEntity.getCardPIN()), "OK") ?
                ResponseEntity.ok("Logged into") :
                ResponseEntity.status(403).build();
        else
            return ResponseEntity.status(500).body("Main server isn't working");
    }

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody OperationEntity operationEntity,
            @RequestHeader Locale lang
    ) {
        var msgFromServer = operationService.makeAPayment(
                operationEntity.getCardID(),
                operationEntity.getAmountOfMoney(),
                lang
        );
        return msgFromServer.isEmpty() ?
                ResponseEntity.status(403).build() :
                ResponseEntity.ok(msgFromServer);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> makeAWithdraw(
            @RequestBody OperationEntity operationEntity,
            @RequestHeader Locale lang
    ) {
        var msgFromServer = operationService.makeAWithdraw(
                operationEntity.getCardID(),
                operationEntity.getAmountOfMoney(),
                lang
        );
        return msgFromServer.isEmpty() ?
                ResponseEntity.status(403).build() :
                ResponseEntity.ok(msgFromServer);
    }

    @PostMapping("/show")
    public ResponseEntity<String> showAccountMoney(
            @RequestBody CheckMoney checkMoney,
            @RequestHeader Locale lang
    ) {
        var msgFromServer = operationService.showMoney(
                checkMoney.getCardID(),
                lang
        );
        return msgFromServer.isEmpty() ?
                ResponseEntity.status(403).build() :
                ResponseEntity.ok(msgFromServer);
    }

}
