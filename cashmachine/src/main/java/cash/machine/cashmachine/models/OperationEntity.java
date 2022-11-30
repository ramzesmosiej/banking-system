package cash.machine.cashmachine.models;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class OperationEntity {
    private Long cardID;
    private String cardPIN;

    @Positive
    private Double amountOfMoney;
}
