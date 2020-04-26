package com.shilei.tourist.service;

import com.shilei.tourist.vo.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
@Component
public interface CountService {
    //查看当前人数
    PersonCountVO personCount() throws InterruptedException;

    //存储当前人数
    void savePersonNumber() throws InterruptedException;

    //存储今日平均值
    void saveAvgToday();

    //明日人数预计
    List<TomorrowNumberVO> tomorrowNumber();

    //一日人数统计
    @Transactional
    List<TodayCountVO> todayPersonNumber(TodayCountDTO todayCountDTO) throws ParseException;

    //多日人数统计
    @Transactional
    List<MoreDayCountVO> moreDayCount(MoreDayCountDTO moreDayCountDTO) throws ParseException;

    //返回日期
    List<String> getDate(MoreDayCountDTO moreDayCountDTO) throws ParseException;

    List getNumberWarning() throws Exception;

    List<Map<String,String>> getYear();

    List<Map<String,String>> getMonth(int year);

    @Transactional
    List<MoreDayCountVO> monthCount(MonthCountDTO monthCountDTO) throws ParseException;

    @Transactional
    List<MoreDayCountVO> weekCount(TodayCountDTO todayCountDTO) throws ParseException;

    List<String> getAllDate(TodayCountDTO todayCountDTO) throws ParseException;

    List<HomeInfoGetVO> getHomeInfo(String address);
}
