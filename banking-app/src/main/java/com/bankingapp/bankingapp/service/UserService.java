package com.bankingapp.bankingapp.service;


import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final AuthorityRepository authorityRepository;

    private final CardRepository cardRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;


    public User registerUser(RegistrationRequest registrationRequest) {
        var optionalUser = userRepository.findUserByLogin(registrationRequest.getLogin());
        if (optionalUser.isPresent())
            throw new BadCredentialsException("Login already defined in the system");
        else {
            User newUser = User.builder()
                    .userCard(null)
                    .login(registrationRequest.getLogin())
                    .password(encoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .email(registrationRequest.getEmail())
                    // setting active to true, functionality to activate account will be added later
                    .isActive(true)
                    .amountOfMoney((double) 0).build();
            final var authority = authorityRepository.findById(Authority.USER_AUTHORITY.getName())
                    .orElseThrow(() -> new IllegalStateException("Authority not found"));
            newUser.setAuthorities(new HashSet<>(Set.of(authority)));
            User savedUser = userRepository.save(newUser);
            savedUser.setUserCard(Card.builder().user(savedUser).PIN(registrationRequest.getCardPIN()).build());
            return userRepository.save(savedUser);
        }
    }

}
