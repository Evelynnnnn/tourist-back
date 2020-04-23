package com.shilei.tourist.controller;

import com.shilei.tourist.dao.EveryDayAvgDao;
import com.shilei.tourist.service.AddressService;
import com.shilei.tourist.service.CountService;
import com.shilei.tourist.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.shilei.tourist.service.impl.CountServiceImpl.*;

@Component
@RestController
@RequestMapping("/count")
public class CountController {
    @Autowired
    CountService countService;

    @Autowired
    AddressService addressService;

    @Autowired
    EveryDayAvgDao everyDayAvgDao;

    @GetMapping("/countPerson")
    public PersonCountVO countPerson() throws InterruptedException {
        return countService.personCount();
    }

    @GetMapping("/tomorrowNumber")
    public List<TomorrowNumberVO> tomorrowNumber(){
        return countService.tomorrowNumber();
    }

    @Transactional
    @PostMapping("/todayCount")
    public List<TodayCountVO> todayCount(@RequestBody TodayCountDTO todayCountDTO) throws ParseException {
        return countService.todayPersonNumber(todayCountDTO);
    }

    @GetMapping("/allAddressName")
    public List<Map<String,String>> getAllAddressName(){
        return addressService.getAddressNameList();
    }

    @Transactional
    @PostMapping("/getDate")
    public List<String> getDate(@RequestBody MoreDayCountDTO moreDayCountDTO) throws ParseException {
        return countService.getDate(moreDayCountDTO);
    }

    @Transactional
    @PostMapping("/getAllDate")
    public List<String> getWeek(@RequestBody TodayCountDTO todayCountDTO) throws ParseException {
        return countService.getAllDate(todayCountDTO);
    }

    @PostMapping("/getMonthDate")
    public List<String> getMonthDate(@RequestBody MonthCountDTO monthCountDTO) throws ParseException {
        MoreDayCountDTO moreDayCountDTO = new MoreDayCountDTO();
        moreDayCountDTO.setStartDate(getFirstDayOfMonth(monthCountDTO.getYear(),monthCountDTO.getMonth()));
        moreDayCountDTO.setEndDate(getLastDayOfMonth(monthCountDTO.getYear(),monthCountDTO.getMonth()));
        moreDayCountDTO.setAddress(monthCountDTO.getAddress());
        return getDayList(moreDayCountDTO);
    }

    @PostMapping("/moreDayCount")
    public List<MoreDayCountVO> getMoreDayCount(@RequestBody MoreDayCountDTO moreDayCountDTO) throws ParseException {
        return countService.moreDayCount(moreDayCountDTO);
    }

    @GetMapping("/warning")
    public List<NumberWarningVO> getNumberWarning() throws Exception {
        return countService.getNumberWarning();
    }

    @GetMapping("/year")
    public List<Map<String,String>> getYear(){
        return countService.getYear();
    }

    @GetMapping("/month")
    public List<Map<String,String>> getMonth(@RequestParam int year){
        return countService.getMonth(year);
    }

    @PostMapping("/monthCount")
    public List<MoreDayCountVO> getMonthCount(@RequestBody MonthCountDTO monthCountDTO) throws ParseException {
        return countService.monthCount(monthCountDTO);
    }

    @PostMapping("/weekCount")
    public List<MoreDayCountVO> getWeekCount(@RequestBody TodayCountDTO todayCountDTO) throws ParseException {
        return countService.weekCount(todayCountDTO);
    }
//    @GetMapping(value = "/info")
//    public JSONArray getImgInfo() {
//        return addressService.getImageInfo();
//    }
//
//
//
//
//    @GetMapping(value = "/image" )
//    public void getImage(@RequestParam int pictureId, HttpServletRequest request, HttpServletResponse response) {
//        addressService.getImage(request,response,pictureId+1);
//    }

    @GetMapping(value = "/ximage" )
    public void getXImage( @RequestParam int pictureId,HttpServletRequest request,HttpServletResponse response) {
        addressService.getXImage(request,response,pictureId+1);
    }
//
//    @GetMapping("/video")
//    public void getVideo(HttpServletResponse response,int videoId){
//        addressService.getVideo(response,videoId+1);
//    }
//

}
