package cash.machine.cashmachine.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "cash.machine")
@Configuration
@Getter
@Setter
public class PropertiesConnector {
    private String id;
    private Integer money;
}
