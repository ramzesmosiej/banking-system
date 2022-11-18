package com.bankingapp.bankingapp.DTO;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
}
