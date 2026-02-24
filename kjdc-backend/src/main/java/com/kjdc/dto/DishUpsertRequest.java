package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 菜品创建/更新请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishUpsertRequest {

    private Long shopId;

    private String name;

    private String description;

    private BigDecimal price;

    private String category;

    private String imageUrl;

    private Boolean available;

    private Integer salesVolume;

    private BigDecimal rating;
}
