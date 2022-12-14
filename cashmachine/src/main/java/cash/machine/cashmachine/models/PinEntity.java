package cash.machine.cashmachine.models;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class PinEntity {
    private Long cardID;
    private String cardPIN;
}
