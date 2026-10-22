package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品分类 Mapper
 */
@Mapper
public interface CategoryMapper {

    Category selectById(Long id);

    List<Category> selectList(String keyword);

    int insert(Category category);

    int update(Category category);

    int deleteById(Long id);

    long countByStatus(Integer status);
}
