package com.kjdc.service.impl;

import com.kjdc.dto.CreateOrderRequest;
import com.kjdc.dto.OrderDTO;
import com.kjdc.dto.OrderItemDTO;
import com.kjdc.entity.Dish;
import com.kjdc.entity.Order;
import com.kjdc.entity.OrderItem;
import com.kjdc.repository.DishRepository;
import com.kjdc.repository.OrderRepository;
import com.kjdc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 订单业务服务实现
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    
    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> items = new java.util.ArrayList<>();
        
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Dish dish = dishRepository.findById(itemRequest.getDishId())
                    .orElseThrow(() -> new IllegalArgumentException("菜品不存在"));
            
            BigDecimal subtotal = dish.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            OrderItem item = OrderItem.builder()
                    .dishId(dish.getId())
                    .dishName(dish.getName())
                    .dishPrice(dish.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .subtotal(subtotal)
                    .build();
            
            items.add(item);
        }
        
        // 创建订单
        Order order = Order.builder()
                .orderNo(orderNo)
                .userId(request.getUserId())
                .status("PENDING")
                .totalAmount(totalAmount)
                .deliveryAddress(request.getDeliveryAddress())
                .remarks(request.getRemarks())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // 关联订单项
        items.forEach(item -> item.setOrder(order));
        order.setItems(items);
        
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalArgumentException("只能取消未支付的订单");
        }
        
        order.setStatus("CANCELLED");
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepository.save(order);
        
        return convertToDTO(updated);
    }
    
    @Override
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepository.save(order);
        
        return convertToDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getShopOrders(Long shopId) {
        List<Long> dishIds = dishRepository.findByShopId(shopId)
                .stream()
                .map(Dish::getId)
                .collect(Collectors.toList());

        if (dishIds.isEmpty()) {
            return Collections.emptyList();
        }

        return orderRepository.findByItemDishIds(dishIds)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6);
    }
    
    /**
     * 将实体转换为DTO
     */
    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .userId(order.getUserId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .remarks(order.getRemarks())
                .items(order.getItems() != null ? order.getItems().stream()
                        .map(item -> OrderItemDTO.builder()
                                .id(item.getId())
                                .dishId(item.getDishId())
                                .dishName(item.getDishName())
                                .dishPrice(item.getDishPrice())
                                .quantity(item.getQuantity())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()) : null)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
