package com.shilei.tourist.controller;

import com.shilei.tourist.entity.City;
import com.shilei.tourist.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Component
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    CityService cityService;

    @GetMapping("/allProvince")
    public List<Map<String,String>> findAllProvince() {
        return cityService.findAllProvince();
    }

    @GetMapping("/getCity")
    public List<Map<String,String>> findCityByName(@RequestParam String name) {
        return cityService.findCityByName(name);
    }


}
