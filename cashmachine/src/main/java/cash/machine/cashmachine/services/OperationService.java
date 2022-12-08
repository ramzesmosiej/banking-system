package cash.machine.cashmachine.services;

import cash.machine.cashmachine.models.OperationEntity;
import com.client.openfeign.clients.BankingAppClient;
import lombok.AllArgsConstructor;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@Service
public class OperationService {

    private final BankingAppClient bankingAppClient;
    private final PropertiesConnector propertiesConnector;


    public String makeAPayment(OperationEntity operationEntity, Locale lang) {
        var response = checkCard(operationEntity.getCardID(), operationEntity.getCardPIN(), lang);
        return response.getValue0().equals("OK") ? Objects.requireNonNull(bankingAppClient.addCashToAccount(
                operationEntity.getCardID(),
                operationEntity.getAmountOfMoney(),
                propertiesConnector.getId(),
                response.getValue1()
        ).getBody()) : "AUTH_ERROR";
    }

    public String withdrawMoney(OperationEntity operationEntity, Locale lang) {
        var response = checkCard(operationEntity.getCardID(), operationEntity.getCardPIN(), lang);
        return response.getValue0().equals("OK") ? Objects.requireNonNull(bankingAppClient.withdrawCash(
                operationEntity.getCardID(),
                operationEntity.getAmountOfMoney(),
                propertiesConnector.getId(),
                response.getValue1()
        ).getBody()) : "AUTH_ERROR";
    }

    private Pair<String, Locale> checkCard(Long cardId, String cardPIN, Locale lang) {
        var language = lang == null ? Locale.US : lang;

        var verifying = bankingAppClient.isPINCorrect(cardId, cardPIN, propertiesConnector.getId()).getBody();

        return verifying == null || !verifying ?
                new Pair<>("AUTH_ERROR", language) : new Pair<>("OK", language);
    }

}
