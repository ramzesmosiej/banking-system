package com.bankingapp.bankingapp.config;

import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;

public class BankAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public BankAuthenticationProvider(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());

        User userToAuthenticate = userRepository.findUserByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("login not found in database"));

        Collection<SimpleGrantedAuthority> authorities = userToAuthenticate.getGrantedAuthorities();
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
