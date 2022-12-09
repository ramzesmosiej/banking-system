package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityTest {

    @Test
    public void authorityCreation() {
        var authority1 = new Authority();
        assertThat(authority1).isInstanceOf(Authority.class);

        var authority2 = Authority.USER_AUTHORITY;
        assertThat(authority2.getName()).isEqualTo("ROLE_USER");
    }

}
