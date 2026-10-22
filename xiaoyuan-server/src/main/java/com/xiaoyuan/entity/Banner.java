package com.xiaoyuan.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 轮播图实体
 */
@Data
public class Banner {
    private Long id;
    private String title;
    private String image;
    private String linkUrl;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
}
