package com.shilei.tourist.dao;

import com.shilei.tourist.entity.WarningInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface WarningInfoDao extends JpaRepository<WarningInfo,Integer> {
    List<WarningInfo> findAllByAddressAndDate(String address,String date);

    @Query(nativeQuery = true,value = "select number from warning_info where address =:address and date = :date order by datetime")
    List<String> findNumberByAddressAndDate(String address,String date);

    @Query(nativeQuery = true,value = "select time from warning_info where address =:address and date = :date order by datetime")
    List<String> findDatetimeByAddressAndDate(String address,String date);
}
