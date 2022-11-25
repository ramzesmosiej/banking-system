package com.bankingapp.bankingapp.domain;

import com.bankingapp.bankingapp.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Entity
@Getter
@Table(name = "user_authority")
public class Authority {

    public static Authority ADMIN_AUTHORITY = new Authority(Role.ADMIN.getAuthority());

    public static Authority EMPLOYEE_AUTHORITY = new Authority(Role.EMPLOYEE.getAuthority());

    public static Authority USER_AUTHORITY = new Authority(Role.USER.getAuthority());

    @NotNull
    @Id
    private String name;


}
