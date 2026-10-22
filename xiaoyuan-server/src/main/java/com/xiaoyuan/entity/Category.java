package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体
 */
@Data
public class Category {
    private Long id;
    private String name;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
}
