package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单实体类
 */
@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 订单号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String orderNo;
    
    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 订单状态: PENDING(待支付), PAID(已支付), COMPLETED(已完成), CANCELLED(已取消)
     */
    @Column(nullable = false, length = 20)
    private String status = "PENDING";
    
    /**
     * 订单总金额
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    /**
     * 配送地址
     */
    @Column(length = 255)
    private String deliveryAddress;
    
    /**
     * 备注
     */
    @Column(length = 500)
    private String remarks;
    
    /**
     * 订单项列表
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;
    
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
