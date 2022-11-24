package com.bankingapp.bankingapp.config;

import com.bankingapp.bankingapp.security.Role;
import com.bankingapp.bankingapp.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private static final String[] PERMITTED = {
            "/api/auth/register",
            "api/auth/login",
            "/console",
            "/api/operations/payment",
            "/api/operations/paycheck",
            "/machine/auth/card",
            "/machine/add/cash"
    };

    private final BankAuthenticationProvider bankAuthenticationProvider;

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(BankAuthenticationProvider bankAuthenticationProvider, JwtTokenFilter jwtTokenFilter) {
        this.bankAuthenticationProvider = bankAuthenticationProvider;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().and()
                .csrf().disable().headers().frameOptions().disable().and()
                .authorizeHttpRequests()
                .antMatchers(PERMITTED).permitAll().antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/auth/ping/admin").hasAuthority(Role.ADMIN.getAuthority())
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.eraseCredentials(false)
                .authenticationProvider(bankAuthenticationProvider);
        return builder.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
