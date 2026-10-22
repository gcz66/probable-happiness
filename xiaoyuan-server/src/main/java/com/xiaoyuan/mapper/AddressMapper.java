package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收货地址 Mapper
 */
@Mapper
public interface AddressMapper {

    Address selectById(Long id);

    List<Address> selectByUserId(Long userId);

    int insert(Address address);

    int update(Address address);

    int deleteById(Long id);

    /** 取消该用户所有默认地址 */
    int clearDefault(Long userId);
}
