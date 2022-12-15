package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.OperationEntity;
import cash.machine.cashmachine.models.PinEntity;
import cash.machine.cashmachine.services.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/login")
    public ResponseEntity<String> logIntoSystem(
            @RequestBody PinEntity pinEntity,
            @RequestHeader Locale lang
    ) {
        return Objects.equals(operationService.logInto(pinEntity.getCardID(), pinEntity.getCardPIN(), lang), "OK") ?
                ResponseEntity.ok("Logged into") :
                ResponseEntity.status(403).build();
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

}
