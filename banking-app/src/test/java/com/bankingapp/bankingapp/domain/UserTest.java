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
        var user = VALID_USER_WITHOUT_CARD;
        assertThat(user.getId()).isNull();
        assertThat(user.getFirstName()).isEqualTo("Jan");
        assertThat(user.getLastName()).isEqualTo("Nowak");

        var user2 = new User();
        user2.setFirstName("Remigiusz");
        user2.setLastName("Pisarski");
        user2.setAmountOfMoney(0.0);
        user2.setLogin("remmo1");
        user2.setPassword("abcdg");
        user2.setEmail("remmo1@gmail.com");
        user2.setIsActive(true);
        user2.setAuthorities(Set.of(Authority.USER_AUTHORITY, Authority.EMPLOYEE_AUTHORITY));
        System.out.println(user2.toString());
        assertThat(user2.getFirstName()).isEqualTo("Remigiusz");
        assertThat(user2.getLastName()).isEqualTo("Pisarski");
        assertThat(user2.getAmountOfMoney()).isEqualTo(0.0);
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
    public void userCardConnection() {
        assertThat(VALID_USER_WITHOUT_CARD.getUserCard()).isNotNull();

        var user = new User();
        user.setUserCard(VALID_CARD);
        assertThat(user.getUserCard().getPIN()).isEqualTo("1234");
    }

}
