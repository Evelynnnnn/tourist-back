package com.shilei.tourist.service;

import com.shilei.tourist.entity.Account;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.security.GeneralSecurityException;

@Service
@Transactional
public interface UserService {

    void checkUser(Account account) throws GeneralSecurityException, MessagingException;

    Boolean loginCheck(Account account);
}
