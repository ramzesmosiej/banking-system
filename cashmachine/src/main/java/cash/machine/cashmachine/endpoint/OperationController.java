package cash.machine.cashmachine.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/payment")
    public ResponseEntity<String> makeAPayment() throws URISyntaxException {

        var request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8081/api/operations/payment"))
                .headers(
                        "Content-Type", "application/json"
                )
                .POST(HttpRequest.BodyPublishers.ofString(
                        """
                                {
                                    "userId": "2",
                                    "cash": "100"
                                }
                             """
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
