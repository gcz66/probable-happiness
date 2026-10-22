package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 收藏 Mapper
 */
@Mapper
public interface FavoriteMapper {

    Favorite selectByUserAndProduct(Long userId, Long productId);

    List<Favorite> selectByUserId(Long userId);

    int insert(Favorite favorite);

    int deleteByUserAndProduct(Long userId, Long productId);

    /** 删除某商品的所有收藏（商品删除时级联清理） */
    int deleteByProduct(Long productId);

    long countByProduct(Long productId);
}
