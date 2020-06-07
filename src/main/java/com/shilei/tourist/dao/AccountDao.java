package com.shilei.tourist.dao;


import com.shilei.tourist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AccountDao extends JpaRepository<Account,Integer> {

    Account findAccountByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update account set code = :code where username = :username")
    void updateAccountCodeByUsername(String username,String code);

    Account findAccountByUsernameAndCode(String username,String code);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from account where username = :username and mail = :mail")
    void deleteAccountByUsernameAndMail(String username,String mail);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update account set code = ''")
    void setCodeEqNull();
}
