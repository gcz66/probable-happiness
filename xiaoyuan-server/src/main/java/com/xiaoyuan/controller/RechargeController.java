package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.RechargeRecord;
import com.xiaoyuan.service.RechargeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 充值接口
 */
@RestController
@RequestMapping("/api/recharge")
public class RechargeController {

    private final RechargeService rechargeService;

    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 充值 */
    @PostMapping
    public Result<RechargeRecord> recharge(@RequestBody Map<String, BigDecimal> body, HttpServletRequest request) {
        BigDecimal amount = body.get("amount");
        return Result.success("充值成功", rechargeService.recharge(getUserId(request), amount));
    }

    /** 我的充值记录 */
    @GetMapping("/list")
    public Result<List<RechargeRecord>> list(HttpServletRequest request) {
        return Result.success(rechargeService.listByUser(getUserId(request)));
    }

    /** 管理端充值记录 */
    @GetMapping("/admin/list")
    public Result<List<RechargeRecord>> adminList() {
        return Result.success(rechargeService.listAll());
    }
}
