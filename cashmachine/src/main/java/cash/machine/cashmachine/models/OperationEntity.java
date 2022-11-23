package cash.machine.cashmachine.models;

import lombok.Data;

@Data
public class OperationEntity {
    private Long cardId;
    private String PIN;
    private Integer amountOfMoney;
}
