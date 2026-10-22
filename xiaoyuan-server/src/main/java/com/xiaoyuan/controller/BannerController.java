package com.xiaoyuan.controller;

import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Banner;
import com.xiaoyuan.service.BannerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图接口
 */
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    /** 前台轮播图列表（仅显示中） */
    @GetMapping("/list")
    public Result<List<Banner>> list() {
        return Result.success(bannerService.listForFront());
    }

    /** 管理端轮播图列表 */
    @GetMapping("/admin/list")
    public Result<List<Banner>> adminList() {
        return Result.success(bannerService.listAll());
    }

    /** 新增轮播图 */
    @PostMapping("/admin")
    public Result<Banner> create(@RequestBody Banner banner) {
        return Result.success("新增成功", bannerService.create(banner));
    }

    /** 更新轮播图 */
    @PutMapping("/admin/{id}")
    public Result<Banner> update(@PathVariable Long id, @RequestBody Banner banner) {
        banner.setId(id);
        return Result.success("更新成功", bannerService.update(banner));
    }

    /** 删除轮播图 */
    @DeleteMapping("/admin/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return Result.success("删除成功", null);
    }
}
