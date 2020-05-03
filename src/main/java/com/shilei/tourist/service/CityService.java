package com.shilei.tourist.service;


import com.shilei.tourist.entity.City;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Component
public interface CityService {

    List<Map<String,String>> findAllProvince();

    List<String> findCityByParentId(Integer id);

    List<Map<String,String>> findCityByName(String name);
}
