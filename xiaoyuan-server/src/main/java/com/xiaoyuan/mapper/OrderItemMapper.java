package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单明细 Mapper
 */
@Mapper
public interface OrderItemMapper {

    List<OrderItem> selectByOrderId(Long orderId);

    List<OrderItem> selectByOrderIds(List<Long> orderIds);

    int batchInsert(List<OrderItem> items);

    int deleteByOrderId(Long orderId);
}
