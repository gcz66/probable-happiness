package com.xiaoyuan.controller;

import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Category;
import com.xiaoyuan.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类接口
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** 前台分类列表（仅启用） */
    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list(null).stream()
                .filter(c -> c.getStatus() != null && c.getStatus() == 1)
                .toList());
    }

    /** 管理端分类列表 */
    @GetMapping("/admin/list")
    public Result<List<Category>> adminList(@RequestParam(required = false) String keyword) {
        return Result.success(categoryService.list(keyword));
    }

    /** 新增分类 */
    @PostMapping("/admin")
    public Result<Category> create(@RequestBody Category category) {
        return Result.success("新增成功", categoryService.create(category));
    }

    /** 更新分类 */
    @PutMapping("/admin/{id}")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return Result.success("更新成功", categoryService.update(category));
    }

    /** 删除分类 */
    @DeleteMapping("/admin/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success("删除成功", null);
    }
}
