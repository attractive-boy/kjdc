package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private String status;
    
    private BigDecimal totalAmount;
    
    private String deliveryAddress;
    
    private String remarks;
    
    private List<OrderItemDTO> items;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}
