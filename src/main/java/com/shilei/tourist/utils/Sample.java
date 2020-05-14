package com.shilei.tourist.utils;

import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.google.gson.Gson;
import com.shilei.tourist.filter.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人流量统计
 */
@Slf4j
public class Sample{

    public static int body_num(){

        try {
            copy("D://1.jpg","D://person//1.jpg");
        } catch (IOException e) {
            log.info("");
            e.printStackTrace();
        }

        AipBodyAnalysis client = null;
        //准备url
        String url = GetPropertiesUtil.getUrl("BaiduAPI");
        //选择请求方式
        HttpPost httppost = new HttpPost(url);
        //准备参数
        List<BasicNameValuePair> para=new ArrayList<BasicNameValuePair>();
        AccessToken accessTokens = new AccessToken();
        String accessToken = accessTokens.getAuth();
        BasicNameValuePair access_token = new BasicNameValuePair("access_token",accessToken);
        BasicNameValuePair Content_Type = new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded");
        BasicNameValuePair show = new BasicNameValuePair("show", "true");
        BasicNameValuePair image = new BasicNameValuePair("image",getImgFileToBase64("D://person//1.jpg"));
        para.add(access_token);
        para.add(Content_Type);
        para.add(show);
        para.add(image);
        int p = 0;
        try {//把参数封装到请求体里面
            httppost.setEntity(new UrlEncodedFormEntity(para,"UTF-8"));
            //准备客户端
            HttpClient httpclient = HttpClients.createDefault();
            //发送请求，接受响应信息
            HttpResponse respones=httpclient.execute(httppost);
            //取出接口响应数据,并转成字符串
            String result=  EntityUtils.toString(respones.getEntity());
            JSONObject jsonObject = JSONObject.parseObject(result);
            String jpg = String.valueOf(jsonObject.get("image"));
            GenerateImage(jpg,GetPropertiesUtil.getApi("ApiLoadPicture"));
            p = (int) jsonObject.get("person_num");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    public static String getImgFileToBase64(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream inputStream = null;
        byte[] buffer = null;
        //读取图片字节数组
        try {
            inputStream = new FileInputStream(imgFile);
            int count = 0;
            while (count == 0) {
                count = inputStream.available();
            }
            buffer = new byte[count];
            inputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    // 关闭inputStream流
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对字节数组Base64编码
        return new BASE64Encoder().encode(buffer);
    }

    public static boolean GenerateImage(String imgData, String imgFilePath) throws IOException { // 对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            out = new FileOutputStream(imgFilePath);
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            out.write(b);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
            return true;
        }
    }

//    public static Integer body_num() {
//
//
//        // 请求url
//        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_num";
//        try {
//            // 本地文件路径
//            String filePath = "D://person//1.jpg";
//            byte[] imgData = FileUtil.readFileByBytes(filePath);
//            String imgStr = Base64Util.encode(imgData);
//            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
//
//            String param = "image=" + imgParam+"show="+true;
//
//            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
//            AccessToken accessTokens = new AccessToken();
//            String accessToken = accessTokens.getAuth();
//            System.out.println(accessToken);
//
//            String result = HttpUtil.post(url, accessToken, param);
//            Gson gson = new Gson();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map = gson.fromJson(result, map.getClass());
//            double goodsid=(double) map.get("person_num");
//            System.out.println(map.get("image"));
//            return (int)goodsid;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void main(String[] args) throws InterruptedException, IOException {
        body_num();
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
}