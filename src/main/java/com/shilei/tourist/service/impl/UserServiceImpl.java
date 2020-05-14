package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.AccountDao;
import com.shilei.tourist.entity.Account;
import com.shilei.tourist.mail.SendMail;
import com.shilei.tourist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.GeneralSecurityException;
import java.util.Random;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    AccountDao accountDao;


    @Override
    public void checkUser(Account account) throws GeneralSecurityException, MessagingException {
        SendMail sendMail = new SendMail();
        if (!account.getUsername().isEmpty()){
            String code = generateConfirmCode();
            Account findAccount = accountDao.findAccountByUsername(account.getUsername());
            findAccount.setCode(code);
            accountDao.updateAccountCodeByUsername(findAccount.getUsername(),findAccount.getCode());
            sendMail.sendEmailToMail(code,findAccount.getMail(),"验证码");
        }
    }

    @Override
    public Boolean loginCheck(Account account) {
        if (!account.getCode().isEmpty() && !account.getUsername().isEmpty()){
            Account find = accountDao.findAccountByUsernameAndCode(account.getUsername(),account.getCode());
            if (find != null){
                return true;
            }
        }
        return false;
    }

    public String generateConfirmCode(){
        String code = "";
        for (int i = 0; i < 6; i++) {
            int min = 97;
            int max = 122;
            Random random = new Random();//指定种子数字
            code += String.valueOf((char)(random.nextInt(max)%(max-min+1) + min));
        }
        return code;
    }
}
