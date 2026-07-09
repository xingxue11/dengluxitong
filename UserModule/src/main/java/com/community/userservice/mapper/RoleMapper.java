package com.community.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.userservice.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}