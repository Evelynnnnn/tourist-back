package com.shilei.tourist.controller;

import com.alibaba.fastjson.JSONObject;
import com.shilei.tourist.dao.LoginRecordDao;
import com.shilei.tourist.entity.Account;
import com.shilei.tourist.service.LoginRecordService;
import com.shilei.tourist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    LoginRecordService loginRecordService;

    @Autowired
    LoginRecordDao loginRecordDao;

    @PostMapping("/login")
    public Boolean countPerson(@RequestBody Account account)  {
        return userService.checkUser(account);
    }

    @PostMapping("/userInfo")
    public void userInfo(@RequestBody Map<String,Object> loginInfo){
        loginRecordService.saveLoginRecord(loginInfo);
    }

    @GetMapping("/record")
    public JSONObject loginRecord(){
        return loginRecordService.findLoginRecord();
    }

    @PostMapping("/lastLoginTime")
    public String getLastLoginTime(@RequestBody Map<String,String> map){
        return loginRecordDao.findLastLoginTimeByUsername(map.get("username"));
    }

    @PostMapping("/lastLoginAddress")
    public String getLastLoginAddress(@RequestBody Map<String,String> map){
        return loginRecordDao.findLastLoginAddressByUsername(map.get("username"));
    }
}
