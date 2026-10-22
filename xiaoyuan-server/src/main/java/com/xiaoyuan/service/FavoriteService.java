package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Favorite;
import com.xiaoyuan.entity.Product;
import com.xiaoyuan.mapper.FavoriteMapper;
import com.xiaoyuan.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收藏服务
 */
@Service
public class FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final ProductMapper productMapper;

    public FavoriteService(FavoriteMapper favoriteMapper, ProductMapper productMapper) {
        this.favoriteMapper = favoriteMapper;
        this.productMapper = productMapper;
    }

    public List<Favorite> list(Long userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    public void add(Long userId, Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        if (favoriteMapper.selectByUserAndProduct(userId, productId) != null) {
            return; // 已收藏，幂等
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favoriteMapper.insert(favorite);
    }

    public void cancel(Long userId, Long productId) {
        favoriteMapper.deleteByUserAndProduct(userId, productId);
    }

    public boolean isFavorite(Long userId, Long productId) {
        return favoriteMapper.selectByUserAndProduct(userId, productId) != null;
    }
}
