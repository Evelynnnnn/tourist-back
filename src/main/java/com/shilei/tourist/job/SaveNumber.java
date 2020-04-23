package com.shilei.tourist.job;

import com.shilei.tourist.service.CountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaveNumber {
    @Autowired
    CountService countService;


//
//    @Scheduled(cron = "0 0/2 8-20 * * ?")
//    void savePersonNumber() throws InterruptedException {
//        countService.savePersonNumber();
//    }

    @Scheduled(cron = "0 0 18 * * ?")
    void saveAvgEveryDay(){
        countService.saveAvgToday();
    }


}
