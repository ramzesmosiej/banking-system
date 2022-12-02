package com.bankingapp.bankingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@AllArgsConstructor
@Builder
@Entity(name = "UserAccount")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "user_account")
public class Account extends DomainObject {
    private double amountOfMoney;

    @JsonIgnore
    @OneToOne(mappedBy = "userAccount")
    private User user;
}
