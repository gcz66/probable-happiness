package com.xiaoyuan.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 下单请求 DTO
 * productIds 为空表示从购物车结算，否则直接购买指定商品
 */
@Data
public class CreateOrderDTO {

    /** 收货地址ID */
    private Long addressId;

    /** 备注 */
    private String remark;

    /** 直接购买的商品ID（从购物车结算时传空） */
    private List<Long> productIds;

    /** 购买数量（配合 productIds 单个商品时使用） */
    private Integer quantity;

    /** 待下单的购物车条目ID列表（从购物车结算时使用） */
    @NotEmpty(message = "请选择要结算的商品")
    private List<Long> cartIds;
}
