package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "person_number")
@Data
public class PersonNumber {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private Integer number;

    @Column(name = "time")
    private Date nowTime;

    @Column(name = "date")
    private String date;

    @Column(name = "hour")
    private String hour;
}
