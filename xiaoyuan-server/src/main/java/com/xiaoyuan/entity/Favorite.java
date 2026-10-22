package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体
 */
@Data
public class Favorite {
    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createTime;

    /** 非表字段：联查商品 */
    private String productName;
    private String productImage;
    private java.math.BigDecimal productPrice;
    private String categoryName;
}
