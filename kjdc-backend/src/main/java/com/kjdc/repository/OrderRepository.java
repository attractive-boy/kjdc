package com.kjdc.repository;

import com.kjdc.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 按订单号查询
     */
    Optional<Order> findByOrderNo(String orderNo);
    
    /**
     * 按用户ID查询订单
     */
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 按用户ID和状态查询订单
     */
    List<Order> findByUserIdAndStatus(Long userId, String status);
    
    /**
     * 按状态查询订单
     */
    List<Order> findByStatus(String status);

    /**
     * 按订单创建时间倒序查询所有订单
     */
    List<Order> findAllByOrderByCreatedAtDesc();

    /**
     * 按菜品ID集合查询涉及的订单（商家端）
     */
    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.dishId IN :dishIds ORDER BY o.createdAt DESC")
    List<Order> findByItemDishIds(List<Long> dishIds);
}
