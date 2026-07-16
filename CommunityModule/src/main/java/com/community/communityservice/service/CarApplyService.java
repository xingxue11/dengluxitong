package com.community.communityservice.service;

import com.community.communityservice.entity.CarApply;

import java.util.List;

public interface CarApplyService {

    List<CarApply> findAll();

    CarApply findById(String id);

    CarApply create(CarApply carApply);

    CarApply update(String id, CarApply carApply);

    void delete(String id);

    List<CarApply> findByUserguid(String userguid);
    
    List<CarApply> findByUsername(String username);
}