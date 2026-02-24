package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.entity.User;
import com.kjdc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户API控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * 微信登录
     */
    @PostMapping("/wechat-login")
    public ApiResponse<User> wechatLogin(
            @RequestParam String openId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String avatarUrl) {
        
        try {
            User user = userService.loginWithWechat(openId, nickname, avatarUrl);
            return ApiResponse.success("登录成功", user);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ApiResponse.success("获取用户信息成功", user))
                .orElseGet(() -> ApiResponse.fail(404, "用户不存在"));
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String defaultAddress) {
        
        try {
            User user = userService.updateUser(id, phone, defaultAddress);
            return ApiResponse.success("更新用户信息成功", user);
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
}
