package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "everyday_avg")
@Data
public class EveryDayAvg {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "number")
    private int number;

    @Column(name = "address")
    private String address;

    @Column(name = "date")
    private String date;
}
