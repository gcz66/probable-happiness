package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 评论实体
 */
@Data
public class Comment {
    private Long id;
    private Long userId;
    private Long productId;
    private Long orderId;
    private String content;
    private Integer score;
    private Integer status;
    private LocalDateTime createTime;

    /** 非表字段：联查用户 */
    private String nickname;
    private String avatar;
    private String productName;
}
