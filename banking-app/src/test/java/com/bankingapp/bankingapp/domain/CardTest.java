package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    public void cardCreationTest() {
        var card1 = VALID_CARD;

        assertThat(card1.getId()).isNull();
        assertThat(card1.getPIN()).isEqualTo(VALID_PIN);
    }

    @Test
    public void relationshipUserWithCardTest() {
        var user = VALID_USER_WITH_VALID_CARD;

        assertThat(user.getUserCard()).isNotNull();
        assertThat(user.getUserCard().getPIN()).isEqualTo(VALID_PIN);
    }

    private static final String VALID_PIN = "1234";
    private static final Card VALID_CARD = Card.builder().PIN(VALID_PIN).build();
    private static final User VALID_USER_WITH_VALID_CARD = User.builder()
            .firstName("Jan")
            .lastName("Nowak")
            .email("jan.nowak@gmail.com")
            .login("janN")
            .password("abcd")
            .userCard(VALID_CARD)
            .isActive(true)
            .build();

}
