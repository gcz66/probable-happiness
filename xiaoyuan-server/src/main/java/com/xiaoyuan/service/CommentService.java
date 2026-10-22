package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Comment;
import com.xiaoyuan.mapper.CommentMapper;
import com.xiaoyuan.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务
 */
@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final ProductMapper productMapper;

    public CommentService(CommentMapper commentMapper, ProductMapper productMapper) {
        this.commentMapper = commentMapper;
        this.productMapper = productMapper;
    }

    public List<Comment> listByProduct(Long productId) {
        return commentMapper.selectByProductId(productId);
    }

    public List<Comment> listAll(String keyword) {
        return commentMapper.selectAll(keyword);
    }

    public Comment create(Long userId, Comment comment) {
        if (productMapper.selectById(comment.getProductId()) == null) {
            throw new BusinessException("商品不存在");
        }
        if (comment.getScore() == null || comment.getScore() < 1 || comment.getScore() > 5) {
            throw new BusinessException("评分必须在1-5之间");
        }
        comment.setUserId(userId);
        comment.setStatus(1);
        commentMapper.insert(comment);
        return comment;
    }

    public void delete(Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        commentMapper.deleteById(id);
    }
}
