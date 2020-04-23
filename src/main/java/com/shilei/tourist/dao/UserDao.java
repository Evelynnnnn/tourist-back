package com.shilei.tourist.dao;


import com.shilei.tourist.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<Account,Integer> {

    Account findByUsernameAndPassword(String username, String password);

    Account findUserByUsername(String username);
}
