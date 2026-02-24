package com.kjdc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建订单请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    private Long userId;

    private String deliveryAddress;

    private String remarks;

    private List<OrderItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {

        private Long dishId;

        private Integer quantity;
    }
}
