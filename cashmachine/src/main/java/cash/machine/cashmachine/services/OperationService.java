package cash.machine.cashmachine.services;

import cash.machine.cashmachine.models.OperationEntity;
import com.client.openfeign.clients.BankingAppClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Service
public class OperationService {

    private final BankingAppClient bankingAppClient;

    public String makeAPayment(OperationEntity operationEntity, Locale lang) {
        var language = lang == null ? Locale.US : lang;

        var verifying = bankingAppClient.isPINCorrect(
                operationEntity.getCardID(), operationEntity.getCardPIN()
        ).getBody();

        if (verifying == null || !verifying)
            return "AUTH_ERROR";

        return Objects.requireNonNull(bankingAppClient.addCashToAccount(
                operationEntity.getCardID(), operationEntity.getAmountOfMoney(), language
        ).getBody());
    }

    public String withdrawMoney(OperationEntity operationEntity, Locale lang) {
        var language = lang == null ? Locale.US : lang;

        var verifying = bankingAppClient.isPINCorrect(
                operationEntity.getCardID(), operationEntity.getCardPIN()
        ).getBody();

        if (verifying == null || !verifying)
            return "AUTH_ERROR";

        return Objects.requireNonNull(bankingAppClient.withdrawCash(
                operationEntity.getCardID(),operationEntity.getAmountOfMoney(), language
        ).getBody());
    }

}
