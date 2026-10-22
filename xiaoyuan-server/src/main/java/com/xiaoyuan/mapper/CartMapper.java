package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 购物车 Mapper
 */
@Mapper
public interface CartMapper {

    Cart selectByUserAndProduct(Long userId, Long productId);

    List<Cart> selectByUserId(Long userId);

    int insert(Cart cart);

    int update(Cart cart);

    int deleteById(Long id);

    int deleteByUserIdAndProduct(Long userId, Long productId);

    int clearByUserId(Long userId);
}
