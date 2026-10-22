package com.xiaoyuan.controller;

import com.xiaoyuan.common.Result;
import com.xiaoyuan.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据统计接口（管理端看板）
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /** 总览指标 */
    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        return Result.success(statisticsService.overview());
    }

    /** 各状态订单分布 */
    @GetMapping("/orderStatus")
    public Result<List<Map<String, Object>>> orderStatus() {
        return Result.success(statisticsService.orderStatus());
    }

    /** 近7日订单量与营业额 */
    @GetMapping("/recent7Days")
    public Result<List<Map<String, Object>>> recent7Days() {
        return Result.success(statisticsService.recent7Days());
    }

    /** 各分类商品数量 */
    @GetMapping("/productByCategory")
    public Result<List<Map<String, Object>>> productByCategory() {
        return Result.success(statisticsService.productByCategory());
    }

    /** 热门商品 TopN */
    @GetMapping("/hotProducts")
    public Result<List<Map<String, Object>>> hotProducts(@RequestParam(defaultValue = "5") int topN) {
        return Result.success(statisticsService.hotProducts(topN));
    }
}
