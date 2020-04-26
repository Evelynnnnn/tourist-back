package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.TomorrowWeatherDao;
import com.shilei.tourist.dao.WeatherDao;
import com.shilei.tourist.entity.TomorrowWeather;
import com.shilei.tourist.entity.Weather;
import com.shilei.tourist.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    TomorrowWeatherDao tomorrowWeatherDao;

    @Override
    public String getTodayWeather() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(date);
        Weather weather = weatherDao.findByDate(today);
        String weatherInfo;
        if(null == weather){
            weatherInfo = "查看时间过早,请于08:00后查看!";
        }else {
            weatherInfo = "今天是" + today
                    + ",\t最高温度:" + weather.getMax()
                    + "摄氏度,\t最低温度为" + weather.getMin()
                    + "摄氏度,\t白日天气:" + weather.getTxtN()
                    + ",\t夜间天气:" + weather.getTxtN()
                    + ",\t风力等级:" + weather.getSc()
                    + ",\t风向:" + weather.getDir()
                    + ",\t降水量:" + weather.getPcpn()
                    + ",\t相对湿度:" + weather.getHum()
                    + ",\t紫外线指数:" + weather.getUv()
                    + ",\t能见度:" + weather.getVis()
                    + ",\t大气压:" + weather.getPres() + "。";
        }
        log.info("今日天气详情为{}",weatherInfo);
        return weatherInfo;
    }

    @Override
    public String getTomorrowWeather() {
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = formatter.format(date);
        TomorrowWeather weather = tomorrowWeatherDao.findByDate(tomorrow);
        String weatherInfo;
        if(null == weather){
            weatherInfo = "查看时间过早,请于08:00后查看!";
        }else {
            weatherInfo = "明天是" + tomorrow
                    + ",\t最高温度:" + weather.getMax()
                    + "摄氏度,\t最低温度为" + weather.getMin()
                    + "摄氏度,\t白日天气:" + weather.getTxtN()
                    + ",\t夜间天气:" + weather.getTxtN()
                    + ",\t风力等级:" + weather.getSc()
                    + ",\t风向:" + weather.getDir()
                    + ",\t降水量:" + weather.getPcpn()
                    + ",\t相对湿度:" + weather.getHum()
                    + ",\t紫外线指数:" + weather.getUv()
                    + ",\t能见度:" + weather.getVis()
                    + ",\t大气压:" + weather.getPres() + "。";
        }
        log.info("今日天气详情为{}",weatherInfo);
        return weatherInfo;
    }
}
