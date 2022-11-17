package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static com.bankingapp.bankingapp.domain.TestConsts.VALID_USER_WITHOUT_CARD;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    public void userCreationTest() {
        var user = VALID_USER_WITHOUT_CARD;

        assertThat(user.getId()).isNull();
        assertThat(user.getFirstName()).isEqualTo("Jan");
        assertThat(user.getLastName()).isEqualTo("Nowak");
    }

}
