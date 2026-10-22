package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车实体
 */
@Data
public class Cart {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 非表字段：联查商品信息 */
    private String productName;
    private String productImage;
    private java.math.BigDecimal productPrice;
    private Integer productStock;
    private Integer productStatus;
}
