package com.shilei.tourist.dao;


import com.shilei.tourist.entity.EveryDayAvg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EveryDayAvgDao extends JpaRepository<EveryDayAvg,Integer> {

    @Query(nativeQuery = true,value = "select everyday_avg.number from everyday_avg where everyday_avg.date >= :startDate and everyday_avg.date <= :endDate and everyday_avg.address = :address order by everyday_avg.date")
    List<String> findEveryDayAvgByDateAndAddress(String startDate, String endDate, String address);

    @Query(nativeQuery = true,value = "select everyday_avg.number as avg from everyday_avg where everyday_avg.date = :date and everyday_avg.address = :address limit 1")
    String findAvgNumberByDateAndAddress(String address,String date);
}
