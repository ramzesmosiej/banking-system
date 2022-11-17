package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;

public class TestConsts {
    private static final String VALID_PIN = "1234";
    private static final Card VALID_CARD = Card.builder().PIN(VALID_PIN).build();
    public static final User VALID_USER_WITH_VALID_CARD = User.builder()
            .firstName("Jan")
            .lastName("Nowak")
            .email("jan.nowak@gmail.com")
            .login("janN")
            .password("abcd")
            .userCard(VALID_CARD)
            .build();

}
