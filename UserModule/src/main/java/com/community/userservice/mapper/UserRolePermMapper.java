package com.community.userservice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.userservice.entity.UserRolePerm;

@Mapper
public interface UserRolePermMapper extends BaseMapper<UserRolePerm> {

    @Select("SELECT r.rowguid, r.rolename, r.addtime FROM zl_roleinfo r " +
            "JOIN zl_userrole_perm urp ON r.rowguid = urp.rolelguid " +
            "WHERE urp.userguid = #{userguid}")
    java.util.List<com.community.userservice.entity.Role> selectRolesByUserGuid(@Param("userguid") String userguid);
}