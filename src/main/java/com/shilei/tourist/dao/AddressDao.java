package com.shilei.tourist.dao;

import com.shilei.tourist.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressDao extends JpaRepository<Address,Integer> {

    Address findByPictureId(int id);

    List<Address> findAll();

    @Query(nativeQuery = true,value = "select a.name from address a")
    List<String> findAllAddressName();
}
