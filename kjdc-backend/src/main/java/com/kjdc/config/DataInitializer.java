package com.kjdc.config;

import com.kjdc.entity.Dish;
import com.kjdc.entity.Shop;
import com.kjdc.repository.DishRepository;
import com.kjdc.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 数据初始化器 - 应用启动时自动加载示例数据
 * 运行环境：Azure MySQL (quick_order 数据库)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final DishRepository dishRepository;
        private final ShopRepository shopRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 只在菜品数量为0时初始化数据
        long dishCount = dishRepository.count();
        if (dishCount == 0) {
            log.info("==================== 数据库初始化开始 ====================");
            Long shopId = ensureDefaultShop();
            initializeDishes(shopId);
            log.info("==================== 数据库初始化完成，已插入 {} 条菜品数据 ====================", dishRepository.count());
        } else {
            log.info("==================== 数据库中已存在 {} 条菜品数据，跳过初始化 ====================", dishCount);
        }
    }
    
    /**
     * 初始化菜品数据
     */
    private void initializeDishes(Long shopId) {
        // 热菜类
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("宫保鸡丁")
                .description("选用优质鸡肉，配以花生和辣椒，鲜香麻辣")
                .price(new BigDecimal("38.00"))
                .category("热菜")
                .imageUrl("https://images.unsplash.com/photo-1598103442097-8b74394b95c6?w=400")
                .available(true)
                .salesVolume(250)
                .rating(new BigDecimal("4.8"))
                .build());
        
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("红烧肉")
                .description("精选五花肉，慢火烹制，软糯入味，肥而不腻")
                .price(new BigDecimal("42.00"))
                .category("热菜")
                .imageUrl("https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400")
                .available(true)
                .salesVolume(180)
                .rating(new BigDecimal("4.7"))
                .build());
        
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("水煮鱼")
                .description("鲜活鱼肉，辣油烹制，麻辣鲜香，开胃十足")
                .price(new BigDecimal("48.00"))
                .category("热菜")
                .imageUrl("https://images.unsplash.com/photo-1580959375944-abd7e991f971?w=400")
                .available(true)
                .salesVolume(210)
                .rating(new BigDecimal("4.9"))
                .build());
        
        // 凉菜类
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("拍黄瓜")
                .description("新鲜黄瓜，清爽爽口，搭配特制酱汁")
                .price(new BigDecimal("12.00"))
                .category("凉菜")
                .imageUrl("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400")
                .available(true)
                .salesVolume(320)
                .rating(new BigDecimal("4.6"))
                .build());
        
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("凉拌木耳")
                .description("黑木耳脆嫩，配以青椒、豆芽，爽口下饭")
                .price(new BigDecimal("15.00"))
                .category("凉菜")
                .imageUrl("https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400")
                .available(true)
                .salesVolume(150)
                .rating(new BigDecimal("4.5"))
                .build());
        
        // 主食类
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("番茄鸡蛋面")
                .description("手工面条，番茄汤底，营养丰富，清汤爽口")
                .price(new BigDecimal("16.00"))
                .category("主食")
                .imageUrl("https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400")
                .available(true)
                .salesVolume(280)
                .rating(new BigDecimal("4.7"))
                .build());
        
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("蛋炒饭")
                .description("金黄米粒，新鲜鸡蛋，火候十足，香气四溢")
                .price(new BigDecimal("14.00"))
                .category("主食")
                .imageUrl("https://images.unsplash.com/photo-1623521752511-a4d03e92e9e8?w=400")
                .available(true)
                .salesVolume(350)
                .rating(new BigDecimal("4.8"))
                .build());
        
        // 饮品类
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("冰糖雪梨水")
                .description("新鲜梨子，冰糖炖制，清热止咳，甘甜爽口")
                .price(new BigDecimal("8.00"))
                .category("饮品")
                .imageUrl("https://images.unsplash.com/photo-1545544636-3b9faf0d2c7f?w=400")
                .available(true)
                .salesVolume(200)
                .rating(new BigDecimal("4.6"))
                .build());
        
        dishRepository.save(Dish.builder()
                .shopId(shopId)
                .name("西瓜果汁")
                .description("新鲜西瓜榨制，清凉解暑，天然果汁")
                .price(new BigDecimal("9.00"))
                .category("饮品")
                .imageUrl("https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400")
                .available(true)
                .salesVolume(220)
                .rating(new BigDecimal("4.7"))
                .build());
        
        // 甜品类
        dishRepository.save(Dish.builder()
                                .shopId(shopId)
                .name("杨枝甘露")
                .description("芒果、椰浆、西米露，热带风味，丝滑口感")
                .price(new BigDecimal("12.00"))
                .category("甜品")
                .imageUrl("https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400")
                .available(true)
                .salesVolume(180)
                .rating(new BigDecimal("4.9"))
                .build());
        
        log.info("菜品数据初始化完成");
    }

        private Long ensureDefaultShop() {
                long shopCount = shopRepository.count();
                if (shopCount > 0) {
                        return shopRepository.findAll().get(0).getId();
                }

                Shop shop = Shop.builder()
                                .name("快捷点餐旗舰店")
                                .description("主打家常快餐与人气推荐菜")
                                .imageUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600")
                                .phone("400-800-8888")
                                .address("北京市朝阳区示例路88号")
                                .businessHours("10:00-22:00")
                                .approved(true)
                                .open(true)
                                .build();

                Shop saved = shopRepository.save(shop);
                log.info("默认店铺已创建: {}", saved.getName());
                return saved.getId();
        }
}
