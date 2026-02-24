package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建店铺请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCreateRequest {

    private String name;

    private String description;

    private String imageUrl;

    private String phone;

    private Long ownerUserId;

    private String address;

    private String businessHours;
}
