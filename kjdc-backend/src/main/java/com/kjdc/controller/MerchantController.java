package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.dto.DishDTO;
import com.kjdc.dto.DishUpsertRequest;
import com.kjdc.dto.OrderDTO;
import com.kjdc.dto.ShopCreateRequest;
import com.kjdc.dto.ShopDTO;
import com.kjdc.dto.ShopUpdateRequest;
import com.kjdc.service.DishService;
import com.kjdc.service.OrderService;
import com.kjdc.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商家端API控制器
 */
@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MerchantController {

    private final ShopService shopService;
    private final DishService dishService;
    private final OrderService orderService;

    /**
     * 创建店铺（商家入驻）
     */
    @PostMapping("/shops")
    public ApiResponse<ShopDTO> createShop(@RequestBody ShopCreateRequest request) {
        try {
            return ApiResponse.success("店铺创建成功", shopService.createShop(request));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 获取店铺信息
     */
    @GetMapping("/shops/{id}")
    public ApiResponse<ShopDTO> getShop(@PathVariable Long id) {
        ShopDTO shop = shopService.getShopById(id);
        if (shop == null) {
            return ApiResponse.fail(404, "店铺不存在");
        }
        return ApiResponse.success("获取店铺信息成功", shop);
    }

    /**
     * 更新店铺信息
     */
    @PutMapping("/shops/{id}")
    public ApiResponse<ShopDTO> updateShop(@PathVariable Long id, @RequestBody ShopUpdateRequest request) {
        try {
            return ApiResponse.success("更新店铺信息成功", shopService.updateShop(id, request));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 设置营业状态
     */
    @PutMapping("/shops/{id}/open")
    public ApiResponse<ShopDTO> setShopOpen(@PathVariable Long id, @RequestParam Boolean open) {
        try {
            return ApiResponse.success("更新营业状态成功", shopService.setShopOpen(id, open));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 店铺菜品列表
     */
    @GetMapping("/shops/{shopId}/dishes")
    public ApiResponse<List<DishDTO>> getShopDishes(@PathVariable Long shopId) {
        return ApiResponse.success("获取店铺菜品成功", dishService.getDishesByShopId(shopId));
    }

    /**
     * 新增菜品
     */
    @PostMapping("/shops/{shopId}/dishes")
    public ApiResponse<DishDTO> createDish(@PathVariable Long shopId, @RequestBody DishUpsertRequest request) {
        try {
            request.setShopId(shopId);
            return ApiResponse.success("创建菜品成功", dishService.createDish(request));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 更新菜品
     */
    @PutMapping("/dishes/{id}")
    public ApiResponse<DishDTO> updateDish(@PathVariable Long id, @RequestBody DishUpsertRequest request) {
        try {
            return ApiResponse.success("更新菜品成功", dishService.updateDish(id, request));
        } catch (Exception e) {
            return ApiResponse.badRequest(e.getMessage());
        }
    }

    /**
     * 删除菜品
     */
    @DeleteMapping("/dishes/{id}")
    public ApiResponse<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ApiResponse.success("删除菜品成功", null);
    }

    /**
     * 店铺订单列表
     */
    @GetMapping("/shops/{shopId}/orders")
    public ApiResponse<List<OrderDTO>> getShopOrders(@PathVariable Long shopId) {
        return ApiResponse.success("获取店铺订单成功", orderService.getShopOrders(shopId));
    }
}
