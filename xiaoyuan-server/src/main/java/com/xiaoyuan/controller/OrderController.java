package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.dto.CreateOrderDTO;
import com.xiaoyuan.entity.Orders;
import com.xiaoyuan.service.OrderService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 订单接口
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 下单（购物车结算） */
    @PostMapping("/create")
    public Result<Orders> create(@Valid @RequestBody CreateOrderDTO dto, HttpServletRequest request) {
        return Result.success("下单成功", orderService.createOrder(getUserId(request), dto));
    }

    /** 余额支付 */
    @PostMapping("/pay/{orderId}")
    public Result<Void> pay(@PathVariable Long orderId, HttpServletRequest request) {
        orderService.pay(getUserId(request), orderId);
        return Result.success("支付成功", null);
    }

    /** 我的订单分页 */
    @GetMapping("/list")
    public Result<PageInfo<Orders>> list(@RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(required = false) Integer status,
                                         HttpServletRequest request) {
        return Result.success(orderService.userOrders(getUserId(request), status, pageNum, pageSize));
    }

    /** 订单详情 */
    @GetMapping("/detail/{orderId}")
    public Result<Orders> detail(@PathVariable Long orderId, HttpServletRequest request) {
        return Result.success(orderService.detail(getUserId(request), orderId));
    }

    /** 取消订单 */
    @PostMapping("/cancel/{orderId}")
    public Result<Void> cancel(@PathVariable Long orderId, HttpServletRequest request) {
        orderService.cancel(getUserId(request), orderId);
        return Result.success("已取消", null);
    }

    /** 管理端订单分页 */
    @GetMapping("/admin/list")
    public Result<PageInfo<Orders>> adminList(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) String keyword) {
        return Result.success(orderService.adminOrders(status, keyword, pageNum, pageSize));
    }

    /** 管理端更新订单状态（接单/配送/完成/取消） */
    @PutMapping("/admin/status/{orderId}")
    public Result<Void> updateStatus(@PathVariable Long orderId, @RequestBody Orders orders) {
        orderService.updateStatus(orderId, orders.getStatus());
        return Result.success("更新成功", null);
    }
}
