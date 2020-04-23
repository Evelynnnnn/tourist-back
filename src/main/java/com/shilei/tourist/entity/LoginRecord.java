package com.shilei.tourist.entity;


import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "login_record")
@Data
public class LoginRecord {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "login_time")
    private String loginTime;

    @Column(name = "ip")
    private String ip;

    @Column(name = "address")
    private String address;

}
