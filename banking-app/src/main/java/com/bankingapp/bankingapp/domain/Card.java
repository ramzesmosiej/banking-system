package com.bankingapp.bankingapp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity(name = "UserCard")
@Table(name = "user_card")
public class Card extends DomainObject {

    @NotNull
    @Positive
    @Size(min = 4, max = 4)
    private Integer PIN;

    @OneToOne(mappedBy = "userCard")
    private User user;

}
