package com.shilei.tourist.controller;

import com.shilei.tourist.service.WarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@RestController
@RequestMapping("/warning")
public class WarningController {

    @Autowired
    WarningService warningService;

    @PostMapping("/todayWarning")
    public List<Map<String,String>> getTodayWarningInfo(@RequestBody Map<String,String> map){
        return warningService.getTodayWarningInfoByAddress(map.get("address"));
    }

    @GetMapping("/warningLineY")
    public List<String> todayWaringLineY(@RequestParam String address){
        return warningService.getWarningLineY(address);
    }

    @GetMapping("/warningLineX")
    public List<String> todayWaringLineX(@RequestParam String address){
        return warningService.getWarningLineX(address);
    }

    @PostMapping("/settingOne")
    public void settingOne(@RequestBody Map<String,Object> map){
        warningService.settingOne(map);
    }

    @GetMapping("/settingAll")
    public void settingAll(@RequestParam int number){
        warningService.settingAll(number);
    }
}
