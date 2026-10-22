-- =====================================================
-- 校园外卖平台 数据库初始化脚本
-- MySQL 8.x
-- 数据库：xiaoyuan
-- 共 12 张业务表
-- =====================================================

CREATE DATABASE IF NOT EXISTS `xiaoyuan` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `xiaoyuan`;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`     VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(100) NOT NULL COMMENT '密码(MD5/BCrypt)',
    `nickname`     VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`       VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `phone`        VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `balance`      DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
    `status`       TINYINT      NOT NULL DEFAULT '1' COMMENT '状态 1正常 0禁用',
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 管理员表
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
    `username`    VARCHAR(50) NOT NULL COMMENT '账号',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name`   VARCHAR(50) DEFAULT NULL COMMENT '姓名',
    `role`        VARCHAR(20) NOT NULL DEFAULT 'ADMIN' COMMENT '角色 ADMIN/SUPER',
    `status`      TINYINT     NOT NULL DEFAULT '1' COMMENT '状态 1正常 0禁用',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 3. 商品分类表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '分类名称',
    `sort`        INT         NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      TINYINT     NOT NULL DEFAULT '1' COMMENT '状态 1上架 0下架',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 4. 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '商品ID',
    `category_id` BIGINT       NOT NULL COMMENT '分类ID',
    `name`        VARCHAR(100) NOT NULL COMMENT '商品名称',
    `image`       VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
    `price`       DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
    `stock`       INT          NOT NULL DEFAULT '0' COMMENT '库存',
    `sales`       INT          NOT NULL DEFAULT '0' COMMENT '销量',
    `description` TEXT         COMMENT '商品描述',
    `status`      TINYINT      NOT NULL DEFAULT '1' COMMENT '状态 1上架 0下架',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 5. 轮播图表
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '轮播图ID',
    `title`       VARCHAR(100) DEFAULT NULL COMMENT '标题',
    `image`       VARCHAR(255) NOT NULL COMMENT '图片地址',
    `link_url`    VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
    `sort`        INT          NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT '1' COMMENT '状态 1显示 0隐藏',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='轮播图表';

-- 6. 收货地址表
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '地址ID',
    `user_id`     BIGINT       NOT NULL COMMENT '用户ID',
    `consignee`   VARCHAR(50)  NOT NULL COMMENT '收货人',
    `phone`       VARCHAR(20)  NOT NULL COMMENT '联系电话',
    `detail`      VARCHAR(255) NOT NULL COMMENT '详细地址',
    `is_default`  TINYINT      NOT NULL DEFAULT '0' COMMENT '是否默认 1是 0否',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 7. 购物车表
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT   NOT NULL COMMENT '商品ID',
    `quantity`    INT      NOT NULL DEFAULT '1' COMMENT '数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 8. 订单表
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no`     VARCHAR(32)   NOT NULL COMMENT '订单号',
    `user_id`      BIGINT        NOT NULL COMMENT '用户ID',
    `address_id`   BIGINT        DEFAULT NULL COMMENT '收货地址ID',
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
    `status`       TINYINT       NOT NULL DEFAULT '0' COMMENT '状态 0待支付 1已支付 2已接单 3配送中 4已完成 5已取消',
    `remark`       VARCHAR(255)  DEFAULT NULL COMMENT '备注',
    `pay_time`     DATETIME      DEFAULT NULL COMMENT '支付时间',
    `create_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 9. 订单明细表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id`         BIGINT        NOT NULL AUTO_INCREMENT COMMENT '明细ID',
    `order_id`   BIGINT        NOT NULL COMMENT '订单ID',
    `product_id` BIGINT        NOT NULL COMMENT '商品ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT '商品名称（快照）',
    `product_image` VARCHAR(255) DEFAULT NULL COMMENT '商品图片（快照）',
    `price`      DECIMAL(10,2) NOT NULL COMMENT '下单时单价',
    `quantity`   INT           NOT NULL COMMENT '数量',
    `subtotal`   DECIMAL(10,2) NOT NULL COMMENT '小计',
    PRIMARY KEY (`id`),
    KEY `idx_order` (`order_id`),
    KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 10. 评论表
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `user_id`     BIGINT       NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT       NOT NULL COMMENT '商品ID',
    `order_id`    BIGINT       DEFAULT NULL COMMENT '关联订单ID',
    `content`     VARCHAR(500) NOT NULL COMMENT '评论内容',
    `score`       INT          NOT NULL DEFAULT '5' COMMENT '评分 1-5',
    `status`      TINYINT      NOT NULL DEFAULT '1' COMMENT '状态 1显示 0隐藏',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 11. 收藏表
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `product_id`  BIGINT   NOT NULL COMMENT '商品ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_product` (`user_id`, `product_id`),
    KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 12. 充值记录表
DROP TABLE IF EXISTS `recharge_record`;
CREATE TABLE `recharge_record` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '充值ID',
    `order_no`    VARCHAR(32)   NOT NULL COMMENT '充值流水号',
    `user_id`     BIGINT        NOT NULL COMMENT '用户ID',
    `amount`      DECIMAL(10,2) NOT NULL COMMENT '充值金额',
    `type`        TINYINT       NOT NULL DEFAULT '1' COMMENT '类型 1充值 2消费 3退款',
    `balance_after` DECIMAL(10,2) NOT NULL COMMENT '充值后余额',
    `remark`      VARCHAR(255)  DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_recharge_no` (`order_no`),
    KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表';

-- =====================================================
-- 初始化数据
-- 注意：admin / user 账号密码由应用启动时通过 BCrypt 自动播种
--   默认管理员：admin / admin123
--   默认用户：  test / 123456
-- =====================================================

-- 商品分类
INSERT INTO `category` (`id`, `name`, `sort`) VALUES
(1, '川湘菜', 1), (2, '粤菜', 2), (3, '快餐简餐', 3), (4, '饮品甜品', 4), (5, '夜宵烧烤', 5);

-- 商品
INSERT INTO `product` (`id`, `category_id`, `name`, `price`, `stock`, `sales`, `description`) VALUES
(1, 1, '麻婆豆腐', 12.00, 100, 120, '麻辣鲜香，下饭神器'),
(2, 1, '回锅肉', 18.00, 100, 96, '肥而不腻，川味经典'),
(3, 2, '白切鸡饭', 16.00, 100, 88, '正宗粤式白切鸡'),
(4, 2, '煲仔饭', 15.00, 100, 76, '锅巴香脆，配料丰富'),
(5, 3, '香辣鸡腿堡套餐', 20.00, 200, 210, '汉堡+薯条+可乐'),
(6, 3, '番茄牛腩面', 17.00, 100, 65, '大块牛腩，汤浓味美'),
(7, 4, '珍珠奶茶', 8.00, 300, 320, 'Q弹珍珠，香浓奶茶'),
(8, 4, '杨枝甘露', 12.00, 150, 150, '芒果西柚，清爽解腻'),
(9, 5, '烤鸡翅(4只)', 15.00, 100, 130, '外焦里嫩'),
(10, 5, '烤羊肉串(10串)', 25.00, 100, 90, '孜然飘香');

-- 轮播图
INSERT INTO `banner` (`title`, `image`, `link_url`, `sort`) VALUES
('开学季特惠', '/images/banner1.jpg', '/product/list', 1),
('新店开业', '/images/banner2.jpg', '/product/list', 2),
('满减活动', '/images/banner3.jpg', '/product/list', 3);
