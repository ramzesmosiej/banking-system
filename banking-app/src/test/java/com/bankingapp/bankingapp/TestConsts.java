package com.bankingapp.bankingapp;

import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;

public class TestConsts {

    // Creation consts
    public static final String VALID_PIN = "1234";
    public static final Card VALID_CARD = Card.builder().PIN(VALID_PIN).build();
    public static final User VALID_USER_WITH_VALID_CARD = User.builder()
            .firstName("Jan")
            .lastName("Nowak")
            .email("jan.nowak@gmail.com")
            .login("janN")
            .password("abcd")
            .userCard(VALID_CARD)
            .amountOfMoney(500.0)
            .build();

    public static final User VALID_USER_WITHOUT_CARD = User.builder()
            .firstName("Jan")
            .lastName("Nowak")
            .email("jan.nowak@gmail.com")
            .login("janN")
            .password("abcd")
            .userCard(VALID_CARD)
            .amountOfMoney(500.0)
            .build();

    // Service consts
    public static final Long VALID_USER_ID = 2L;
    public static final Double VALID_CASH = 200.0;
    public static final Double CASH_FOR_RICH = 20000.0;
    public static RegistrationRequest VALID_USER_REQUEST() {
        var requestUser = new RegistrationRequest();
        requestUser.setFirstName("Jan");
        requestUser.setLastName("Nowak");
        requestUser.setLogin("janN");
        requestUser.setEmail("jan.nowak@gmail.com");
        requestUser.setPassword("3214");
        return requestUser;
    }

}
