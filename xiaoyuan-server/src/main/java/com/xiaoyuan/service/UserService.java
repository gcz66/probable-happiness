package com.xiaoyuan.service;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.User;
import com.xiaoyuan.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }

    public User update(User user) {
        User db = getById(user.getId());
        db.setNickname(user.getNickname());
        db.setAvatar(user.getAvatar());
        db.setPhone(user.getPhone());
        userMapper.update(db);
        return db;
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        User upd = new User();
        upd.setId(userId);
        upd.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(upd);
    }

    public long count() {
        return userMapper.count();
    }
}
