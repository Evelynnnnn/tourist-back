package com.shilei.tourist.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Component
public interface WarningService {
    List<Map<String,String>> getTodayWarningInfoByAddress(String address);

    List<String> getWarningLineY(String address);

    List<String> getWarningLineX(String address);

    void settingOne(Map<String,Object> map);

    void settingAll(int number);
}
