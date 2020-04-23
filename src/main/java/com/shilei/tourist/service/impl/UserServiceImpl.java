package com.shilei.tourist.service.impl;

import com.shilei.tourist.dao.UserDao;
import com.shilei.tourist.entity.Account;
import com.shilei.tourist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public Boolean checkUser(Account account) {
        Boolean confirm = false;
        if (!account.getUsername().isEmpty() && !account.getPassword().isEmpty()){
            if(userDao.findByUsernameAndPassword(account.getUsername(), account.getPassword()) != null){
                confirm = true;
            }
        }
        return confirm;
    }
}
