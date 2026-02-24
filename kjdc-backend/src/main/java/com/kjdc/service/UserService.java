package com.kjdc.service;

import com.kjdc.entity.User;

import java.util.Optional;
import java.util.List;

/**
 * 用户业务服务接口
 */
public interface UserService {
    
    /**
     * 创建或更新用户（微信登录）
     */
    User loginWithWechat(String openId, String nickname, String avatarUrl);
    
    /**
     * 按ID查询用户
     */
    Optional<User> getUserById(Long id);
    
    /**
     * 按用户名查询用户
     */
    Optional<User> getUserByUsername(String username);
    
    /**
     * 更新用户信息
     */
    User updateUser(Long userId, String phone, String defaultAddress);

    /**
     * 查询所有用户（管理员）
     */
    List<User> getAllUsers();
}
