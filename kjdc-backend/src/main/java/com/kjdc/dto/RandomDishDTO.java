package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 随机菜品推荐响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RandomDishDTO {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private String price;
    
    private String category;

    private Long shopId;
    
    private String imageUrl;
    
    private String rating;
}
