-- 快捷点餐系统 Azure MySQL 初始化脚本
-- 数据库名称: quick_order
-- 创建日期: 2026-02-24

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS quick_order;
USE quick_order;

-- 创建users表（用户信息）
CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  wechat_open_id VARCHAR(100) UNIQUE,
  nickname VARCHAR(100),
  avatar_url VARCHAR(255),
  phone VARCHAR(20),
  default_address VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_wechat_open_id (wechat_open_id),
  INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建shops表（店铺信息）
CREATE TABLE IF NOT EXISTS shops (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  image_url VARCHAR(255),
  phone VARCHAR(20),
  owner_user_id BIGINT,
  address VARCHAR(255),
  business_hours VARCHAR(100),
  rating DECIMAL(3, 1),
  monthly_sales INT DEFAULT 0,
  approved BOOLEAN DEFAULT false,
  open BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建dishes表（菜品信息）
CREATE TABLE IF NOT EXISTS dishes (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  price DECIMAL(10, 2) NOT NULL,
  category VARCHAR(50),
  shop_id BIGINT,
  image_url VARCHAR(255),
  available BOOLEAN DEFAULT true,
  sales_volume INT DEFAULT 0,
  rating DECIMAL(3, 1),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_category (category),
  INDEX idx_available (available),
  INDEX idx_sales_volume (sales_volume),
  INDEX idx_shop_id (shop_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建addresses表（用户地址）
CREATE TABLE IF NOT EXISTS addresses (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  recipient_name VARCHAR(50) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  province VARCHAR(50),
  city VARCHAR(50),
  district VARCHAR(50),
  detail_address VARCHAR(255) NOT NULL,
  is_default BOOLEAN DEFAULT false,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建orders表（订单信息）
CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_no VARCHAR(50) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDING',
  total_amount DECIMAL(10, 2) NOT NULL,
  delivery_address VARCHAR(255),
  remarks VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_order_no (order_no),
  INDEX idx_status (status),
  INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建order_items表（订单项）
CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  dish_id BIGINT NOT NULL,
  dish_name VARCHAR(100) NOT NULL,
  dish_price DECIMAL(10, 2) NOT NULL,
  quantity INT NOT NULL,
  subtotal DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE RESTRICT,
  INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建cart_items表（购物车项）
CREATE TABLE IF NOT EXISTS cart_items (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  dish_id BIGINT NOT NULL,
  dish_name VARCHAR(100) NOT NULL,
  dish_price DECIMAL(10, 2) NOT NULL,
  quantity INT NOT NULL,
  subtotal DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (dish_id) REFERENCES dishes(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  UNIQUE KEY unique_user_dish (user_id, dish_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化店铺数据
INSERT INTO shops (name, description, image_url, phone, address, business_hours, rating, monthly_sales, approved, open) VALUES
('快捷点餐旗舰店', '主打家常快餐与人气推荐菜', 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600', '400-800-8888', '北京市朝阳区示例路88号', '10:00-22:00', 4.8, 1200, true, true);

-- 初始化菜品数据
INSERT INTO dishes (name, description, price, category, shop_id, image_url, available, sales_volume, rating) VALUES
('宫保鸡丁', '选用优质鸡肉，配以花生和辣椒，鲜香麻辣', 38.00, '热菜', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1598103442097-8b74394b95c6?w=400', true, 250, 4.8),
('红烧肉', '精选五花肉，慢火烹制，软糯入味，肥而不腻', 42.00, '热菜', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1567620905732-2d1ec7ab7445?w=400', true, 180, 4.7),
('水煮鱼', '鲜活鱼肉，辣油烹制，麻辣鲜香，开胃十足', 48.00, '热菜', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1580959375944-abd7e991f971?w=400', true, 210, 4.9),
('拍黄瓜', '新鲜黄瓜，清爽爽口，搭配特制酱汁', 12.00, '凉菜', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400', true, 320, 4.6),
('凉拌木耳', '黑木耳脆嫩，配以青椒、豆芽，爽口下饭', 15.00, '凉菜', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400', true, 150, 4.5),
('番茄鸡蛋面', '手工面条，番茄汤底，营养丰富，清汤爽口', 16.00, '主食', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=400', true, 280, 4.7),
('蛋炒饭', '金黄米粒，新鲜鸡蛋，火候十足，香气四溢', 14.00, '主食', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1623521752511-a4d03e92e9e8?w=400', true, 350, 4.8),
('冰糖雪梨水', '新鲜梨子，冰糖炖制，清热止咳，甘甜爽口', 8.00, '饮品', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1545544636-3b9faf0d2c7f?w=400', true, 200, 4.6),
('西瓜果汁', '新鲜西瓜榨制，清凉解暑，天然果汁', 9.00, '饮品', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=400', true, 220, 4.7),
('杨枝甘露', '芒果、椰浆、西米露，热带风味，丝滑口感', 12.00, '甜品', (SELECT id FROM shops WHERE name='快捷点餐旗舰店' LIMIT 1), 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400', true, 180, 4.9);

-- 验证表创建
SHOW TABLES;
SELECT COUNT(*) as dish_count FROM dishes;
