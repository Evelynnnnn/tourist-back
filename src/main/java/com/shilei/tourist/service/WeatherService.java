package com.shilei.tourist.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public interface WeatherService {

    String getTodayWeather();

    String getTomorrowWeather();
}
