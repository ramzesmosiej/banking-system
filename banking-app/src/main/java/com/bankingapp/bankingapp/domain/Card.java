package com.bankingapp.bankingapp.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Entity(name = "UserCard")
@Getter
@Setter
@Table(name = "user_card")
public class Card extends DomainObject {

    @NotNull
    @Pattern(regexp = "\\d\\d\\d\\d")
    @Size(min = 4, max = 4)
    private String PIN;

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
