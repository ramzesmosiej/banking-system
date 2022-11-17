package com.bankingapp.bankingapp.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Builder
@Entity(name = "UserCard")
@Getter
@Setter
@Table(name = "user_card")
public class Card extends DomainObject {

    @NotNull
    @Min(0)
    @Max(9999)
    private Integer PIN;

    @OneToOne(mappedBy = "userCard")
    private User user;

    @Override
    public String toString() {
        return "Card{" +
                "PIN=" + PIN +
                ", user=" + user +
                '}';
    }
}
