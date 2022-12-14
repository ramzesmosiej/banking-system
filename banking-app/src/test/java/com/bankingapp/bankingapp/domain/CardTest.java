package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static com.bankingapp.bankingapp.TestConsts.*;
import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    public void cardCreationTest() {
        var card1 = VALID_CARD;
        var card2 = new Card();
        card2.setPIN(VALID_PIN);
        System.out.println(card1.toString());

        assertThat(card1.getId()).isNull();
        assertThat(card1.getUser()).isNull();
        assertThat(card1.getPIN()).isEqualTo(VALID_PIN);
        assertThat(card2.getPIN()).isEqualTo(VALID_PIN);
    }

    @Test
    public void relationshipUserWithCardTest() {
        var user = VALID_USER_WITH_VALID_CARD;

        assertThat(user.getUserCard()).isNotNull();
        assertThat(user.getUserCard().getPIN()).isEqualTo(VALID_PIN);
    }

}
