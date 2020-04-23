package com.shilei.tourist.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @todo ip工具类
 * @author yanan
 * @date 2018年11月12日
 */
public class IpUtil {

    /**
     * @todo 获取外网ip所在区域
     * @param
     * @date 2018年11月12日
     * @author yanan
     */
    public static String getIpArea(String ip) throws Exception {
        // 淘宝的ip定位
        JSONObject json = readJsonFromUrl("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
        if (json.has("code")) {
            if ((json.get("code").toString().trim()).equalsIgnoreCase("0")) {
                String strIp = ((JSONObject) json.get("data")).get("city").toString();
                return strIp;
            } else {
                return "郑州";
            }
        } else {
            return "郑州";
        }
    }
    /**
     * @todo 百度api ip接口
     * @param
     * @date 2018年11月9日
     * @author yanan
     */
    public static JSONObject readJsonFromUrl(String url) throws Exception, JSONException {
        InputStream is = null;
        try {
            URL ul = new URL(url); // 创建 URL
            HttpURLConnection urlCon = (HttpURLConnection) ul.openConnection();
            urlCon.setConnectTimeout(2000);
            urlCon.setReadTimeout(2000);
            // Long b = System.currentTimeMillis();
            is = urlCon.getInputStream(); // 打开到这个URL的流
            // System.out.println("获取百度api
            // ip接口:="+(System.currentTimeMillis()-b));
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("status", "1");
            // System.out.println("new jsonObj status=0,new jsonObj status=0new
            // jsonObj status=0new jsonObj status=0");
            return jsonObj;
        } finally {
            if (null != is)
                is.close();
        }
    }
    /**
     * @todo 将输入流转换成字符串
     * @param
     * @date 2018年11月9日
     * @author yanan
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    /**
     * @todo  获取外网ip
     * @param
     * @date 2018年11月12日
     * @author yanan
     * @throws Exception
     */
    public static String getOuterNetIp(final HttpServletRequest request) throws Exception {
        String ipAddr = getIpAddr(request);//获取ip地址
        boolean internalIp = internalIp(ipAddr);//判断ip是否内网ip
        if(!internalIp){//外网地址直接返回
            return ipAddr;
        }
        String result = "";
        URLConnection connection;
        BufferedReader in = null;
        try {
            URL url = new URL("http://www.icanhazip.com");
            connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "KeepAlive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }
    /**
     * @todo 判断是否内网ip
     * @param
     * @date 2018年11月12日
     * @author yanan
     */
    public static boolean internalIp(String ip) {
        if ("127.0.0.1".equalsIgnoreCase(ip))
            return true;
        if ("0:0:0:0:0:0:0:1".equals(ip))
            return true;
        byte[] addr = IPAddressUtil.textToNumericFormatV4(ip);
        return internalIp(addr);
    }
    /**
     * @todo 判断解析后的ip是否内网
     * @param
     * @date 2018年11月12日
     * @author yanan
     */
    public static boolean internalIp(byte[] addr) {
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte SECTION_1 = 0x0A;
        // 172.16.x.x/12
        final byte SECTION_2 = (byte) 0xAC;
        final byte SECTION_3 = (byte) 0x10;
        final byte SECTION_4 = (byte) 0x1F;
        // 127.0.0.1/16
        final byte SECTION_5 = (byte) 0xC0;
        final byte SECTION_6 = (byte) 0xA8;
        switch (b0) {
            case SECTION_1:
                return true;
            case SECTION_2:
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true;
                }
            case SECTION_5:
                switch (b1) {
                    case SECTION_6:
                        return true;
                }
            default:
                return false;
        }
    }
    /**
     * @todo 获取ip地址
     * @param
     * @date 2018年11月12日
     * @author yanan
     */
    public static final String getIpAddr(final HttpServletRequest request) throws Exception {
        if (request == null) {
            throw (new Exception("======================request为null======================"));
        }
        /**
         * 在JSP里，获取客户端的IP地址的方法是：request.getRemoteAddr() 
         * 这种方法在大部分情况下都是有效的。但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了。
         * 经过代理以后，由于在客户端和服务之间增加了中间层，因此服务器无法直接拿到客户端的IP，服务器端应用也无法直接通过
         * 转发请求的地址返回给客户端。但是在转发请求的HTTP头信息中，增加了X－FORWARDED－FOR等信息。用以跟踪原有的客户
         * 端IP地址和原来客户端请求的服务器地址。
         * 假定客户端通过多级代理，最终到达服务器端(nginx,squid,haproxy)；此时经过多级反向的代理，
         * 通过方法getRemoteAddr()，得不到客户端真实IP，可以通过x-forwarded-for等获得转发后请求信息。
         * 因此获取ip的步骤应为：先获取各种代理服务器ip，若为空再获取request.getRemoteAddr() 
         */

        //x-forwarded-for是一个 Squid 开发的字段，只有在通过了HTTP代理或者负载均衡服务器时才会添加该项。当客户端请求被转发，
        //格式为X-Forwarded-For:client1,proxy1,proxy2，一般情况下，第一个ip为客户端真实ip，
        //IP将会追加在其后并以逗号隔开，后面的为经过的代理服务器ip。现在大部分的代理都会加上这个请求头。
        String ipString = request.getHeader("x-forwarded-for");
        //用apache http做代理时一般会加上Proxy-Client-IP请求头
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        //WL-Proxy-Client-IP是他的weblogic插件加上的头。
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        //HTTP_CLIENT_IP ：有些代理服务器会加上此请求头。
        if (ipString == null || ipString.length() == 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ipString: " + ipString);
        }
        if (ipString == null || ipString.length() == 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ipString: " + ipString);
        }
        //nginx代理一般会加上此请求头。
        if (ipString == null || ipString.length() == 0 || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ipString: " + ipString);
        }
        //当不是上述（代理）方式访问时，request.getRemoteAddr()直接获取客户真实ip
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr(); //客户端未经过代理，直接访问服务器端(nginx,squid,haproxy)；
        }
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }
        return ipString;
    }


}