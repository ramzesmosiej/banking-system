package com.bankingapp.bankingapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "AppUser")
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card userCard;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        return getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
    }
}
