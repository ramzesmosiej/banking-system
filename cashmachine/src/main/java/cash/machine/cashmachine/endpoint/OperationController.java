package cash.machine.cashmachine.endpoint;

import cash.machine.cashmachine.models.OperationEntity;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/cash-machine-api")
public class OperationController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @PostMapping("/payment")
    public ResponseEntity<String> makeAPayment(
            @RequestBody OperationEntity operationEntity
            ) throws URISyntaxException {

        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/api/operations/payment"))
                .headers(
                        "Content-Type", "application/json"
                )
                .POST(HttpRequest.BodyPublishers.ofString(
                        new JSONObject()
                                .put("userId", operationEntity.getCardId())
                                .put("cash", operationEntity.getAmountOfMoney())
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
        }
    }

}
