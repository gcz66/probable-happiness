package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Favorite;
import com.xiaoyuan.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收藏接口
 */
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 我的收藏列表 */
    @GetMapping("/list")
    public Result<List<Favorite>> list(HttpServletRequest request) {
        return Result.success(favoriteService.list(getUserId(request)));
    }

    /** 是否已收藏 */
    @GetMapping("/status/{productId}")
    public Result<Boolean> isFavorite(@PathVariable Long productId, HttpServletRequest request) {
        return Result.success(favoriteService.isFavorite(getUserId(request), productId));
    }

    /** 收藏 */
    @PostMapping("/add/{productId}")
    public Result<Void> add(@PathVariable Long productId, HttpServletRequest request) {
        favoriteService.add(getUserId(request), productId);
        return Result.success("收藏成功", null);
    }

    /** 取消收藏 */
    @DeleteMapping("/cancel/{productId}")
    public Result<Void> cancel(@PathVariable Long productId, HttpServletRequest request) {
        favoriteService.cancel(getUserId(request), productId);
        return Result.success("已取消收藏", null);
    }
}
