package com.kjdc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 用户地址实体类
 */
@Entity
@Table(name = "addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户ID
     */
    @Column(nullable = false)
    private Long userId;
    
    /**
     * 收货人姓名
     */
    @Column(nullable = false, length = 50)
    private String recipientName;
    
    /**
     * 联系电话
     */
    @Column(nullable = false, length = 20)
    private String phone;
    
    /**
     * 省份
     */
    @Column(length = 50)
    private String province;
    
    /**
     * 城市
     */
    @Column(length = 50)
    private String city;
    
    /**
     * 区
     */
    @Column(length = 50)
    private String district;
    
    /**
     * 详细地址
     */
    @Column(nullable = false, length = 255)
    private String detailAddress;
    
    /**
     * 是否为默认地址
     */
    @Column(nullable = false)
    private Boolean isDefault = false;
}
