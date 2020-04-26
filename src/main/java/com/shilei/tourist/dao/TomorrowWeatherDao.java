package com.shilei.tourist.dao;

import com.shilei.tourist.entity.TomorrowWeather;
import com.shilei.tourist.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TomorrowWeatherDao extends JpaRepository<TomorrowWeather,Integer> {
    TomorrowWeather findByDate(String date);
}
