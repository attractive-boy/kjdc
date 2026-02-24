package com.kjdc.controller;

import com.kjdc.common.ApiResponse;
import com.kjdc.dto.DishDTO;
import com.kjdc.dto.RandomDishDTO;
import com.kjdc.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品API控制器
 */
@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DishController {
    
    private final DishService dishService;
    
    /**
     * 获取所有可用菜品
     */
    @GetMapping
    public ApiResponse<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllAvailableDishes();
        return ApiResponse.success("获取菜品列表成功", dishes);
    }
    
    /**
     * 获取所有菜品分类
     */
    @GetMapping("/categories")
    public ApiResponse<List<String>> getCategories() {
        List<String> categories = dishService.getAllCategories();
        return ApiResponse.success("获取分类列表成功", categories);
    }
    
    /**
     * 按分类查询菜品
     */
    @GetMapping("/category/{category}")
    public ApiResponse<List<DishDTO>> getDishesByCategory(@PathVariable String category) {
        List<DishDTO> dishes = dishService.getDishesByCategory(category);
        return ApiResponse.success("获取分类菜品成功", dishes);
    }
    
    /**
     * 按ID查询菜品详情
     */
    @GetMapping("/{id}")
    public ApiResponse<DishDTO> getDishById(@PathVariable Long id) {
        DishDTO dish = dishService.getDishById(id);
        if (dish == null) {
            return ApiResponse.fail(404, "菜品不存在");
        }
        return ApiResponse.success("获取菜品详情成功", dish);
    }
    
    /**
     * 搜索菜品
     */
    @GetMapping("/search")
    public ApiResponse<List<DishDTO>> searchDishes(@RequestParam String keyword) {
        List<DishDTO> dishes = dishService.searchDishes(keyword);
        return ApiResponse.success("搜索菜品成功", dishes);
    }
    
    /**
     * 获取随机推荐菜品（快速决策功能）
     */
    @GetMapping("/random")
    public ApiResponse<RandomDishDTO> getRandomDish() {
        RandomDishDTO dish = dishService.getRandomDish();
        if (dish == null) {
            return ApiResponse.fail(404, "没有可用的菜品");
        }
        return ApiResponse.success("获取推荐菜品成功", dish);
    }
}
