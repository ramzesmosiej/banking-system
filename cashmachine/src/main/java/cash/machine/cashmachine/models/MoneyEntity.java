package cash.machine.cashmachine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MoneyEntity {
    private Long cardID;
    private Double amount;
}
