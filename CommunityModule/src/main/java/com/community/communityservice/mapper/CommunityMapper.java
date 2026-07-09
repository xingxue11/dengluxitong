package com.community.communityservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.communityservice.entity.Community;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommunityMapper extends BaseMapper<Community> {
}