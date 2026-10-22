package com.xiaoyuan.service;

import com.xiaoyuan.mapper.CommentMapper;
import com.xiaoyuan.mapper.OrdersMapper;
import com.xiaoyuan.mapper.ProductMapper;
import com.xiaoyuan.mapper.RechargeRecordMapper;
import com.xiaoyuan.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计服务（管理端看板）
 */
@Service
public class StatisticsService {

    private final OrdersMapper ordersMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final RechargeRecordMapper rechargeRecordMapper;

    public StatisticsService(OrdersMapper ordersMapper, ProductMapper productMapper, UserMapper userMapper,
                             CommentMapper commentMapper, RechargeRecordMapper rechargeRecordMapper) {
        this.ordersMapper = ordersMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.rechargeRecordMapper = rechargeRecordMapper;
    }

    /**
     * 总览指标
     */
    public Map<String, Object> overview() {
        Map<String, Object> data = new HashMap<>();
        data.put("todayOrders", ordersMapper.countToday());
        data.put("todayAmount", ordersMapper.sumTodayAmount());
        data.put("totalOrders", ordersMapper.countAll());
        data.put("totalAmount", totalSalesAmount());
        data.put("totalUsers", userMapper.count());
        data.put("totalProducts", productMapper.selectPage(null, null, null).size());
        data.put("totalComments", commentMapper.countAll());
        data.put("totalRecharge", rechargeRecordMapper.sumRecharge());
        return data;
    }

    /**
     * 各状态订单分布
     */
    public List<Map<String, Object>> orderStatus() {
        return ordersMapper.countByStatus();
    }

    /**
     * 近7日订单量与营业额
     */
    public List<Map<String, Object>> recent7Days() {
        return ordersMapper.recent7Days();
    }

    /**
     * 各分类商品数量
     */
    public List<Map<String, Object>> productByCategory() {
        return productMapper.countByCategory();
    }

    /**
     * 热门商品（按销量排序 Top N）
     */
    public List<Map<String, Object>> hotProducts(int topN) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        var products = productMapper.selectPage(null, null, 1);
        products.sort((a, b) -> Integer.compare(b.getSales(), a.getSales()));
        int limit = Math.min(topN, products.size());
        for (int i = 0; i < limit; i++) {
            var p = products.get(i);
            Map<String, Object> m = new HashMap<>();
            m.put("name", p.getName());
            m.put("sales", p.getSales());
            result.add(m);
        }
        return result;
    }

    private BigDecimal totalSalesAmount() {
        return ordersMapper.sumTotalAmount();
    }
}
