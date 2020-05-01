package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "warning_info")
@Data
public class WarningInfo {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "number")
    private int number;

    @Column(name = "date")
    private String date;

    @Column(name = "datetime")
    private String datetime;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "warning_number")
    private int warningNumber;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "time")
    private String time;
}
