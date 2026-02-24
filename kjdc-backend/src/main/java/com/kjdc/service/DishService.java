package com.kjdc.service;

import com.kjdc.dto.DishDTO;
import com.kjdc.dto.DishUpsertRequest;
import com.kjdc.dto.RandomDishDTO;

import java.util.List;

/**
 * 菜品业务服务接口
 */
public interface DishService {
    
    /**
     * 获取所有可用菜品
     */
    List<DishDTO> getAllAvailableDishes();
    
    /**
     * 按分类查询菜品
     */
    List<DishDTO> getDishesByCategory(String category);
    
    /**
     * 获取所有菜品分类
     */
    List<String> getAllCategories();
    
    /**
     * 按ID查询菜品
     */
    DishDTO getDishById(Long id);
    
    /**
     * 随机推荐菜品
     */
    RandomDishDTO getRandomDish();
    
    /**
     * 按名称搜索菜品
     */
    List<DishDTO> searchDishes(String keyword);

    /**
     * 按店铺ID获取菜品
     */
    List<DishDTO> getDishesByShopId(Long shopId);

    /**
     * 创建菜品（商家端）
     */
    DishDTO createDish(DishUpsertRequest request);

    /**
     * 更新菜品（商家端）
     */
    DishDTO updateDish(Long id, DishUpsertRequest request);

    /**
     * 删除菜品（商家端）
     */
    void deleteDish(Long id);
}
