package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户名
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    /**
     * 微信OpenID（微信小程序登录）
     */
    @Column(unique = true, length = 100)
    private String wechatOpenId;
    
    /**
     * 昵称
     */
    @Column(length = 100)
    private String nickname;
    
    /**
     * 头像URL
     */
    @Column(length = 255)
    private String avatarUrl;
    
    /**
     * 电话号码
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * 默认地址
     */
    @Column(length = 255)
    private String defaultAddress;
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
