package com.bankingapp.bankingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "cards")
public class Card {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d\\d\\d\\d")
    @Size(min = 4, max = 4)
    private String PIN;

    /*@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private User user;*/

    @OneToOne(mappedBy = "card")
    private Account account;

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", PIN='" + PIN + '\'' +
                '}';
    }

}
