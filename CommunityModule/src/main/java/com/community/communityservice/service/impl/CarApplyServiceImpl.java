package com.community.communityservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.communityservice.entity.CarApply;
import com.community.communityservice.mapper.CarApplyMapper;
import com.community.communityservice.service.CarApplyService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CarApplyServiceImpl implements CarApplyService {

    private final CarApplyMapper carApplyMapper;

    public CarApplyServiceImpl(CarApplyMapper carApplyMapper) {
        this.carApplyMapper = carApplyMapper;
    }

    @Override
    public List<CarApply> findAll() {
        LambdaQueryWrapper<CarApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(CarApply::getCreatetime);
        return carApplyMapper.selectList(queryWrapper);
    }

    @Override
    public CarApply findById(String id) {
        return carApplyMapper.selectById(id);
    }

    @Override
    public CarApply create(CarApply carApply) {
        carApply.setRowguid(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        carApply.setCreatetime(LocalDateTime.now());
        carApplyMapper.insert(carApply);
        return carApply;
    }

    @Override
    public CarApply update(String id, CarApply carApply) {
        CarApply existing = carApplyMapper.selectById(id);
        if (existing != null) {
            if (carApply.getUserguid() != null) {
                existing.setUserguid(carApply.getUserguid());
            }
            if (carApply.getUsername() != null) {
                existing.setUsername(carApply.getUsername());
            }
            if (carApply.getStartdate() != null) {
                existing.setStartdate(carApply.getStartdate());
            }
            if (carApply.getEnddate() != null) {
                existing.setEnddate(carApply.getEnddate());
            }
            if (carApply.getCarnum() != null) {
                existing.setCarnum(carApply.getCarnum());
            }
            if (carApply.getCartype() != null) {
                existing.setCartype(carApply.getCartype());
            }
            if (carApply.getPhone() != null) {
                existing.setPhone(carApply.getPhone());
            }
            if (carApply.getIdcard() != null) {
                existing.setIdcard(carApply.getIdcard());
            }

            carApplyMapper.updateById(existing);
            return existing;
        }
        return null;
    }

    @Override
    public void delete(String id) {
        carApplyMapper.deleteById(id);
    }

    @Override
    public List<CarApply> findByUserguid(String userguid) {
        LambdaQueryWrapper<CarApply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CarApply::getUserguid, userguid);
        queryWrapper.orderByDesc(CarApply::getCreatetime);
        return carApplyMapper.selectList(queryWrapper);
    }
}