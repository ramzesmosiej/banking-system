package com.bankingapp.bankingapp.DTO;

import lombok.Data;

@Data
public class ChangePinRequest {
    private Long cardID;
    private String newPin;
}
