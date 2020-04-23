package com.shilei.tourist.dao;

import com.shilei.tourist.entity.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoginRecordDao extends JpaRepository<LoginRecord,Integer> {

    List<LoginRecord> findAll();
}
