package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.RechargeRecord;
import com.xiaoyuan.entity.User;
import com.xiaoyuan.mapper.RechargeRecordMapper;
import com.xiaoyuan.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 充值服务
 * 事务保证"更新余额 + 记流水"原子性
 */
@Service
public class RechargeService {

    private final RechargeRecordMapper rechargeRecordMapper;
    private final UserMapper userMapper;

    public RechargeService(RechargeRecordMapper rechargeRecordMapper, UserMapper userMapper) {
        this.rechargeRecordMapper = rechargeRecordMapper;
        this.userMapper = userMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    public RechargeRecord recharge(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        BigDecimal newBalance = user.getBalance().add(amount);
        // 更新余额
        userMapper.updateBalance(userId, newBalance);
        // 记充值流水
        RechargeRecord record = new RechargeRecord();
        record.setOrderNo(generateNo());
        record.setUserId(userId);
        record.setAmount(amount);
        record.setType(1);
        record.setBalanceAfter(newBalance);
        record.setRemark("账户充值");
        rechargeRecordMapper.insert(record);
        return record;
    }

    public List<RechargeRecord> listByUser(Long userId) {
        return rechargeRecordMapper.selectByUserId(userId);
    }

    public List<RechargeRecord> listAll() {
        return rechargeRecordMapper.selectAll();
    }

    private String generateNo() {
        return "RC" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
