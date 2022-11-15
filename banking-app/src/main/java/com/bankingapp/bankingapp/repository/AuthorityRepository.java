package com.bankingapp.bankingapp.repository;

import com.bankingapp.bankingapp.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
