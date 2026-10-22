package com.xiaoyuan.config;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.entity.Admin;
import com.xiaoyuan.entity.User;
import com.xiaoyuan.mapper.AdminMapper;
import com.xiaoyuan.mapper.UserMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据播种：首次启动时创建默认管理员和测试用户（密码 BCrypt 加密）
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataInitializer(UserMapper userMapper, AdminMapper adminMapper) {
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
    }

    @Override
    public void run(String... args) {
        try {
            // 默认管理员 admin / admin123
            if (adminMapper.count() == 0) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRealName("超级管理员");
                admin.setRole("SUPER");
                admin.setStatus(1);
                adminMapper.insert(admin);
                System.out.println("[DataInitializer] 已创建默认管理员: admin / admin123");
            }
            // 默认用户 test / 123456
            if (userMapper.count() == 0) {
                User user = new User();
                user.setUsername("test");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setNickname("测试用户");
                user.setPhone("13800000000");
                user.setBalance(java.math.BigDecimal.valueOf(100));
                user.setStatus(1);
                userMapper.insert(user);
                System.out.println("[DataInitializer] 已创建默认用户: test / 123456");
            }
        } catch (Exception e) {
            throw new BusinessException("数据初始化失败：" + e.getMessage());
        }
    }
}
