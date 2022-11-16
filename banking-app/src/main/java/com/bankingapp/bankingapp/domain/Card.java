package com.bankingapp.bankingapp.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Builder
@Entity(name = "UserCard")
@Getter
@Setter
@Table(name = "user_card")
public class Card extends DomainObject {

    @NotNull
    @Positive
    @Size(min = 4, max = 4)
    private Integer PIN;

    @OneToOne(mappedBy = "userCard")
    private User user;

}
