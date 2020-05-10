package com.shilei.tourist.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shilei.tourist.dao.AccountDao;
import com.shilei.tourist.dao.StaffDao;
import com.shilei.tourist.entity.Account;
import com.shilei.tourist.entity.Staff;
import com.shilei.tourist.mail.SendMail;
import com.shilei.tourist.service.StaffService;
import com.shilei.tourist.utils.HanyuPinyinUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    StaffDao staffDao;

    @Autowired
    AccountDao accountDao;

    @Override
    public List<Staff> findAll() {
        return staffDao.findAll();
    }

    @Override
    public List<Staff> findAllWithoutQuit() {
        return staffDao.findAllWithoutQuit();
    }

    @Override
    public void sendEmail(Map<String,Object> map) {
        try {
            String mail = String.valueOf(map.get("mail"));
            SendMail.sendEmail(String.valueOf(map.get("text")),mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAllEmail(String text) {
        List<Staff> list = staffDao.findAllWithoutQuit();
        List<String> mailList = list.stream().map(e -> e.getMail()).collect(Collectors.toList());
        for (String mail:mailList) {
            try {
                SendMail.sendEmail(text,mail);
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addStaff(Map<String,Object> staffs) {
        //what the fuck simple!
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff saveStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        saveStaff.setState("在职");
        changePinYinAndSaveInAccount(saveStaff);
//        saveStaff.setAddress(String.valueOf(staff.get("address")));
//        saveStaff.setCity(String.valueOf(staff.get("city")));
//        saveStaff.setName(String.valueOf(staff.get("name")));
//        saveStaff.setEntryDate(String.valueOf(staff.get("entryDate")));
//        saveStaff.setProvince(String.valueOf(staff.get("province")));
//        saveStaff.setMail(String.valueOf(staff.get("mail")));
//        saveStaff.setPhone(String.valueOf(staff.get("phone")));
//        saveStaff.setJob(String.valueOf(staff.get("job")));
        log.info("新增的人员信息为{}",saveStaff);
        staffDao.save(saveStaff);
    }

    public void changePinYinAndSaveInAccount(Staff staff){
        HanyuPinyinUtil hanyuPinyinUtil = new HanyuPinyinUtil();
        String pinyinName = hanyuPinyinUtil.toHanyuPinyin(staff.getName());
        String email = staff.getMail();
        Account account = new Account();
        account.setMail(email);
        account.setUsername(pinyinName);
        accountDao.save(account);
    }

    @Override
    public void deleteStaff(Map<String, Object> staffs) {
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff deleteStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        staffDao.delete(deleteStaff);
        HanyuPinyinUtil hanyuPinyinUtil = new HanyuPinyinUtil();
        String pinyinName = hanyuPinyinUtil.toHanyuPinyin(deleteStaff.getName());
        accountDao.deleteAccountByUsernameAndMail(pinyinName,deleteStaff.getMail());
    }

    @Override
    public void updateStaff(Map<String, Object> staffs) {
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff updateStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        staffDao.save(updateStaff);
    }
}
