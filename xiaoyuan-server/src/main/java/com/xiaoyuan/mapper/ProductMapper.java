package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品 Mapper
 */
@Mapper
public interface ProductMapper {

    Product selectById(Long id);

    /** 分页查询，支持分类/关键词/状态过滤 */
    List<Product> selectPage(String keyword, Long categoryId, Integer status);

    int insert(Product product);

    int update(Product product);

    int deleteById(Long id);

    /** 更新库存（带库存校验：扣减后不小于0） */
    int deductStock(Long id, Integer quantity);

    /** 增加销量 */
    int addSales(Long id, Integer quantity);

    /** 回补库存 */
    int addStock(Long id, Integer quantity);

    /** 统计各分类商品数量 */
    List<java.util.Map<String, Object>> countByCategory();
}
