package com.shilei.tourist;

import com.google.gson.Gson;
import com.shilei.tourist.filter.HttpUtil;
import com.shilei.tourist.utils.AccessToken;
import com.shilei.tourist.utils.Base64Util;
import com.shilei.tourist.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class test {
    @Test
    void test(){
            try {
                copy("D://1.jpg","D://person//1.jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 请求url
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_num";
            try {
                // 本地文件路径
                String filePath = "D://person//1.jpg";
                byte[] imgData = FileUtil.readFileByBytes(filePath);
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");

                String param = "image="+imgParam+"show=true";

                // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                AccessToken accessTokens = new AccessToken();
                String accessToken = accessTokens.getAuth();

                String result = HttpUtil.post(url, accessToken, param);
                System.out.println(result);
                Gson gson = new Gson();
                Map<String, Object> map = new HashMap<String, Object>();
                map = gson.fromJson(result, map.getClass());
                String img = Base64Util.encode((byte[]) map.get("image"));
                System.out.println(img);
                double goodsid=(double) map.get("person_num");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    static void copy(String inFile, String outFile) throws IOException {
        InputStream in = new FileInputStream(inFile);
        byte[] b = new byte[in.available()];
        in.read(b);
        OutputStream out = new FileOutputStream(outFile);
        out.write(b);
        in.close();
        out.close();
    }

    @Test
    void a(){
        int a = 122;
        char b = (char) a;
        System.out.println(b);
    }

}
