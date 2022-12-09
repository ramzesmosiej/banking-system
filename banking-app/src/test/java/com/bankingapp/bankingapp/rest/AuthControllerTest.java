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

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerUser() {
    }

    @Test
    void registerUserWithAccount() {
    }

    @Test
    void login() throws Exception {
        var token = getEmployeeAccessToken();
        assertThat(token).hasSize(211);
    }

    @Test
    void pingAdmin() {
    }

    @Test
    void ping() {
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
        return serverResponse.andReturn().getResponse().getContentAsString();
    }

}