# 校园外卖平台（毕业设计）

一个前后端分离的校园外卖平台，用户 / 管理员双端，覆盖商品、订单、购物车、评论、收藏、充值、数据统计等 12 个业务模块。

> GitHub：https://github.com/gcz66/probable-happiness

## 技术栈



| 端   | 技术                                                    |
| --- | ----------------------------------------------------- |
| 后端  | Spring Boot 3 + MyBatis + MySQL + JWT + PageHelper    |
| 前端  | Vue 3 + Vite + Element Plus + ECharts + Pinia + Axios |
| 数据库 | MySQL 8                                               |

## 项目结构



```
xiaoyuan-server/   # 后端（Spring Boot 3 + MyBatis）
xiaoyuan-web/      # 前端（Vue 3 + Element Plus + ECharts）
```

## 快速开始

### 1. 初始化数据库



```
-- 在 MySQL 中执行建表脚本（自动创建 xiaoyuan 库 + 12 张表 + 初始化数据）
source xiaoyuan-server/src/main/resources/sql/schema.sql
```

### 2. 启动后端



```
# 1. 修改数据库连接（默认 root/123456）
#    xiaoyuan-server/src/main/resources/application.yml

# 2. 启动（要求 JDK 17+ 和 Maven）
cd xiaoyuan-server
mvn spring-boot:run
# 后端地址：http://localhost:8080
```

后端启动时会自动创建默认账号（BCrypt 加密）：



* 管理员：`admin` / `admin123`

* 普通用户：`test` / `123456`

### 3. 启动前端



```
cd xiaoyuan-web
npm install
npm run dev
# 前端地址：http://localhost:3000（已配置 /api 代理到 8080）
```

### 4. 访问



* 用户端首页：[http://localhost:3000/home](http://localhost:3000/home)

* 用户登录：[http://localhost:3000/login](http://localhost:3000/login)

* 管理端登录：[http://localhost:3000/admin/login](http://localhost:3000/admin/login)（admin / admin123）

## 功能模块

### 用户端



* 商品浏览：轮播图、分类导航、商品列表、商品详情

* 购物车：加入 / 数量调整 / 结算

* 订单：下单（购物车结算）、余额支付、订单列表、取消订单、评价

* 收藏：收藏 / 取消收藏 / 我的收藏

* 个人中心：余额充值、收货地址管理、修改密码

### 管理端



* 数据看板：今日订单、今日营业额、近 7 日趋势、订单状态分布、分类占比、热门商品（ECharts）

* 商品管理、分类管理、轮播图管理、订单管理（接单 / 配送 / 完成）、评论管理、充值记录

## 核心设计

### 数据库（12 张表）

`user` 用户、`admin` 管理员、`category` 分类、`product` 商品、`banner` 轮播图、

`address` 收货地址、`cart` 购物车、`orders` 订单、`order_item` 订单明细、

`comment` 评论、`favorite` 收藏、`recharge_record` 充值记录

### 关键点



* **分页**：PageHelper 实现列表分页，`reasonable: true` 自动纠正越界页码

* **事务**：下单（扣库存 + 写订单 + 写明细 + 清购物车）、支付（扣余额 + 改状态 + 记流水）、充值（加余额 + 记流水）均用 `@Transactional` 保证原子性

* **鉴权**：JWT 生成 token，拦截器解析校验；`/api/**/admin/**` 要求 ADMIN 角色

* **统一返回**：`Result<T>` 统一响应体，`GlobalExceptionHandler` 全局异常处理（业务异常、参数校验、兜底）

* **级联删除**：删除商品时级联清理其评论与收藏，删除分类时若存在商品则拒绝

## 默认账号



| 角色  | 账号    | 密码       |
| --- | ----- | -------- |
| 管理员 | admin | admin123 |
| 用户  | test  | 123456   |

## 环境要求



* JDK 17+

* Maven 3.6+

* MySQL 8.0+

* Node.js 18+