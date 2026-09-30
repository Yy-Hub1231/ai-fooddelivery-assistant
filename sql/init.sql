-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS fooddelivery_assistant
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;
-- 2. 使用数据库
USE fooddelivery_assistant;
-- 3. 创建外卖订单表
CREATE TABLE IF NOT EXISTS food_order (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    user_name VARCHAR(100) NOT NULL COMMENT '用户名',
    food_name VARCHAR(200) NOT NULL COMMENT '菜品名称',
    quantity INT DEFAULT 1 COMMENT '数量',
    price DECIMAL(10, 2) DEFAULT 0.00 COMMENT '单价',
    address VARCHAR(500) COMMENT '配送地址',
    phone VARCHAR(20) COMMENT '联系电话',
    status VARCHAR(20) DEFAULT '待配送' COMMENT '订单状态：待配送/已配送/已取消',
    order_time DATETIME COMMENT '下单时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外卖订单表';