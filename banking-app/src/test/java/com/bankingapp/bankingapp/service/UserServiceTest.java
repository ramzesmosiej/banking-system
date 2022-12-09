package com.bankingapp.bankingapp.service;

import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.exceptions.UserAlreadyExists;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.bankingapp.bankingapp.TestConsts.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    /*@Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    
    @Test
    void registerUser_userAlreadyExists() {
        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.of(VALID_USER_WITHOUT_CARD));
        assertThatThrownBy(() -> userService.registerUser(VALID_USER_REQUEST()))
                .isInstanceOf(UserAlreadyExists.class)
                .hasMessageContaining("Login already defined");
    }

    @Test
    void registerUser_authorityNotFound() {
        when(authorityRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.registerUser(VALID_USER_REQUEST()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authority not found");
    }

    @Test
    void registerUser_validData() {
        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.empty());
        when(authorityRepository.findById(anyString())).thenReturn(Optional.of(Authority.USER_AUTHORITY));
        when(userRepository.save(any())).thenReturn(VALID_USER_WITHOUT_CARD);

        var savedUser = userService.registerUser(VALID_USER_REQUEST());
        assertThat(savedUser.getLastName()).isEqualTo("Nowak");
    }
    */
}