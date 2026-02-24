package com.kjdc.service;

import com.kjdc.dto.CreateOrderRequest;
import com.kjdc.dto.OrderDTO;

import java.util.List;

/**
 * 订单业务服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderDTO createOrder(CreateOrderRequest request);
    
    /**
     * 按ID查询订单
     */
    OrderDTO getOrderById(Long id);
    
    /**
     * 按订单号查询订单
     */
    OrderDTO getOrderByNo(String orderNo);
    
    /**
     * 按用户ID查询订单列表
     */
    List<OrderDTO> getUserOrders(Long userId);
    
    /**
     * 取消订单
     */
    OrderDTO cancelOrder(Long orderId);
    
    /**
     * 更新订单状态
     */
    OrderDTO updateOrderStatus(Long orderId, String status);

    /**
     * 查询所有订单（管理员）
     */
    List<OrderDTO> getAllOrders();

    /**
     * 查询店铺相关订单（商家端）
     */
    List<OrderDTO> getShopOrders(Long shopId);
}
