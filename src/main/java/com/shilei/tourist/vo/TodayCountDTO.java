package com.shilei.tourist.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class TodayCountDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;

    private String address;
}
