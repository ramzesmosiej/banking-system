package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

    @Test
    public void cardCreationTest() {
        var card1 = Card.builder().PIN(1234).build();

        assertThat(card1.getId()).isNull();
        assertThat(card1.getPIN()).isEqualTo(1234);
    }

}
