package cash.machine.cashmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.client.openfeign.clients")
@SpringBootApplication
public class CashmachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashmachineApplication.class, args);
	}

}
