package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.User;
import com.xiaoyuan.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务：用户/管理员登录、注册
 */
@Service
public class AuthService {

    private final UserMapper userMapper;
    private final com.xiaoyuan.mapper.AdminMapper adminMapper;
    private final com.xiaoyuan.util.JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserMapper userMapper, com.xiaoyuan.mapper.AdminMapper adminMapper,
                       com.xiaoyuan.util.JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户登录
     */
    public java.util.Map<String, Object> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String token = jwtUtil.generateToken(user.getId(), "USER", user.getUsername());
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("token", token);
        map.put("user", user);
        return map;
    }

    /**
     * 用户注册
     */
    public void register(String username, String password, String nickname, String phone) {
        if (userMapper.selectByUsername(username) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(nickname == null || nickname.isEmpty() ? username : nickname);
        user.setPhone(phone);
        user.setBalance(new java.math.BigDecimal("0"));
        user.setStatus(1);
        userMapper.insert(user);
    }

    /**
     * 管理员登录
     */
    public java.util.Map<String, Object> adminLogin(String username, String password) {
        var admin = adminMapper.selectByUsername(username);
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String token = jwtUtil.generateToken(admin.getId(), "ADMIN", admin.getUsername());
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("token", token);
        map.put("admin", admin);
        return map;
    }
}
