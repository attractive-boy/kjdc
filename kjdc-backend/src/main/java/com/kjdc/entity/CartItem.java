package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 购物车项实体类
 */
@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 菜品ID
     */
    @Column(nullable = false)
    private Long dishId;
    
    /**
     * 菜品名称
     */
    @Column(nullable = false, length = 100)
    private String dishName;
    
    /**
     * 菜品价格
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dishPrice;
    
    /**
     * 数量
     */
    @Column(nullable = false)
    private Integer quantity;
    
    /**
     * 小计金额
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
