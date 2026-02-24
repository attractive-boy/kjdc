package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.dto.CreateOrderRequest;
import com.kjdc.dto.OrderDTO;
import com.kjdc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单API控制器
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public ApiResponse<OrderDTO> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            OrderDTO order = orderService.createOrder(request);
            return ApiResponse.success("创建订单成功", order);
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
    
    /**
     * 按ID查询订单
     */
    @GetMapping("/{id}")
    public ApiResponse<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        if (order == null) {
            return ApiResponse.fail(404, "订单不存在");
        }
        return ApiResponse.success("获取订单详情成功", order);
    }
    
    /**
     * 按订单号查询订单
     */
    @GetMapping("/no/{orderNo}")
    public ApiResponse<OrderDTO> getOrderByNo(@PathVariable String orderNo) {
        OrderDTO order = orderService.getOrderByNo(orderNo);
        if (order == null) {
            return ApiResponse.fail(404, "订单不存在");
        }
        return ApiResponse.success("获取订单详情成功", order);
    }
    
    /**
     * 查询用户订单列表
     */
    @GetMapping("/user/{userId}")
    public ApiResponse<List<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getUserOrders(userId);
        return ApiResponse.success("获取用户订单列表成功", orders);
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderDTO> cancelOrder(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.cancelOrder(id);
            return ApiResponse.success("取消订单成功", order);
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
    
    /**
     * 更新订单状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            OrderDTO order = orderService.updateOrderStatus(id, status);
            return ApiResponse.success("更新订单状态成功", order);
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }
}
