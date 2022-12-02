package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Optional;

import static com.bankingapp.bankingapp.TestConsts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PropertiesLanguageConnector propertiesLanguageConnector;
    @InjectMocks
    AccountService userAccountService;

    @Test
    void addCashToUser_validUserID() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = userRepository.findById(VALID_USER_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();

        var serverResponse = userAccountService.addCashToUser(VALID_USER_ID, VALID_CASH);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney + VALID_CASH
        );

    }

    @Test
    void addCashToUser_validUserID_and_validLocale() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = userRepository.findById(VALID_USER_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();

        var serverResponse = userAccountService.addCashToUser(VALID_USER_ID, VALID_CASH, Locale.US);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney + VALID_CASH
        );

    }

    @Test
    void addCahToUser_invalidUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.addCashToUser(VALID_USER_ID, VALID_CASH)
        ))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("dosen't exists in db");

    }

    @Test
    void takeCashFromAccount_validUserID() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = userRepository.findById(VALID_USER_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();
        var serverResponse = userAccountService.takeCashFromAccount(VALID_USER_ID, VALID_CASH);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney - VALID_CASH
        );

    }

    @Test
    void takeCashFromAccount_validUserID_and_validLocale() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = userRepository.findById(VALID_USER_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();
        var serverResponse = userAccountService.takeCashFromAccount(VALID_USER_ID, VALID_CASH, Locale.US);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney - VALID_CASH
        );

    }

    @Test
    void takeCashFromAccount_invalidUserID() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(VALID_USER_ID, VALID_CASH)
        ))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("dosen't exists in db");

    }

    @Test
    void takeCashFromAccount_notEnoughMoney() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(VALID_USER_WITH_VALID_CARD));

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(VALID_USER_ID, CASH_FOR_RICH)
        ))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining("Not enough money");

    }

    private void mocking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(VALID_USER_WITH_VALID_CARD));
        when(userRepository.save(ArgumentMatchers.any())).thenReturn(VALID_USER_WITH_VALID_CARD);
        when(propertiesLanguageConnector.getMessageOnLanguage(any(), any())).thenReturn(
                "Operation successful! Cash was added successfuly! Now you have:"
        );
    }

}