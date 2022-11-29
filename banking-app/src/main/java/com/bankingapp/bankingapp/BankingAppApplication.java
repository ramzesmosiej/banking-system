package com.bankingapp.bankingapp;

import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.domain.Card;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.CardRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;



@SpringBootApplication
public class BankingAppApplication {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, AuthorityRepository authorityRepository, CardRepository cardRepository) {

		return args -> {

			authorityRepository.save(Authority.ADMIN_AUTHORITY);
			authorityRepository.save(Authority.EMPLOYEE_AUTHORITY);
			authorityRepository.save(Authority.USER_AUTHORITY);


			User admin = User.builder()
					.login("admin")
					.password(passwordEncoder.encode("admin"))
					.firstName("Admin")
					.lastName("Adminowaty")
					.email("admin@gmail.com")
					.isActive(true)
					.amountOfMoney(0.0)
					.build();


			final var authorityAdmin = authorityRepository.findById(Role.ADMIN.getAuthority()).get();
			final var authorityUser = authorityRepository.findById(Role.USER.getAuthority()).get();
			admin.setAuthorities(Set.of(authorityAdmin, authorityUser));
			User adminSaved = userRepository.save(admin);
			adminSaved.setUserCard(Card.builder().user(adminSaved).PIN("1234").build());
			userRepository.save(adminSaved);

		};
	}

}
