package com.shilei.tourist.dao;

import com.shilei.tourist.entity.EveryDayAvg;
import com.shilei.tourist.entity.PersonNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public interface NumberDao extends JpaRepository<PersonNumber,Integer> {
    @Query(nativeQuery = true,value = "select p.number from person_number p where p.address =:address order by p.time DESC limit 1")
    String findNumberByAddress(String address);

    @Query(nativeQuery = true,value = "select cast(AVG(p.number) as decimal(10,2)) as number from person_number p where p.date = :date and p.address = :address")
    EveryDayAvg findAllByDateAndAddress(String date, String address);

    List<PersonNumber> findAllByAddressAndDateOrderByNowTimeDesc(String placeName, String nowDate);

    List<PersonNumber> findAllByAddressAndDate(String address, String nowDate);

    @Query(nativeQuery = true,value = "select MAX(p.number) as number from person_number p where p.date = ?1 and p.address = ?2 group by p.hour order by p.hour")
    List<String> findMaxByDateAndAddress(String date, String address);

    @Query(nativeQuery = true, value =
//            " select  cast(AVG(p.number) as decimal(10,2)) as number " +
//            " from person_number p " +
//            " where p.date = ?1 " +
//            " and p.address = ?2 " +
//            " and p.hour = 8 " +
//            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 9 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 10 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 11 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number" +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 12 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 13 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 14 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 15 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 16 " +
            " UNION all " +
            " select cast(AVG(p.number) as decimal(10,2)) as number " +
            " from person_number p " +
            " where p.date = ?1 " +
            " and p.address = ?2 " +
            " and p.hour = 17 ")
    List<String> findAvgByDateAndAddress(String date, String address);

    @Query(nativeQuery = true,value = "select MAX(p.number) as number from person_number p where p.date >= :startDate and p.date <= :endDate and p.address = :address order by p.date")
    List<String> findMoreDayMaxByDateAndAddress(String startDate, String endDate, String address);

    @Query(nativeQuery = true,value = "select p.date from person_number p where p.date >= :startDate and p.date <= :endDate")
    List<String> findAllDateByDate(String startDate, String endDate);

    @Query(nativeQuery = true,value = "select MIN(p.number) as min from person_number p where p.date = :date and p.address = :address limit 1")
    String findMinNumberByDateAndAddress(String date, String address);

    @Query(nativeQuery = true,value = "select p.number from person_number p where p.address =:address and p.date = :date order by p.time DESC limit 1")
    String findNumberByAddressAndDate(String address,String date);

}
