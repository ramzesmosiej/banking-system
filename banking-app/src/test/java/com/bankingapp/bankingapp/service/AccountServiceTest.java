package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.exceptions.NotEnoughMoneyException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
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
class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @Mock
    PropertiesLanguageConnector propertiesLanguageConnector;
    @InjectMocks
    AccountService userAccountService;

    @Test
    void addCashToAccount_validAccountID() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = accountRepository.findById(VALID_ACCOUNT_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();

        var serverResponse = userAccountService.addCashToAccount(VALID_ACCOUNT_ID, VALID_CASH);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney + VALID_CASH
        );

    }

    @Test
    void addCashToAccount_validAccountID_and_validLocale() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = accountRepository.findById(VALID_ACCOUNT_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();

        var serverResponse = userAccountService.addCashToAccount(VALID_ACCOUNT_ID, VALID_CASH, Locale.US);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney + VALID_CASH
        );

    }

    @Test
    void addCahToUser_invalidAccountID() {

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.addCashToAccount(VALID_ACCOUNT_ID, VALID_CASH)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("doesn't exists");

    }

    @Test
    void takeCashFromAccount_validAccountID() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = accountRepository.findById(VALID_ACCOUNT_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();
        var serverResponse = userAccountService.takeCashFromAccount(VALID_ACCOUNT_ID, VALID_CASH);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney - VALID_CASH
        );

    }

    @Test
    void takeCashFromAccount_validUserID_and_validLocale() throws InterruptedException {

        mocking();

        var initalAmountOfMoney = accountRepository.findById(VALID_ACCOUNT_ID)
                .orElseThrow(() -> new UserNotFoundException("")).getAmountOfMoney();
        var serverResponse = userAccountService.takeCashFromAccount(VALID_ACCOUNT_ID, VALID_CASH, Locale.US);
        assertThat(serverResponse).contains("Operation successful");

        var actualAmountOfMoney = serverResponse.split(":");
        assertThat(Double.parseDouble(actualAmountOfMoney[1].trim())).isEqualTo(
                initalAmountOfMoney - VALID_CASH
        );

    }

    @Test
    void takeCashFromAccount_invalidAccountID() {

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(VALID_ACCOUNT_ID, VALID_CASH)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("doesn't exists");

    }

    @Test
    void takeCashFromAccount_notEnoughMoney() {

        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(VALID_ACCOUNT));

        assertThat(catchThrowable(() ->
                userAccountService.takeCashFromAccount(VALID_USER_ID, CASH_FOR_RICH)
        ))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining("Not enough money");

    }

    private void mocking() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(VALID_ACCOUNT));
        when(accountRepository.save(ArgumentMatchers.any())).thenReturn(VALID_ACCOUNT);
        when(propertiesLanguageConnector.getMessageOnLanguage(any(), any())).thenReturn(
                "Operation successful! Cash was added successfuly! Now you have:"
        );
    }

}