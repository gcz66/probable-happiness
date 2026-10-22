package com.xiaoyuan.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体
 */
@Data
public class Product {
    private Long id;
    private Long categoryId;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String description;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 非表字段：分类名称（联查） */
    private String categoryName;
}
