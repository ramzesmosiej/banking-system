package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.AuthenticationEntity;
import cash.machine.cashmachine.models.MoneyEntity;
import cash.machine.cashmachine.models.OperationEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@Controller
@RequestMapping("/cash-machine-api")
public class OperationController {

    private final SystemAppProxy systemAppProxy;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody OperationEntity operationEntity
            ) throws URISyntaxException, ExecutionException, InterruptedException {

        var verifying = systemAppProxy.verifyCardAndTransaction(
                new AuthenticationEntity(operationEntity.getCardID(), operationEntity.getCardPIN())
        ).getBody();

        if (verifying == null || !verifying)
            return ResponseEntity.status(403).build();

        var response = systemAppProxy.addCashToAccount(
                new MoneyEntity(operationEntity.getCardID(), operationEntity.getAmountOfMoney())
        ).getBody();

        return ResponseEntity.ok(response);
/*
        // TODO to remove quickly
        var getToken = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        new JSONObject()
                                .put("login", "remmo1")
                                .put("password", "1234abcd")
                                .toString()
                ))
                .build();

        CompletableFuture<HttpResponse<String>> token = HttpClient.newBuilder()
                .build()
                .sendAsync(getToken, HttpResponse.BodyHandlers.ofString());
        System.out.println(token.get().body());

        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/api/operations/payment"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token.get().body())
                .POST(HttpRequest.BodyPublishers.ofString(
                        new JSONObject()
                                .put("userId", 2)
                                .put("cash", 100)
                                .toString()
                ))
                .build();

        CompletableFuture<HttpResponse<String>>response = HttpClient.newBuilder()
                .build()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString());

        try {
            return ResponseEntity.ok(response.get().body());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }*/

    }

}
