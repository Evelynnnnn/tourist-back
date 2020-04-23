package com.shilei.tourist.utils;

import com.google.gson.Gson;
import com.shilei.tourist.filter.HttpUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 人流量统计
 */
public class Sample{

    public static Integer body_num() {

        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/body_num";
        try {
            // 本地文件路径
            String filePath = "D://person//1.jpg";
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam+"show="+true;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            AccessToken accessTokens = new AccessToken();
            String accessToken = accessTokens.getAuth();
            System.out.println(accessToken);

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println(result);
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<String, Object>();
            map = gson.fromJson(result, map.getClass());
            double goodsid=(double) map.get("person_num");
            return (int)goodsid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        body_num();
//        Sample sample = new Sample();
//        //File file = new File("D://test.jpg");
////        if (file.exists() && file.length() != 0 && file.canRead()&&file.isFile()){
//
////        }
//        String inFile1 = "D://test.jpg";
//        String outFile1 = "D://what.jpg";
//        String outFile2 = "D://count//1.jpg";
//        File file = new File(outFile1);
//        while (true){
//            copy(inFile1,outFile1);
//            if (file.length() != 0){
//                copy(outFile1,outFile2);
//            }
//        }
//    }
//
//    static void copy(String inFile,String outFile) throws IOException {
//        InputStream in=new FileInputStream(inFile);
//        byte[] b=new byte[in.available()];
//        in.read(b);
//        OutputStream out=new FileOutputStream(outFile);
//        out.write(b);
//        in.close();
//        out.close();
    }
}