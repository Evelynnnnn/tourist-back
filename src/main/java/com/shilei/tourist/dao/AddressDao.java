package com.shilei.tourist.dao;

import com.shilei.tourist.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AddressDao extends JpaRepository<Address,Integer> {

    Address findByPictureId(int id);

    List<Address> findAll();

    @Query(nativeQuery = true,value = "select a.name from address a")
    List<String> findAllAddressName();

    Address findAddressByName(String name);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update address set warning_number = :warningNumber where name = :address")
    void updateWarningNumberByAddress(String warningNumber,String address);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update address set warning_number = :warningNumber")
    void updateAllWarningNumber(int warningNumber);
}
