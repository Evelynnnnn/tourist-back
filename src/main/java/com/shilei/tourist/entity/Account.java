package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "mail")
    private String mail;

    @Column(name = "code")
    private String code;

}
