package com.shilei.tourist.controller;

import com.shilei.tourist.dao.StaffDao;
import com.shilei.tourist.entity.Staff;
import com.shilei.tourist.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    StaffService staffService;

    @Autowired
    StaffDao staffDao;

    @GetMapping("/all")
    public List<Staff> findAll(){
        return staffService.findAll();
    }

    @GetMapping("/incumbentStaff")
    public List<Staff> findIncumbentStaff(){
        return staffService.findAllWithoutQuit();
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody Map<String,Object> map){
        staffService.sendEmail(map);
    }

    @GetMapping("/sendAllEmail")
    public void sendAllEmail(@RequestParam String text){
        staffService.sendAllEmail(text);
    }

    @PostMapping("/addStaff")
    public void addStaff(@RequestBody Map<String,Object> staff){
        staffService.addStaff(staff);
    }

    @PostMapping("/updateStaff")
    public void updateStaff(@RequestBody Map<String,Object> staff){
        System.out.println(staff);
        staffService.updateStaff(staff);
    }

    @PostMapping("/deleteStaff")
    public void deleteStaff(@RequestBody Map<String,Object> staff){
        staffService.deleteStaff(staff);
    }
}
