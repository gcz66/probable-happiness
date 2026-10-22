package com.xiaoyuan.controller;

import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Product;
import com.xiaoyuan.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

/**
 * 商品接口
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** 前台商品分页列表（仅上架商品） */
    @GetMapping("/list")
    public Result<PageInfo<Product>> list(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) Long categoryId) {
        return Result.success(productService.page(pageNum, pageSize, keyword, categoryId, 1));
    }

    /** 商品详情 */
    @GetMapping("/detail/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    /** 管理端商品分页（含下架） */
    @GetMapping("/admin/list")
    public Result<PageInfo<Product>> adminList(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(required = false) Integer status) {
        return Result.success(productService.page(pageNum, pageSize, keyword, categoryId, status));
    }

    /** 新增商品 */
    @PostMapping("/admin")
    public Result<Product> create(@RequestBody Product product) {
        return Result.success("新增成功", productService.create(product));
    }

    /** 更新商品 */
    @PutMapping("/admin/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success("更新成功", productService.update(product));
    }

    /** 删除商品 */
    @DeleteMapping("/admin/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success("删除成功", null);
    }
}
