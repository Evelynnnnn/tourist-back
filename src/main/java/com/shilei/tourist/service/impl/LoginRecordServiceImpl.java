package com.shilei.tourist.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shilei.tourist.dao.LoginRecordDao;
import com.shilei.tourist.entity.LoginRecord;
import com.shilei.tourist.service.LoginRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LoginRecordServiceImpl implements LoginRecordService {

    @Autowired
    LoginRecordDao loginRecordDao;

    @Override
    public void saveLoginRecord(Map<String,Object> loginInfo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoginRecord loginRecord = new LoginRecord();
        Date date = new Date();
        String username = String.valueOf(loginInfo.get("username"));

        Map<String,Object> map = (Map<String, Object>) loginInfo.get("map");
        String ip = String.valueOf(map.get("ip"));

        List<String> list = (List<String>) map.get("data");
        String address = list.get(0)+list.get(1)+list.get(2);

        loginRecord.setAddress(address);
        loginRecord.setIp(ip);
        loginRecord.setLoginTime(simpleDateFormat.format(date));
        loginRecord.setUsername(username);
        log.info("本次登录的用户详细信息{}",loginRecord);
        loginRecordDao.save(loginRecord);
    }

    @Override
    public JSONObject findLoginRecord() {
        List<LoginRecord> loginRecordList = loginRecordDao.findLoginRecordsOrderByLoginTimeDesc();
        JSONObject records = new JSONObject();
        records.put("list",loginRecordList);
        records.put("pageTotal",loginRecordList.size());
        return records;
    }
}
