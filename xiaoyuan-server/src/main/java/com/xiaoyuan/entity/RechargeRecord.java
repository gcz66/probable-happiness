package com.xiaoyuan.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录实体
 */
@Data
public class RechargeRecord {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;
    private Integer type;
    private BigDecimal balanceAfter;
    private String remark;
    private LocalDateTime createTime;
}
