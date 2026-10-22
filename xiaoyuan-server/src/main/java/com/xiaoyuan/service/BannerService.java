package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Banner;
import com.xiaoyuan.mapper.BannerMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 轮播图服务
 */
@Service
public class BannerService {

    private final BannerMapper bannerMapper;

    public BannerService(BannerMapper bannerMapper) {
        this.bannerMapper = bannerMapper;
    }

    /** 前台：仅返回显示中的轮播图 */
    public List<Banner> listForFront() {
        return bannerMapper.selectList(1);
    }

    /** 管理端：全部轮播图 */
    public List<Banner> listAll() {
        return bannerMapper.selectList(null);
    }

    public Banner getById(Long id) {
        Banner banner = bannerMapper.selectById(id);
        if (banner == null) {
            throw new BusinessException("轮播图不存在");
        }
        return banner;
    }

    public Banner create(Banner banner) {
        if (banner.getStatus() == null) banner.setStatus(1);
        if (banner.getSort() == null) banner.setSort(0);
        bannerMapper.insert(banner);
        return banner;
    }

    public Banner update(Banner banner) {
        getById(banner.getId());
        bannerMapper.update(banner);
        return getById(banner.getId());
    }

    public void delete(Long id) {
        getById(id);
        bannerMapper.deleteById(id);
    }
}
