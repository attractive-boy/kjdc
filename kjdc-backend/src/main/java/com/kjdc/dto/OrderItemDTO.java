package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单项DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    
    private Long id;
    
    private Long dishId;
    
    private String dishName;
    
    private BigDecimal dishPrice;
    
    private Integer quantity;
    
    private BigDecimal subtotal;
}
