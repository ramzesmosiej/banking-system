package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    public void accountCreation() {
        var account1 = new Account();

        account1.setAmountOfMoney(1000);
        account1.setId(1L);

        assertThat(account1.getId()).isEqualTo(1L);
        assertThat(account1.getAmountOfMoney()).isEqualTo(1000);
    }

}
