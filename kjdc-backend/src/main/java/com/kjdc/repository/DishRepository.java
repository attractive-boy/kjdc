package com.kjdc.repository;

import com.kjdc.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜品Repository
 */
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    
    /**
     * 按分类查询菜品
     */
    List<Dish> findByCategory(String category);
    
    /**
     * 按可用状态查询菜品
     */
    List<Dish> findByAvailable(Boolean available);
    
    /**
     * 按名称模糊查询
     */
    List<Dish> findByNameContaining(String name);

    /**
     * 按店铺ID查询菜品
     */
    List<Dish> findByShopId(Long shopId);
    
    /**
     * 获取所有可用的菜品，按销量降序排列
     */
    @Query("SELECT d FROM Dish d WHERE d.available = true ORDER BY d.salesVolume DESC")
    List<Dish> findAllAvailableOrderBySalesVolume();
    
    /**
     * 获取所有菜品分类
     */
    @Query("SELECT DISTINCT d.category FROM Dish d WHERE d.category IS NOT NULL")
    List<String> findAllCategories();
}
