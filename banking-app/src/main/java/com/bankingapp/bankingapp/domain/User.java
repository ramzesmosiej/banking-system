package com.bankingapp.bankingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Entity(name = "AppUser")
@Getter
@NoArgsConstructor
@Setter
@Table(name = "app_user")
public class User extends DomainObject {

    @NotNull
    @Column(unique = true)
    private String login;

    @JsonIgnore
    @Column(name = "password_hash")
    private String password;

    @Size(max = 40)
    private String firstName;

    @Size(max = 40)
    private String lastName;

    @Email
    @NotNull
    private String email;

    @ManyToMany
    @JoinTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "name")
    )
    private Set<Authority> authorities = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card userCard;

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                ", userCard=" + userCard +
                '}';
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        return getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
    }
}
