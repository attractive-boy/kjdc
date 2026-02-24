package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.dto.ShopDTO;
import com.kjdc.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 店铺API控制器（用户端）
 */
@RestController
@RequestMapping("/shops")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShopController {

    private final ShopService shopService;

    /**
     * 获取已审核且营业中的店铺列表
     */
    @GetMapping
    public ApiResponse<List<ShopDTO>> getOpenShops() {
        return ApiResponse.success("获取店铺列表成功", shopService.getOpenApprovedShops());
    }

    /**
     * 获取店铺详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ShopDTO> getShopById(@PathVariable Long id) {
        ShopDTO shop = shopService.getShopById(id);
        if (shop == null) {
            return ApiResponse.fail(404, "店铺不存在");
        }
        return ApiResponse.success("获取店铺详情成功", shop);
    }
}
