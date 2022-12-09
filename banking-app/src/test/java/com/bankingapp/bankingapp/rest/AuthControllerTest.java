package com.bankingapp.bankingapp.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerUser() throws Exception {
        var token = getEmployeeAccessToken();

        var registerRequest = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/register-with-account")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                         {
                            "login": "remmo2",
                            "password": "1234abcd",
                            "firstName": "Remigiusz",
                            "lastName": "Pisarski",
                            "email": "rpisarski123@gmail.com"
                         }
                        """)
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        var responseBody = registerRequest.andReturn().getResponse().getContentAsString();
        assertThat(responseBody).isNotNull().contains("registered");
    }

    @Test
    void registerUserWithAccount() throws Exception {
        var token = getEmployeeAccessToken();

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
    }

    @Test
    void login() throws Exception {
        var token = getEmployeeAccessToken();
        assertThat(token).hasSize(218);
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