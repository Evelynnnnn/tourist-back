package com.shilei.tourist.job;

import com.shilei.tourist.dao.AccountDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeCode {

    @Autowired
    AccountDao accountDao;

    @Scheduled(cron = "0 0/2 0-23 * * ?")
    void setCodeEqNull(){
        accountDao.setCodeEqNull();
        log.info("用户验证码清除成功！");
    }
}
