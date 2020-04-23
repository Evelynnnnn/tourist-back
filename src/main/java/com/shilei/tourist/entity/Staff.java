package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "staff")
@Data
public class Staff {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "entry_date")
    private String entryDate;

    @Column(name = "name")
    private String name;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "mail")
    private String mail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "job")
    private String job;

    @Column(name = "state")
    private String state;

    @Column(name = "duty_address")
    private String dutyAddress;
}
