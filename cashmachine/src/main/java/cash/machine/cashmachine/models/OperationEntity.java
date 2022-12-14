package cash.machine.cashmachine.models;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class OperationEntity {
    @Positive
    private Double amountOfMoney;
}
