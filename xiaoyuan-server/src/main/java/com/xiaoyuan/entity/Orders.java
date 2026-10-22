package com.xiaoyuan.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体
 */
@Data
public class Orders {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 非表字段 */
    private String username;
    private String consignee;
    private String phone;
    private String addressDetail;
    private List<OrderItem> items;
}
