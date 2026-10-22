package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收货地址实体
 */
@Data
public class Address {
    private Long id;
    private Long userId;
    private String consignee;
    private String phone;
    private String detail;
    private Integer isDefault;
    private LocalDateTime createTime;
}
