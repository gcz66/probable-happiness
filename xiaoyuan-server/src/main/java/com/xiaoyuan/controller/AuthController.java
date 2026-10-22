package com.xiaoyuan.controller;

import com.xiaoyuan.common.Result;
import com.xiaoyuan.dto.LoginDTO;
import com.xiaoyuan.dto.RegisterDTO;
import com.xiaoyuan.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** 用户登录 */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success("登录成功", authService.login(dto.getUsername(), dto.getPassword()));
    }

    /** 用户注册 */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO dto) {
        authService.register(dto.getUsername(), dto.getPassword(), dto.getNickname(), dto.getPhone());
        return Result.success("注册成功", null);
    }

    /** 管理员登录 */
    @PostMapping("/admin/login")
    public Result<Map<String, Object>> adminLogin(@Valid @RequestBody LoginDTO dto) {
        return Result.success("登录成功", authService.adminLogin(dto.getUsername(), dto.getPassword()));
    }
}
