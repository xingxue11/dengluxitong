package com.community.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.userservice.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM zl_userinfo WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM zl_userinfo WHERE username = #{username}")
    int countByUsername(@Param("username") String username);

    @Select("SELECT COUNT(*) FROM zl_userinfo WHERE email = #{email}")
    int countByEmail(@Param("email") String email);
}