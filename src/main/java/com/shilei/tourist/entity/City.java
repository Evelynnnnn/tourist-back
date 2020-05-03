package com.shilei.tourist.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "city")
@Data
public class City {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private Integer type;

}
