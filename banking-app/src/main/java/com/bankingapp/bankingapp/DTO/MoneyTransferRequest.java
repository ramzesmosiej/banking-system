package com.bankingapp.bankingapp.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class MoneyTransferRequest {

    @NotNull
    private Long senderId;
    @NotNull
    private Long receiverId;
    @PositiveOrZero
    private Double amount;
}
