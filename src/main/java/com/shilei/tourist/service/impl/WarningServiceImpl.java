package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.AddressDao;
import com.shilei.tourist.dao.WarningInfoDao;
import com.shilei.tourist.entity.WarningInfo;
import com.shilei.tourist.service.WarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class WarningServiceImpl implements WarningService {

    @Autowired
    WarningInfoDao warningInfoDao;

    @Autowired
    AddressDao addressDao;

    @Override
    public List<Map<String,String>> getTodayWarningInfoByAddress(String address) {
        if(address.isEmpty() || null == address){
            address = "1号地点";
        }
        Date date = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String,String>> list = new ArrayList<>();
        List<WarningInfo> warningInfoList = warningInfoDao.findAllByAddressAndDate(address,s.format(date));
        for (WarningInfo warningInfo: warningInfoList) {
            Map<String,String> map = new HashMap<>();
            String string = "警告!\u3000\u3000\u3000"+warningInfo.getDatetime()+",在地点"+warningInfo.getAddress()
                    +"中,人数:"+warningInfo.getNumber()
                    +",预警人数:"+warningInfo.getWarningNumber()
                    +",负责人:"+warningInfo.getName()
                    +",联系方式:"+warningInfo.getPhone();
            map.put("name",string);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<String> getWarningLineY(String address) {
        if(address.isEmpty() || null == address){
            address = "1号地点";
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = warningInfoDao.findNumberByAddressAndDate(address,simpleDateFormat.format(date));
        return list;
    }

    @Override
    public List<String> getWarningLineX(String address) {
        if(address.isEmpty() || null == address){
            address = "1号地点";
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = warningInfoDao.findDatetimeByAddressAndDate(address,simpleDateFormat.format(date));
        return list;
    }

    @Override
    public void settingOne(Map<String, Object> map) {
        String number = String.valueOf(map.get("text"));
        String address = String.valueOf(map.get("address"));
        addressDao.updateWarningNumberByAddress(number,address);
    }

    @Override
    public void settingAll(int number) {
        addressDao.updateAllWarningNumber(number);
    }
}
