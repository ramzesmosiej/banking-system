package com.bankingapp.bankingapp.DTO;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class CashOperationRequest {

    @NotNull
    @PositiveOrZero
    private Long accountId;

    @NotNull
    @PositiveOrZero
    private Double cash;

}
