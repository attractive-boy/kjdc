package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.dto.OrderDTO;
import com.kjdc.dto.ShopDTO;
import com.kjdc.entity.User;
import com.kjdc.service.OrderService;
import com.kjdc.service.ShopService;
import com.kjdc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员端API控制器
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    private final ShopService shopService;
    private final UserService userService;
    private final OrderService orderService;

    /**
     * 店铺列表
     */
    @GetMapping("/shops")
    public ApiResponse<List<ShopDTO>> getAllShops() {
        return ApiResponse.success("获取店铺列表成功", shopService.getAllShops());
    }

    /**
     * 审核店铺
     */
    @PutMapping("/shops/{id}/approve")
    public ApiResponse<ShopDTO> approveShop(@PathVariable Long id, @RequestParam Boolean approved) {
        try {
            return ApiResponse.success("更新店铺审核状态成功", shopService.setShopApproved(id, approved));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 用户列表
     */
    @GetMapping("/users")
    public ApiResponse<List<User>> getAllUsers() {
        return ApiResponse.success("获取用户列表成功", userService.getAllUsers());
    }

    /**
     * 订单列表
     */
    @GetMapping("/orders")
    public ApiResponse<List<OrderDTO>> getAllOrders() {
        return ApiResponse.success("获取订单列表成功", orderService.getAllOrders());
    }
}
