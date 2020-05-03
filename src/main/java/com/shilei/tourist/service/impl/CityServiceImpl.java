package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.CityDao;
import com.shilei.tourist.entity.City;
import com.shilei.tourist.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CityServiceImpl implements CityService {

    @Autowired
    CityDao cityDao;

    @Override
    public List<Map<String,String>> findAllProvince() {
        List<String> list = cityDao.findAllProvince();
        List<Map<String,String>> province = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> map = new HashMap<>();
            map.put("value","选项"+i);
            map.put("label",list.get(i));
            province.add(map);
        }
        return province;
    }

    @Override
    public List<String> findCityByParentId(Integer id) {
        return cityDao.findCityByParentId(id);
    }

    @Override
    public List<Map<String,String>> findCityByName(String name) {
        City city = cityDao.findCityByName(getURLDecoderString(name));
        List<String> list = cityDao.findCityByParentId(city.getId());
        List<Map<String,String>> citys = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String,String> map = new HashMap<>();
            map.put("value","选项"+i);
            map.put("label",list.get(i));
            citys.add(map);
        }
        return citys;
    }


    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
