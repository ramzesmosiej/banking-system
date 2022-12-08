package com.bankingapp.bankingapp.config;

import com.bankingapp.bankingapp.exceptions.UserNotActivatedException;
import com.bankingapp.bankingapp.exceptions.UserNotFoundException;
import com.bankingapp.bankingapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@AllArgsConstructor
@Service
public class BankAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        var userToAuthenticate = userRepository.findUserByLogin(login).orElseThrow(
                () -> new UserNotFoundException("Login doesn't exists in DB!")
        );

        Collection<SimpleGrantedAuthority> authorities = userToAuthenticate.getGrantedAuthorities();
        if (Boolean.FALSE.equals(userToAuthenticate.getIsActive())) {
            throw new UserNotActivatedException("User " + userToAuthenticate.getLogin() + " was not activated");
        }
        if (encoder.matches(password, userToAuthenticate.getPassword())) {
            return new UsernamePasswordAuthenticationToken(login, password, authorities);
        } else {
            throw new BadCredentialsException("error");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
