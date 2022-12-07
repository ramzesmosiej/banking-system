package com.bankingapp.bankingapp;

import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AccountRepository;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@AllArgsConstructor
@SpringBootApplication
public class BankingAppApplication {

	AccountRepository accountRepository;
	AuthorityRepository authorityRepository;
	CardRepository cardRepository;
	UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {

		return args -> {

			authorityRepository.save(Authority.ADMIN_AUTHORITY);
			authorityRepository.save(Authority.EMPLOYEE_AUTHORITY);
			authorityRepository.save(Authority.USER_AUTHORITY);

			// User creation
			User admin = User.builder()
					.login("admin")
					.password(passwordEncoder.encode("12w1w21w1g723dg3*H@dJ(@D"))
					.firstName("Admin")
					.lastName("Adminowaty")
					.email("admin@gmail.com")
					.isActive(true)
					.build();

			final var authorityAdmin = authorityRepository.findById(Role.ADMIN.getAuthority()).
					orElseThrow(() -> new IllegalStateException("Authority not found!"));
			final var authorityUser = authorityRepository.findById(Role.USER.getAuthority()).
					orElseThrow(() -> new IllegalStateException("Authority not found!"));

			admin.setAuthorities(Set.of(authorityAdmin, authorityUser));
			userRepository.save(admin);

		};
	}

}
