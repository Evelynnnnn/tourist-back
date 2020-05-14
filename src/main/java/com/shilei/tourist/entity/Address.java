package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "person_num")
    private String personNum;

    @Column(name = "warning_number")
    private int warningNumber;
}
