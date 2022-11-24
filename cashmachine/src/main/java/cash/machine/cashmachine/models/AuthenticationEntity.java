package cash.machine.cashmachine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationEntity {
    private Long cardID;
    private String cardPIN;
}
