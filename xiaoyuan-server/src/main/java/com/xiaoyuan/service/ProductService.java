package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Product;
import com.xiaoyuan.mapper.CategoryMapper;
import com.xiaoyuan.mapper.CommentMapper;
import com.xiaoyuan.mapper.FavoriteMapper;
import com.xiaoyuan.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务
 */
@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;
    private final FavoriteMapper favoriteMapper;

    public ProductService(ProductMapper productMapper, CategoryMapper categoryMapper,
                          CommentMapper commentMapper, FavoriteMapper favoriteMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.commentMapper = commentMapper;
        this.favoriteMapper = favoriteMapper;
    }

    public PageInfo<Product> page(int pageNum, int pageSize, String keyword, Long categoryId, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectPage(keyword, categoryId, status);
        return new PageInfo<>(list);
    }

    public Product getById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return product;
    }

    public Product create(Product product) {
        checkCategory(product.getCategoryId());
        if (product.getStock() == null) product.setStock(0);
        if (product.getSales() == null) product.setSales(0);
        if (product.getStatus() == null) product.setStatus(1);
        productMapper.insert(product);
        return product;
    }

    public Product update(Product product) {
        getById(product.getId());
        if (product.getCategoryId() != null) {
            checkCategory(product.getCategoryId());
        }
        productMapper.update(product);
        return getById(product.getId());
    }

    /**
     * 删除商品（级联删除其评论与收藏，保证数据一致）
     */
    public void delete(Long id) {
        getById(id);
        productMapper.deleteById(id);
        // 级联清理评论与收藏，避免产生孤儿数据
        commentMapper.selectByProductId(id).forEach(c -> commentMapper.deleteById(c.getId()));
        favoriteMapper.deleteByProduct(id);
    }

    private void checkCategory(Long categoryId) {
        if (categoryId != null && categoryMapper.selectById(categoryId) == null) {
            throw new BusinessException("所属分类不存在");
        }
    }
}
