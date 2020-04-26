package com.shilei.tourist.controller;

import com.shilei.tourist.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("/todayWeather")
    String getTodayWeather(){
        return weatherService.getTodayWeather();
    }

    @GetMapping("/tomorrowWeather")
    String getTomorrowWeather(){
        return weatherService.getTomorrowWeather();
    }

}
