package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体类
 */
@Entity
@Table(name = "dishes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 菜品名称
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * 菜品描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 菜品价格
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /**
     * 菜品分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 归属店铺ID（商家端管理）
     */
    @Column
    private Long shopId;
    
    /**
     * 菜品图片URL
     */
    @Column(length = 255)
    private String imageUrl;
    
    /**
     * 是否可用
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;
    
    /**
     * 销量（用于推荐算法）
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer salesVolume = 0;
    
    /**
     * 评分
     */
    @Column(precision = 3, scale = 1)
    private BigDecimal rating;
    
    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    /**
     * 更新时间
     */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
