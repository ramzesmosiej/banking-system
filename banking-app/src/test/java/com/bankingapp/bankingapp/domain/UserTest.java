package com.bankingapp.bankingapp.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

import static com.bankingapp.bankingapp.TestConsts.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    public void userCreationTest() {
        var user = VALID_USER;
        assertThat(user.getId()).isNull();
        assertThat(user.getFirstName()).isEqualTo("Jan");
        assertThat(user.getLastName()).isEqualTo("Nowak");

        var user2 = new User();
        user2.setFirstName("Remigiusz");
        user2.setLastName("Pisarski");
        user2.setLogin("remmo1");
        user2.setPassword("abcdg");
        user2.setEmail("remmo1@gmail.com");
        user2.setIsActive(true);
        user2.setAuthorities(Set.of(Authority.USER_AUTHORITY, Authority.EMPLOYEE_AUTHORITY));
        System.out.println(user2.toString());

        assertThat(user2.getFirstName()).isEqualTo("Remigiusz");
        assertThat(user2.getLastName()).isEqualTo("Pisarski");
        assertThat(user2.getLogin()).isEqualTo("remmo1");
        assertThat(user2.getPassword()).isEqualTo("abcdg");
        assertThat(user2.getEmail()).isEqualTo("remmo1@gmail.com");
        assertThat(user2.getAuthorities()).hasSameElementsAs(
                Set.of(Authority.USER_AUTHORITY, Authority.EMPLOYEE_AUTHORITY));
        assertThat(user2.getIsActive()).isTrue();
        assertThat(user2.getGrantedAuthorities()).hasSameElementsAs(
                List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"), new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    public void userAccountConnection() {

        var user1 = VALID_USER;
        var account1 = VALID_ACCOUNT;
        account1.setUser(user1);
        user1.getAccounts().add(account1);

        assertThat(user1.getAccounts()).isNotEmpty();
        assertThat(account1.getUser()).isNotNull();

    }

}
