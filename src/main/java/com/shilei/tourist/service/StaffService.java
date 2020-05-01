package com.shilei.tourist.service;

import com.shilei.tourist.entity.Staff;

import java.util.List;
import java.util.Map;

public interface StaffService {
    List<Staff> findAll();

    List<Staff> findAllWithoutQuit();

    void sendEmail(Map<String, Object> map);

    void sendAllEmail(String text);

    void addStaff(Map<String, Object> staff);

    void deleteStaff(Map<String, Object> staff);

    void updateStaff(Map<String, Object> staff);
}
