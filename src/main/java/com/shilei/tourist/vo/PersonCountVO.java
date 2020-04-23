package com.shilei.tourist.vo;

import lombok.Data;

@Data
public class PersonCountVO {
    //景点名
    private String placeName;
    //人数
    private Integer personNumber;
    //时间
    private String nowTime;
}
