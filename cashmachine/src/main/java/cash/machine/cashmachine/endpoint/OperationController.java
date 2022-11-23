package cash.machine.cashmachine.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/cash-machine-api")
public class OperationController {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @GetMapping("/payment")
    public ResponseEntity<String> makeAPayment() throws URISyntaxException, IOException, InterruptedException {

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

        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response);

        return ResponseEntity.ok(response.body());
    }

}
