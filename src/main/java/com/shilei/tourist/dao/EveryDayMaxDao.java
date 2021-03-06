package com.shilei.tourist.dao;


import com.shilei.tourist.entity.EveryDayMax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EveryDayMaxDao extends JpaRepository<EveryDayMax,Integer> {

    @Query(nativeQuery = true,value = "select everyday_max.number from everyday_max where everyday_max.date >= :startDate and everyday_max.date <= :endDate and everyday_max.address = :address order by everyday_max.date")
    List<String> findEveryDayMaxByDateAndAddress(String startDate, String endDate, String address);

    @Query(nativeQuery = true,value = "select everyday_max.number as max from everyday_max where everyday_max.date = :date and everyday_max.address = :address limit 1")
    String findMaxNumberByDateAndAddress(String date, String address);

    @Query(nativeQuery = true,value = "select everyday_max.number as max from everyday_max where everyday_max.address = :address ")
    List<Integer> findAllMaxNumberByAddress(String address);
}
