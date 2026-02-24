package com.kjdc.service.impl;

import com.kjdc.dto.DishDTO;
import com.kjdc.dto.DishUpsertRequest;
import com.kjdc.dto.RandomDishDTO;
import com.kjdc.entity.Dish;
import com.kjdc.repository.DishRepository;
import com.kjdc.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 菜品业务服务实现
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishServiceImpl implements DishService {
    
    private final DishRepository dishRepository;
    private final Random random = new Random();
    
    @Override
    public List<DishDTO> getAllAvailableDishes() {
        return dishRepository.findByAvailable(true)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DishDTO> getDishesByCategory(String category) {
        return dishRepository.findByCategory(category)
                .stream()
                .filter(Dish::getAvailable)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getAllCategories() {
        return dishRepository.findAllCategories();
    }
    
    @Override
    public DishDTO getDishById(Long id) {
        return dishRepository.findById(id)
                .filter(Dish::getAvailable)
                .map(this::convertToDTO)
                .orElse(null);
    }
    
    @Override
    public RandomDishDTO getRandomDish() {
        // 获取所有可用菜品，按销量排序（使用加权随机算法）
        List<Dish> availableDishes = dishRepository.findAllAvailableOrderBySalesVolume();
        
        if (availableDishes.isEmpty()) {
            return null;
        }
        
        // 加权随机：销量高的菜品更容易被选中
        Dish selectedDish = weightedRandomSelection(availableDishes);
        
        return RandomDishDTO.builder()
                .id(selectedDish.getId())
                .name(selectedDish.getName())
                .description(selectedDish.getDescription())
                .price(selectedDish.getPrice().toPlainString())
                .category(selectedDish.getCategory())
            .shopId(selectedDish.getShopId())
                .imageUrl(selectedDish.getImageUrl())
                .rating(selectedDish.getRating() != null ? selectedDish.getRating().toPlainString() : "0")
                .build();
    }
    
    @Override
    public List<DishDTO> searchDishes(String keyword) {
        return dishRepository.findByNameContaining(keyword)
                .stream()
                .filter(Dish::getAvailable)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DishDTO> getDishesByShopId(Long shopId) {
        return dishRepository.findByShopId(shopId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DishDTO createDish(DishUpsertRequest request) {
        Dish dish = Dish.builder()
                .shopId(request.getShopId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .salesVolume(request.getSalesVolume() != null ? request.getSalesVolume() : 0)
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Dish saved = dishRepository.save(dish);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public DishDTO updateDish(Long id, DishUpsertRequest request) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("菜品不存在"));

        if (request.getShopId() != null) {
            dish.setShopId(request.getShopId());
        }
        if (request.getName() != null) {
            dish.setName(request.getName());
        }
        if (request.getDescription() != null) {
            dish.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            dish.setPrice(request.getPrice());
        }
        if (request.getCategory() != null) {
            dish.setCategory(request.getCategory());
        }
        if (request.getImageUrl() != null) {
            dish.setImageUrl(request.getImageUrl());
        }
        if (request.getAvailable() != null) {
            dish.setAvailable(request.getAvailable());
        }
        if (request.getSalesVolume() != null) {
            dish.setSalesVolume(request.getSalesVolume());
        }
        if (request.getRating() != null) {
            dish.setRating(request.getRating());
        }
        dish.setUpdatedAt(LocalDateTime.now());

        Dish saved = dishRepository.save(dish);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
    
    /**
     * 加权随机选择：销量越高，被选中的概率越大
     */
    private Dish weightedRandomSelection(List<Dish> dishes) {
        // 计算权重总和（销量 + 1，避免销量为0的菜品无法被选中）
        long weightSum = dishes.stream()
                .mapToLong(d -> d.getSalesVolume() + 1)
                .sum();
        
        // 随机生成一个数，范围在 [0, weightSum)
        long random_value = (long) (random.nextDouble() * weightSum);
        
        long currentWeight = 0;
        for (Dish dish : dishes) {
            currentWeight += dish.getSalesVolume() + 1;
            if (random_value < currentWeight) {
                return dish;
            }
        }
        
        // 如果没有找到（理论上不会发生），返回最后一个菜品
        return dishes.get(dishes.size() - 1);
    }
    
    /**
     * 将实体转换为DTO
     */
    private DishDTO convertToDTO(Dish dish) {
        return DishDTO.builder()
                .id(dish.getId())
                .name(dish.getName())
                .description(dish.getDescription())
                .price(dish.getPrice())
                .category(dish.getCategory())
                .shopId(dish.getShopId())
                .imageUrl(dish.getImageUrl())
                .available(dish.getAvailable())
                .salesVolume(dish.getSalesVolume())
                .rating(dish.getRating())
                .createdAt(dish.getCreatedAt())
                .updatedAt(dish.getUpdatedAt())
                .build();
    }
}
