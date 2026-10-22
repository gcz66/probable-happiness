package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员 Mapper
 */
@Mapper
public interface AdminMapper {

    Admin selectById(Long id);

    Admin selectByUsername(String username);

    int insert(Admin admin);

    int update(Admin admin);

    int deleteById(Long id);

    long count();
}
