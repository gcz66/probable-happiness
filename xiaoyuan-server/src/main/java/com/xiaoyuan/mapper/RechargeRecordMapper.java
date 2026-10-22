package com.xiaoyuan.mapper;

import com.xiaoyuan.entity.RechargeRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 充值记录 Mapper
 */
@Mapper
public interface RechargeRecordMapper {

    RechargeRecord selectById(Long id);

    List<RechargeRecord> selectByUserId(Long userId);

    List<RechargeRecord> selectAll();

    int insert(RechargeRecord record);

    long countAll();

    /** 统计充值总额 */
    java.math.BigDecimal sumRecharge();
}
