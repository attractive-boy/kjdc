package com.kjdc.repository;

import com.kjdc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 按用户名查询
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 按微信OpenID查询
     */
    Optional<User> findByWechatOpenId(String openId);
}
