package com.shilei.tourist.service.impl;

import com.alibaba.fastjson.JSON;
import com.shilei.tourist.dao.StaffDao;
import com.shilei.tourist.entity.Staff;
import com.shilei.tourist.mail.SendMail;
import com.shilei.tourist.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    @Autowired
    StaffDao staffDao;

    @Override
    public List<Staff> findAll() {
        return staffDao.findAll();
    }

    @Override
    public List<Staff> findAllWithoutQuit() {
        return staffDao.findAllWithoutQuit();
    }

    @Override
    public void sendEmail(Map<String,String> map) {
        try {
            SendMail.sendEmail(map.get("text"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStaff(Map<String,Object> staffs) {
        //what the fuck simple!
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff saveStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        saveStaff.setState("在职");
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

    @Override
    public void deleteStaff(Map<String, Object> staffs) {
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff deleteStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        staffDao.delete(deleteStaff);
    }

    @Override
    public void updateStaff(Map<String, Object> staffs) {
        Map<String,Object> staff = (Map<String, Object>) staffs.get("staff");
        Staff updateStaff = JSON.parseObject(JSON.toJSONString(staff),Staff.class);
        staffDao.save(updateStaff);
    }
}
