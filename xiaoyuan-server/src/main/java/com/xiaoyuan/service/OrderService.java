package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.dto.CreateOrderDTO;
import com.xiaoyuan.entity.*;
import com.xiaoyuan.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务
 * 下单、支付均通过事务保证原子性（扣库存、减余额、生成明细要么全部成功要么全部回滚）
 */
@Service
public class OrderService {

    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final RechargeRecordMapper rechargeRecordMapper;

    public OrderService(OrdersMapper ordersMapper, OrderItemMapper orderItemMapper, CartMapper cartMapper,
                        ProductMapper productMapper, UserMapper userMapper, AddressMapper addressMapper,
                        RechargeRecordMapper rechargeRecordMapper) {
        this.ordersMapper = ordersMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
        this.rechargeRecordMapper = rechargeRecordMapper;
    }

    /**
     * 下单（购物车结算），事务保证原子性
     */
    @Transactional(rollbackFor = Exception.class)
    public Orders createOrder(Long userId, CreateOrderDTO dto) {
        // 1. 校验地址
        Address address = addressMapper.selectById(dto.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("收货地址无效");
        }
        // 2. 从购物车取结算商品
        List<Cart> carts = cartMapper.selectByUserId(userId);
        List<Cart> selected = new ArrayList<>();
        for (Cart c : carts) {
            if (dto.getCartIds() != null && dto.getCartIds().contains(c.getId())) {
                selected.add(c);
            }
        }
        if (selected.isEmpty()) {
            throw new BusinessException("请选择要结算的商品");
        }
        // 3. 生成订单主表
        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setAddressId(dto.getAddressId());
        order.setStatus(0); // 待支付
        order.setRemark(dto.getRemark());
        // 4. 计算总价、校验库存、扣库存、生成明细
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (Cart c : selected) {
            Product p = productMapper.selectById(c.getProductId());
            if (p == null) {
                throw new BusinessException("商品不存在");
            }
            if (p.getStatus() != 1) {
                throw new BusinessException("商品【" + p.getName() + "】已下架");
            }
            // 扣库存（带库存校验，不足则回滚）
            int rows = productMapper.deductStock(p.getId(), c.getQuantity());
            if (rows == 0) {
                throw new BusinessException("商品【" + p.getName() + "】库存不足");
            }
            productMapper.addSales(p.getId(), c.getQuantity());
            BigDecimal subtotal = p.getPrice().multiply(BigDecimal.valueOf(c.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = new OrderItem();
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setProductImage(p.getImage());
            item.setPrice(p.getPrice());
            item.setQuantity(c.getQuantity());
            item.setSubtotal(subtotal);
            items.add(item);
        }
        order.setTotalAmount(total);
        ordersMapper.insert(order);
        // 5. 批量插入明细
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.batchInsert(items);
        // 6. 删除已结算的购物车条目
        for (Cart c : selected) {
            cartMapper.deleteById(c.getId());
        }
        return ordersMapper.selectById(order.getId());
    }

    /**
     * 余额支付订单，事务保证"扣余额 + 订单状态变更 + 记流水"原子性
     */
    @Transactional(rollbackFor = Exception.class)
    public void pay(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("订单当前状态不可支付");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getBalance().compareTo(order.getTotalAmount()) < 0) {
            throw new BusinessException("余额不足，请先充值");
        }
        // 扣余额
        userMapper.updateBalance(userId, user.getBalance().subtract(order.getTotalAmount()));
        // 订单置为已支付
        int rows = ordersMapper.payOrder(orderId);
        if (rows == 0) {
            throw new BusinessException("订单状态已变更，请刷新");
        }
        // 记消费流水
        RechargeRecord record = new RechargeRecord();
        record.setOrderNo(order.getOrderNo());
        record.setUserId(userId);
        record.setAmount(order.getTotalAmount().negate());
        record.setType(2); // 消费
        record.setBalanceAfter(user.getBalance().subtract(order.getTotalAmount()));
        record.setRemark("订单支付：" + order.getOrderNo());
        rechargeRecordMapper.insert(record);
    }

    /** 用户订单分页 */
    public PageInfo<Orders> userOrders(Long userId, Integer status, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectUserOrders(userId, status);
        fillItems(list);
        return new PageInfo<>(list);
    }

    /** 管理端订单分页 */
    public PageInfo<Orders> adminOrders(Integer status, String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAdminOrders(status, keyword);
        fillItems(list);
        return new PageInfo<>(list);
    }

    public Orders detail(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (userId != null && !order.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该订单");
        }
        List<OrderItem> items = orderItemMapper.selectByOrderId(orderId);
        order.setItems(items);
        return order;
    }

    /** 管理端更新订单状态（接单/配送/完成/取消） */
    public void updateStatus(Long orderId, Integer status) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        ordersMapper.updateStatus(orderId, status);
    }

    /** 取消订单（待支付状态可取消） */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long userId, Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("该订单已支付，无法取消");
        }
        ordersMapper.updateStatus(orderId, 5);
    }

    /** 填充订单明细 */
    private void fillItems(List<Orders> ordersList) {
        if (ordersList == null || ordersList.isEmpty()) {
            return;
        }
        List<Long> orderIds = new ArrayList<>();
        for (Orders o : ordersList) {
            orderIds.add(o.getId());
        }
        List<OrderItem> allItems = orderItemMapper.selectByOrderIds(orderIds);
        for (Orders o : ordersList) {
            List<OrderItem> items = new ArrayList<>();
            for (OrderItem it : allItems) {
                if (it.getOrderId().equals(o.getId())) {
                    items.add(it);
                }
            }
            o.setItems(items);
        }
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
