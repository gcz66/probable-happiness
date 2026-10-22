package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Category;
import com.xiaoyuan.mapper.CategoryMapper;
import com.xiaoyuan.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类服务
 */
@Service
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    public CategoryService(CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    public List<Category> list(String keyword) {
        return categoryMapper.selectList(keyword);
    }

    public Category getById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }

    public Category create(Category category) {
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        categoryMapper.insert(category);
        return category;
    }

    public Category update(Category category) {
        getById(category.getId());
        categoryMapper.update(category);
        return getById(category.getId());
    }

    /**
     * 删除分类（若分类下仍有商品则拒绝删除，避免孤儿数据）
     */
    public void delete(Long id) {
        getById(id);
        long productCount = productMapper.selectPage(null, id, null).size();
        if (productCount > 0) {
            throw new BusinessException("该分类下仍有商品，无法删除");
        }
        categoryMapper.deleteById(id);
    }
}
