package com.community.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.userservice.entity.RoleMenuPerm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMenuPermMapper extends BaseMapper<RoleMenuPerm> {

    @Select("SELECT menuguid FROM zl_rolemenu_perm WHERE roleguid = #{roleguid}")
    List<String> findMenuGuidsByRoleGuid(@Param("roleguid") String roleguid);

    @Delete("DELETE FROM zl_rolemenu_perm WHERE roleguid = #{roleguid}")
    void deleteByRoleGuid(@Param("roleguid") String roleguid);
}