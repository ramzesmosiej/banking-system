package com.bankingapp.bankingapp.domain;

public class TestConsts {

     static final String VALID_PIN = "1234";
     static final Card VALID_CARD = Card.builder().PIN(VALID_PIN).build();
     static final User VALID_USER_WITHOUT_CARD = User.builder()
             .firstName("Jan")
             .lastName("Nowak")
             .email("jan.nowak@gmail.com")
             .login("janN")
             .password("abcd")
             .isActive(true)
             .amountOfMoney(0.0)
             .build();
     static final User VALID_USER_WITH_VALID_CARD = User.builder()
            .firstName("Jan")
            .lastName("Nowak")
            .email("jan.nowak@gmail.com")
            .login("janN")
            .password("abcd")
            .userCard(VALID_CARD)
            .isActive(true)
            .amountOfMoney(0.0)
            .build();

}
