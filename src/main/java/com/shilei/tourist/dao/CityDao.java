package com.shilei.tourist.dao;

import com.shilei.tourist.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
public interface CityDao extends JpaRepository<City,Integer> {

    @Query(nativeQuery = true,value = "select name from city where parent_id = 1")
    List<String> findAllProvince();

    @Query(nativeQuery = true,value = "select name from city where parent_id = :id")
    List<String> findCityByParentId(Integer id);

    @Query(nativeQuery = true,value = "select * from city where name = :name")
    City findCityByName(String name);
}