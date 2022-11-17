package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.bankingapp.bankingapp.service.TestConsts.VALID_USER_WITH_VALID_CARD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class UserAccountServiceTest {

    @Mock
    UserRepository userRepository;

    @MockBean
    UserAccountService userAccountService;

    @Test
    void addCashToUser() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(VALID_USER_WITH_VALID_CARD));

        userAccountService.addCashToUser(2L, 1000.0);
    }

}