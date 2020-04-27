package com.shilei.tourist.dao;

import com.shilei.tourist.entity.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoginRecordDao extends JpaRepository<LoginRecord,Integer> {

    List<LoginRecord> findAll();

    @Query(nativeQuery = true,value = "select login_time from login_record where username = :username order by login_time DESC limit 1")
    String findLastLoginTimeByUsername(String username);

    @Query(nativeQuery = true,value = "select address from login_record where username = :username order by login_time DESC limit 1")
    String findLastLoginAddressByUsername(String username);

}
