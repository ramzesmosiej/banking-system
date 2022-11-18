package com.bankingapp.bankingapp.service;


import com.bankingapp.bankingapp.DTO.RegistrationRequest;
import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
    }


    public User registerUser(RegistrationRequest registrationRequest) {
        Optional<User> optionalUser = userRepository.findUserByLogin(registrationRequest.getLogin());
        if (optionalUser.isPresent()) {
            throw new BadCredentialsException("Login already defined in the system");
        }
        else {
            User newUser = User.builder()
                    .userCard(null)
                    .login(registrationRequest.getLogin())
                    .password(encoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName()).build();
            final var authority = authorityRepository.findById(Authority.USER_AUTHORITY.getName()).get();
            newUser.setAuthorities(new HashSet<>(Set.of(authority)));
            return userRepository.save(newUser);
        }
    }
}
