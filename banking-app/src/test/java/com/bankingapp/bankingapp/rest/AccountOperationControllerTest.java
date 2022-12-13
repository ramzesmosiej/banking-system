package com.bankingapp.bankingapp.rest;

import com.bankingapp.bankingapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.security.auth.login.AccountNotFoundException;

import static com.bankingapp.bankingapp.TestConsts.isFirstClientWithCardCreated;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class AccountOperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void getUser() throws Exception {
        var token = getEmployeeAccessToken();

        var getUserRequest = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/operations/1")
                .header("Authorization", token)
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        var responseBody = getUserRequest.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isNotNull().contains("\"id\":1");
    }

    @Test
    void payment() throws Exception {
        var token = createUserWithAccountAndReturnAccessToken();

        // make a payment in polish version
        var initialAccountMoney = accountRepository.findById(1L)
                .orElseThrow(() -> new AccountNotFoundException("Account with the given Id doesn't exists"))
                .getAmountOfMoney();

        var paymentRequest = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/operations/payment")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "accountId": "1",
                            "cash": "100"
                        }
                        """)
                .header("lang", "pl")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        assertThat(paymentRequest.andReturn().getResponse().getContentAsString()).isNotNull()
                .contains("pomyślnie dodane do konta");

        var amountOfMoneyAfter = accountRepository.findById(1L)
                .orElseThrow(() -> new AccountNotFoundException("Account with the given Id doesn't exists"))
                .getAmountOfMoney();
        assertThat(amountOfMoneyAfter).isEqualTo(initialAccountMoney + 100);
    }

    @Test
    void paycheck() throws Exception {
        var token = createUserWithAccountAndReturnAccessToken();

        // to be sure
        var chosenAccount = accountRepository.findById(1L)
                .orElseThrow(() -> new AccountNotFoundException("Account with the given Id doesn't exists"));
        chosenAccount.setAmountOfMoney(chosenAccount.getAmountOfMoney() + 1000);
        accountRepository.save(chosenAccount);


        // make a withdraw in deutsch version
        var initialAccountMoney = accountRepository.findById(1L)
                .orElseThrow(() -> new AccountNotFoundException("Account with the given Id doesn't exists"))
                .getAmountOfMoney();

        var paymentRequest = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/operations/paycheck")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                            "accountId": "1",
                            "cash": "100"
                        }
                        """)
                .header("lang", "de")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        assertThat(paymentRequest.andReturn().getResponse().getContentAsString()).isNotNull()
                .contains("Transaktion erfolgreich");

        var amountOfMoneyAfter = accountRepository.findById(1L)
                .orElseThrow(() -> new AccountNotFoundException("Account with the given Id doesn't exists"))
                .getAmountOfMoney();
        assertThat(amountOfMoneyAfter).isEqualTo(initialAccountMoney - 100);
    }

    private String createUserWithAccountAndReturnAccessToken() throws Exception {
        var token = getEmployeeAccessToken();

        // register user
        if (!isFirstClientWithCardCreated) {
            var registerRequest = mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/auth/register-with-account")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content("""
                         {
                            "login": "remmo1",
                            "password": "1234abcd",
                            "firstName": "Remigiusz",
                            "lastName": "Pisarski",
                            "email": "rpisarski123@gmail.com"
                         }
                        """)
            ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

            var responseBody = registerRequest.andReturn().getResponse().getContentAsString();
            assertThat(responseBody).isNotNull().contains("registered").contains("created");
            isFirstClientWithCardCreated = true;
        }

        assertThat(token).isNotNull();
        return token;
    }

    private String getEmployeeAccessToken() throws Exception {
        var serverResponse = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                         {
                            "login": "employee",
                            "password": "password"
                         }
                        """)
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        return "Bearer " + serverResponse.andReturn().getResponse().getContentAsString();
    }

}