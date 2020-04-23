package com.shilei.tourist.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


public interface AddressService {

    void getImage(HttpServletRequest request, HttpServletResponse response, Integer pictureId);

    void getXImage(HttpServletRequest request, HttpServletResponse response, Integer pictureId);

    @Transactional
    List<Map<String,String>> getAddressNameList();

    void getVideo(HttpServletResponse response, int videoId);
}
