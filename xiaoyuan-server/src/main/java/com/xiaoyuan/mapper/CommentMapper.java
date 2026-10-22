package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 评论 Mapper
 */
@Mapper
public interface CommentMapper {

    Comment selectById(Long id);

    /** 商品评论列表 */
    List<Comment> selectByProductId(Long productId);

    /** 管理端评论列表 */
    List<Comment> selectAll(String keyword);

    int insert(Comment comment);

    int update(Comment comment);

    int deleteById(Long id);

    long countAll();

    /** 平均评分 */
    Double avgScore(Long productId);
}
