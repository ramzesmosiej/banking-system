package com.client.openfeign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class CashOperationRequest {

    @NotNull
    @PositiveOrZero
    private Long userId;

    @NotNull
    @PositiveOrZero
    private Double cash;

}
