package com.shilei.tourist.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shilei.tourist.dao.TomorrowWeatherDao;
import com.shilei.tourist.dao.WeatherDao;
import com.shilei.tourist.entity.TomorrowWeather;
import com.shilei.tourist.entity.Weather;
import com.wxapi.WxApiCall.WxApiCall;
import com.wxapi.model.RequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class SaveWeather {

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    TomorrowWeatherDao tomorrowWeatherDao;

    @Scheduled(cron = "0 0 21 * * ?")
    void saveWeather(){
        RequestModel model = new RequestModel();
        model.setGwUrl("https://way.jd.com/he/freeweather");
        model.setAppkey("4c489bad90e74ec8b3b33b5ec2d9da4b");
        Map queryMap = new HashMap();
        queryMap.put("city","苏州"); //访问参数
        model.setQueryParams(queryMap);
        WxApiCall call = new WxApiCall();
        call.setModel(model);
        call.request();
        JSONObject jsonObject = JSONObject.parseObject(call.request());
        log.info("获取到的call为{}",call.request());
        JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));

        JSONArray jsonArray = JSONArray.parseArray(result.getString("HeWeather5"));

        JSONObject content = JSONObject.parseObject(jsonArray.get(0).toString());

        JSONArray dailyForest = JSONArray.parseArray(content.getString("daily_forecast"));

        JSONObject today = JSONObject.parseObject(dailyForest.get(0).toString());
        log.info("今天的天气为{}", today);
        Weather weather = new Weather();
        String date = today.getString("date");
        weather.setDate(date);

        String hum = today.getString("hum");
        weather.setHum(hum);

        String uv = today.getString("uv");
        weather.setUv(uv);

        String vis = today.getString("vis");
        weather.setVis(vis);

        String pcpn = today.getString("pcpn");
        weather.setPcpn(pcpn);

        String pres = today.getString("pres");
        weather.setPres(pres);

        JSONObject tmp = JSONObject.parseObject(today.getString("tmp"));
        String max = tmp.getString("max");
        String min = tmp.getString("min");
        weather.setMax(max);
        weather.setMin(min);

        JSONObject cond = JSONObject.parseObject(today.getString("cond"));
        String txt_n = cond.getString("txt_n");
        String txt_d = cond.getString("txt_d");
        weather.setTxtN(txt_n);
        weather.setTxtD(txt_d);

        JSONObject wind = JSONObject.parseObject(today.getString("wind"));
        String sc = wind.getString("sc");
        String dir = wind.getString("dir");
        weather.setSc(sc);
        weather.setDir(dir);
        log.info("实体类weather的内容有{}",weather);
        weatherDao.save(weather);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    void saveTomorrowWeather(){
        RequestModel model = new RequestModel();
        model.setGwUrl("https://way.jd.com/he/freeweather");
        model.setAppkey("4c489bad90e74ec8b3b33b5ec2d9da4b");
        Map queryMap = new HashMap();
        queryMap.put("city","苏州"); //访问参数
        model.setQueryParams(queryMap);
        WxApiCall call = new WxApiCall();
        call.setModel(model);
        call.request();
        JSONObject jsonObject = JSONObject.parseObject(call.request());
        log.info("获取到的call为{}",call.request());
        JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));

        JSONArray jsonArray = JSONArray.parseArray(result.getString("HeWeather5"));

        JSONObject content = JSONObject.parseObject(jsonArray.get(0).toString());

        JSONArray dailyForest = JSONArray.parseArray(content.getString("daily_forecast"));

        JSONObject today = JSONObject.parseObject(dailyForest.get(1).toString());
        log.info("明日的天气为{}", today);
        TomorrowWeather weather = new TomorrowWeather();

        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrow = formatter.format(date);

        weather.setDate(tomorrow);

        String hum = today.getString("hum");
        weather.setHum(hum);

        String uv = today.getString("uv");
        weather.setUv(uv);

        String vis = today.getString("vis");
        weather.setVis(vis);

        String pcpn = today.getString("pcpn");
        weather.setPcpn(pcpn);

        String pres = today.getString("pres");
        weather.setPres(pres);

        JSONObject tmp = JSONObject.parseObject(today.getString("tmp"));
        String max = tmp.getString("max");
        String min = tmp.getString("min");
        weather.setMax(max);
        weather.setMin(min);

        JSONObject cond = JSONObject.parseObject(today.getString("cond"));
        String txt_n = cond.getString("txt_n");
        String txt_d = cond.getString("txt_d");
        weather.setTxtN(txt_n);
        weather.setTxtD(txt_d);

        JSONObject wind = JSONObject.parseObject(today.getString("wind"));
        String sc = wind.getString("sc");
        String dir = wind.getString("dir");
        weather.setSc(sc);
        weather.setDir(dir);
        log.info("实体类tomorrowWeather的内容有{}",weather);
        tomorrowWeatherDao.save(weather);
    }
}
