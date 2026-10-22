package com.xiaoyuan.controller;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.common.Result;
import com.xiaoyuan.entity.User;
import com.xiaoyuan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private Long getUserId(HttpServletRequest request) {
        Object id = request.getAttribute("userId");
        if (id == null) {
            throw new BusinessException(401, "未登录");
        }
        return (Long) id;
    }

    /** 当前用户信息 */
    @GetMapping("/info")
    public Result<User> info(HttpServletRequest request) {
        return Result.success(userService.getById(getUserId(request)));
    }

    /** 更新个人信息 */
    @PutMapping("/update")
    public Result<User> update(@RequestBody User user, HttpServletRequest request) {
        user.setId(getUserId(request));
        return Result.success(userService.update(user));
    }

    /** 修改密码 */
    @PutMapping("/password")
    public Result<Void> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        userService.changePassword(getUserId(request), body.get("oldPassword"), body.get("newPassword"));
        return Result.success("修改成功", null);
    }
}
