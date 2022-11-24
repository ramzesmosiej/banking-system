package cash.machine.cashmachine.models;

import lombok.Data;

@Data
public class OperationEntity {
    private Long cardID;
    private String cardPIN;
    private Double amountOfMoney;
}
