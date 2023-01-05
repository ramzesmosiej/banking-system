package com.bankingapp.bankingapp.DTO;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ChangePinRequest {
    private Long cardID;
    @Pattern(regexp = "\\d\\d\\d\\d")
    private String newPin;
}
