package com.kjdc.service.impl;

import com.kjdc.entity.User;
import com.kjdc.repository.UserRepository;
import com.kjdc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 用户业务服务实现
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public User loginWithWechat(String openId, String nickname, String avatarUrl) {
        // 检查用户是否已存在
        Optional<User> existUser = userRepository.findByWechatOpenId(openId);
        
        if (existUser.isPresent()) {
            // 更新现有用户的昵称和头像
            User user = existUser.get();
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }
        
        // 创建新用户
        User newUser = User.builder()
                .username("user_" + UUID.randomUUID().toString().substring(0, 8))
                .wechatOpenId(openId)
                .nickname(nickname)
                .avatarUrl(avatarUrl)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        return userRepository.save(newUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public User updateUser(Long userId, String phone, String defaultAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        user.setPhone(phone);
        user.setDefaultAddress(defaultAddress);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
