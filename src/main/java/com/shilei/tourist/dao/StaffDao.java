package com.shilei.tourist.dao;

import com.shilei.tourist.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffDao extends JpaRepository<Staff,Integer> {
    List<Staff> findAll();

    @Query(nativeQuery = true,value = "select * from staff where staff.state = '在职'")
    List<Staff> findAllWithoutQuit();

    @Query(nativeQuery = true,value = "select staff.duty_address,address.person_num,staff.name,staff.job,address.warning_number from staff, address where staff.duty_address = address.name")
    public List<Object[]> getWarningNumber();

    @Query(nativeQuery = true,value = "select name from staff where staff.duty_address = :address and staff.state = '在职'")
    String findStaffByAddress(String address);
}
