package com.bankingapp.bankingapp.domain;

import com.bankingapp.bankingapp.security.Role;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_authority")
public class Authority {

    public static Authority ADMIN_AUTHORITY = new Authority(Role.ADMIN.getAuthority());

    public static Authority EMPLOYEE_AUTHORITY = new Authority(Role.EMPLOYEE.getAuthority());

    public static Authority USER_AUTHORITY = new Authority(Role.USER.getAuthority());

    @NotNull
    @Id
    private String name;


    public Authority() {

    }

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
