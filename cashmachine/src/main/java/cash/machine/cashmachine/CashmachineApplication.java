package cash.machine.cashmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableKafka
@SpringBootApplication
public class CashmachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashmachineApplication.class, args);
	}

}
