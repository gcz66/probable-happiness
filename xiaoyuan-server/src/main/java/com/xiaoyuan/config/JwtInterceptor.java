package com.xiaoyuan.config;

import com.xiaoyuan.common.BusinessException;
import com.xiaoyuan.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录鉴权拦截器
 * 从请求头 Authorization 中解析 JWT，校验有效性，并将用户信息放入 request
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.header}")
    private String header;

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader(header);
        if (token == null || token.isEmpty()) {
            throw new BusinessException(401, "未登录，请先登录");
        }
        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }
        String role = claims.get("role", String.class);
        // 管理端接口要求 ADMIN 角色
        if (request.getRequestURI().contains("/admin/") && !"ADMIN".equals(role)) {
            throw new BusinessException(403, "无权限访问管理接口");
        }
        // 将用户信息放入 request，供后续使用
        request.setAttribute("userId", claims.get("userId", Long.class));
        request.setAttribute("role", role);
        request.setAttribute("username", claims.get("username", String.class));
        return true;
    }
}
