package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bankingapp.bankingapp.service.TestConsts.VALID_USER_WITH_VALID_CARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserAccountService userAccountService;

    @Test
    void addCashToUser_validUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(VALID_USER_WITH_VALID_CARD));

        var serverResponse = userAccountService.addCashToUser(2L, 1000.0);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(1500.0);

    }

    @Test
    void addCahToUser_invalidUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.addCashToUser(3L, 1200.0)
        ))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("dosen't exists in db");

    }

    @Test
    void takeCashFromAccount_validUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(VALID_USER_WITH_VALID_CARD));

        var serverResponse = userAccountService.takeCashFromAccount(2L, 200.0);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(300.0);

    }

    @Test
    void takeCashFromAccount_invalidUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(3L, 1200.0)
        ))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("dosen't exists in db");

    }

    @Test
    void takeCashFromAccount_notEnoughMoney() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(VALID_USER_WITH_VALID_CARD));

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(3L, 1200.0)
        ))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining("not enough money");

    }

}