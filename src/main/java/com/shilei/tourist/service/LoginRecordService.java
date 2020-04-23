package com.shilei.tourist.service;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface LoginRecordService {
    void saveLoginRecord(Map<String, Object> loginInfo);

    JSONObject findLoginRecord();
}
