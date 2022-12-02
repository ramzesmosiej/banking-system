package com.bankingapp.bankingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

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
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @ManyToOne()
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

}
