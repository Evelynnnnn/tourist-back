package com.shilei.tourist.utils;/*本demo未经测试仅供参考*/

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shilei.tourist.utils.HttpsUtil.doGet;

@Slf4j
public class HolidayUtil {

    public static final String URL = "https://tool.bitefu.net/jiari";

    public static String Get(String day) {
        String url = URL + "?d=" + day+"&info=1";
        JSONObject jsonObject = null;
        try {
            String info = doGet(url);
            Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
            Matcher matcher = pattern.matcher(info);
            char ch;
            while (matcher.find()) {
                ch = (char) Integer.parseInt(matcher.group(2), 16);
                info = info.replace(matcher.group(1), ch + "");
            }
            jsonObject = JSONObject.parseObject(info);
            log.info("通过节假日API获得的数据为{}",jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(jsonObject.get("typename"));
    }

}