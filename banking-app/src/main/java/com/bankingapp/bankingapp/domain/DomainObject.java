package com.bankingapp.bankingapp.domain;

import javax.persistence.*;

/**
 * abstract class used for creating entities with automatically generated ID
 */
@MappedSuperclass
public abstract class DomainObject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
