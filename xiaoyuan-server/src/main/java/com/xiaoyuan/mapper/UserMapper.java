package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper {

    User selectById(Long id);

    User selectByUsername(String username);

    int insert(User user);

    int update(User user);

    int deleteById(Long id);

    long count();

    /** 更新余额 */
    int updateBalance(Long id, BigDecimal newBalance);

    int updateStatus(Long id, Integer status);
}
