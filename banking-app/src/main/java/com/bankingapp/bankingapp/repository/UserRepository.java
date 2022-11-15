package com.bankingapp.bankingapp.repository;

import com.bankingapp.bankingapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
