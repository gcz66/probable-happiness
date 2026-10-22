package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.Banner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 轮播图 Mapper
 */
@Mapper
public interface BannerMapper {

    Banner selectById(Long id);

    List<Banner> selectList(Integer status);

    int insert(Banner banner);

    int update(Banner banner);

    int deleteById(Long id);
}
