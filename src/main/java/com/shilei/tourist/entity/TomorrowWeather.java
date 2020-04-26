package com.shilei.tourist.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tomorrow_weather")
public class TomorrowWeather {
    @Id
    @GeneratedValue
    private Integer id;

    //相对湿度
    @Column(name = "hum")
    private String hum;

    @Column(name = "date")
    private String date;

    //紫外线指数
    @Column(name = "uv")
    private String uv;

    //能见度
    @Column(name = "vis")
    private String vis;

    //大气压
    @Column(name = "pres")
    private String pres;

    //降水量
    @Column(name = "pcpn")
    private String pcpn;

    //最低温度
    @Column(name = "min")
    private String min;

    //最高温度
    @Column(name = "max")
    private String max;

    //夜间天气
    @Column(name = "txt_n")
    private String txtN;

    //白天天气
    @Column(name = "txt_d")
    private String txtD;

    //风力等级
    @Column(name = "sc")
    private String sc;

    //风向
    @Column(name = "dir")
    private String dir;

}
