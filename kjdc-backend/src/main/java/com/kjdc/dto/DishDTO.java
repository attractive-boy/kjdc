package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品DTO（数据传输对象）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    
    private Long id;
    
    private String name;
    
    private String description;
    
    private BigDecimal price;
    
    private String category;

    private Long shopId;
    
    private String imageUrl;
    
    private Boolean available;
    
    private Integer salesVolume;
    
    private BigDecimal rating;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
