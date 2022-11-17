package com.bankingapp.bankingapp.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * abstract class used for creating entities with automatically generated ID
 */
@Getter
@MappedSuperclass
@Setter
public abstract class DomainObject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
