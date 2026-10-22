package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.Comment;
import com.xiaoyuan.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论接口
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 商品评论列表（无需登录） */
    @GetMapping("/product/{productId}")
    public Result<List<Comment>> listByProduct(@PathVariable Long productId) {
        return Result.success(commentService.listByProduct(productId));
    }

    /** 发表评论 */
    @PostMapping
    public Result<Comment> create(@RequestBody Comment comment, HttpServletRequest request) {
        return Result.success("评论成功", commentService.create(getUserId(request), comment));
    }

    /** 管理端评论列表 */
    @GetMapping("/admin/list")
    public Result<List<Comment>> adminList(@RequestParam(required = false) String keyword) {
        return Result.success(commentService.listAll(keyword));
    }

    /** 删除评论 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success("删除成功", null);
    }
}
