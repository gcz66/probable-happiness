package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Cart;
import com.xiaoyuan.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车接口
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 我的购物车 */
    @GetMapping("/list")
    public Result<List<Cart>> list(HttpServletRequest request) {
        return Result.success(cartService.list(getUserId(request)));
    }

    /** 加入购物车 */
    @PostMapping("/add")
    public Result<Cart> add(@RequestBody Cart cart, HttpServletRequest request) {
        return Result.success("已加入购物车", cartService.add(getUserId(request), cart.getProductId(), cart.getQuantity()));
    }

    /** 更新数量 */
    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable Long id, @RequestBody Cart cart, HttpServletRequest request) {
        cartService.updateQuantity(getUserId(request), id, cart.getQuantity());
        return Result.success("更新成功", null);
    }

    /** 删除条目 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        cartService.delete(getUserId(request), id);
        return Result.success("删除成功", null);
    }

    /** 清空购物车 */
    @DeleteMapping("/clear")
    public Result<Void> clear(HttpServletRequest request) {
        cartService.clear(getUserId(request));
        return Result.success("已清空", null);
    }
}
