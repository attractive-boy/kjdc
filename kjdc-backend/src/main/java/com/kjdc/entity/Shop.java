package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 店铺实体类
 */
@Entity
@Table(name = "shops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 店铺名称
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * 店铺描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 店铺图片URL
     */
    @Column(length = 255)
    private String imageUrl;
    
    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 店主用户ID（可选）
     */
    @Column
    private Long ownerUserId;
    
    /**
     * 店铺地址
     */
    @Column(length = 255)
    private String address;
    
    /**
     * 营业时间（备注字段）
     */
    @Column(length = 100)
    private String businessHours;
    
    /**
     * 平均评分
     */
    @Column(precision = 3, scale = 1)
    private Double rating;
    
    /**
     * 月销量
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer monthlySales = 0;

    /**
     * 是否已审核
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean approved = false;
    
    /**
     * 是否营业
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean open = true;
    
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
