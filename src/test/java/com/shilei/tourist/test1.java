package com.shilei.tourist;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shilei.tourist.dao.TomorrowWeatherDao;
import com.shilei.tourist.dao.WeatherDao;
import com.shilei.tourist.entity.TomorrowWeather;
import com.shilei.tourist.entity.Weather;
import com.wxapi.WxApiCall.WxApiCall;
import com.wxapi.model.RequestModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

public class test1 {

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    TomorrowWeatherDao tomorrowWeatherDao;

    public static void main(String[] args) {
        for (int i = 0; i < 1000000000; i++) {
            String result = main(String.valueOf(i));
            System.out.println(i);
            if (!result.contains("\\u5bc6\\u7801\\u4e0d\\u6b63\\u786e")){
                break;
            }
        }
    }

     static String main(String pwd) {
        OutputStreamWriter out = null ;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        String url = "https://lanzous.com/filemoreajax.php";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求头属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行   否则会抛异常（java.net.ProtocolException: cannot write to a URLConnection if doOutput=false - call setDoOutput(true)）
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流并开始发送参数
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            //添加参数
            out.write("&lx="+"2");
            out.write("&fid="+"1628193");
            out.write("&uid="+"358715");
            out.write("&pg="+"1");
            out.write("&rep="+"0");
            out.write("&t="+"1589275196");
            out.write("&k="+"83984864cc093d515a4820a6912353c0");
            out.write("&up="+"1");
            out.write("&vip="+"0");
            out.write("&ls="+"1");
            out.write("&pwd="+pwd);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

    @Test
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

        JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));

        JSONArray jsonArray = JSONArray.parseArray(result.getString("HeWeather5"));

        JSONObject content = JSONObject.parseObject(jsonArray.get(0).toString());

        JSONArray dailyForest = JSONArray.parseArray(content.getString("daily_forecast"));

        JSONObject today = JSONObject.parseObject(dailyForest.get(0).toString());

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

        weatherDao.save(weather);
    }

    @Test
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

        JSONObject result = JSONObject.parseObject(jsonObject.getString("result"));

        JSONArray jsonArray = JSONArray.parseArray(result.getString("HeWeather5"));

        JSONObject content = JSONObject.parseObject(jsonArray.get(0).toString());

        JSONArray dailyForest = JSONArray.parseArray(content.getString("daily_forecast"));

        JSONObject today = JSONObject.parseObject(dailyForest.get(1).toString());

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

        tomorrowWeatherDao.save(weather);
    }

}