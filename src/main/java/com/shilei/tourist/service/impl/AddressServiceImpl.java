package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.AddressDao;
import com.shilei.tourist.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;

    @Override
    public void getImage(HttpServletRequest request, HttpServletResponse response, Integer pictureId) {
        FileInputStream fis = null;
        response.setContentType("image/gif");
        try {
            OutputStream out = response.getOutputStream();
            File file = new File("D:\\count"+File.separator + pictureId + ".jpg");
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Map<String,String>> getAddressNameList() {
        List<Map<String,String>> list = new ArrayList<>();
        List<String> names = addressDao.findAllAddressName();
        for (int i = 0; i < names.size(); i++) {
            Map<String,String> map = new HashMap<>();
            String key1 = "value";
            String key2 = "label";
            int number = i+1;
            map.put(key1,String.valueOf("选项" + number));
            map.put(key2,String.valueOf(names.get(i)));
            list.add(map);
        }
        log.info("所有地名有{}",list);
        return list;
    }


    @Override
    public void getXImage(HttpServletRequest request, HttpServletResponse response, Integer pictureId) {
        FileInputStream fis = null;
        response.setContentType("image/gif");
        try {
            OutputStream out = response.getOutputStream();
            File file = new File("D:\\count"+File.separator + 1 + ".jpg");
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void getVideo(HttpServletResponse response,int videoId) {
        File file=new File("D://video//"+File.separator+videoId+".mp4");
        ServletOutputStream out=null;
        try {
            FileInputStream instream =new FileInputStream(file);
            byte[] b=new byte[1024];
            int length=0;
            BufferedInputStream buf=new BufferedInputStream(instream);
            out=response.getOutputStream();
            BufferedOutputStream bot=new BufferedOutputStream(out);
            while((length=buf.read(b))!=-1) {
                bot.write(b,0, b.length);
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }

    }
}
