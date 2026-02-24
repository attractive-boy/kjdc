package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新店铺请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdateRequest {

    private String name;

    private String description;

    private String imageUrl;

    private String phone;

    private String address;

    private String businessHours;

    private Double rating;

    private Integer monthlySales;

    private Boolean open;
}
