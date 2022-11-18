package com.bankingapp.bankingapp;

import com.bankingapp.bankingapp.domain.Authority;
import com.bankingapp.bankingapp.domain.User;
import com.bankingapp.bankingapp.repository.AuthorityRepository;
import com.bankingapp.bankingapp.repository.UserRepository;
import com.bankingapp.bankingapp.security.Role;
import com.bankingapp.bankingapp.service.PasswordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import java.util.Set;



@SpringBootApplication
public class BankingAppApplication {


	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);

	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, AuthorityRepository authorityRepository) {
		return args -> {
			authorityRepository.save(Authority.ADMIN_AUTHORITY);
			authorityRepository.save(Authority.EMPLOYEE_AUTHORITY);
			authorityRepository.save(Authority.USER_AUTHORITY);

			User admin = User.builder()
					.login("admin")
					.password("admin")
					.firstName("Admin")
					.lastName("Adminowaty")
					.email("admin@gmail.com")
					.isActive(true)
					.amountOfMoney(0.0)
					.build()
					;
			final var authority = authorityRepository.findById(Role.ADMIN.getAuthority()).get();
			admin.setAuthorities(Set.of(authority));
			userRepository.save(admin);
		};
	}

}
