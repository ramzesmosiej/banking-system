package cash.machine.cashmachine;

import cash.machine.cashmachine.models.OperationEntity;

public class TestConsts {

    public static OperationEntity VALID_OPERATION_ENTITY() {
        var operationEntity = new OperationEntity();
        operationEntity.setCardID(1234L);
        operationEntity.setCardPIN("1234");
        operationEntity.setAmountOfMoney(1000.0);
        return operationEntity;
    }

}
