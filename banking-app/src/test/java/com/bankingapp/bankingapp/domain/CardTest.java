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
        assertThat(card2.getAccount()).isNull();
        assertThat(card1.getPIN()).isEqualTo(VALID_PIN);
        assertThat(card2.getPIN()).isEqualTo(VALID_PIN);
    }

    @Test
    public void relationshipAccountWithCardTest() {
        var card1 = VALID_CARD;
        var account1 = VALID_ACCOUNT;
        account1.setCard(card1);
        card1.setAccount(account1);

        assertThat(card1.getAccount()).isNotNull();
        assertThat(account1.getCard().getPIN()).isEqualTo(VALID_PIN);
    }

}
