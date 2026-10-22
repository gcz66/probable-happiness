package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单 Mapper
 */
@Mapper
public interface OrdersMapper {

    Orders selectById(Long id);

    Orders selectByOrderNo(String orderNo);

    /** 用户订单分页 */
    List<Orders> selectUserOrders(Long userId, Integer status);

    /** 管理端订单分页 */
    List<Orders> selectAdminOrders(Integer status, String keyword);

    int insert(Orders orders);

    int update(Orders orders);

    int deleteById(Long id);

    /** 支付：待支付 -> 已支付 */
    int payOrder(Long id);

    /** 更新订单状态 */
    int updateStatus(Long id, Integer status);

    /** 统计：今日订单数 */
    long countToday();

    /** 统计：今日营业额 */
    BigDecimal sumTodayAmount();

    /** 统计：已支付订单总营业额（状态1-4） */
    BigDecimal sumTotalAmount();

    /** 统计：订单总数 */
    long countAll();

    /** 统计：各状态订单数 */
    List<Map<String, Object>> countByStatus();

    /** 近7日订单量与营业额 */
    List<Map<String, Object>> recent7Days();
}
