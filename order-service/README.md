# Order Service - 订单服务

## 简介

订单服务是微服务架构中的核心业务服务，负责订单的创建、查询、状态管理等。通过 **OpenFeign** 实现与用户服务的调用。

## 技术栈

- Spring Boot 2.7.x
- Spring Data JPA
- **OpenFeign**（服务间调用）
- Eureka Client
- MySQL 8.0

## OpenFeign 集成

### 1. Feign 客户端定义

```java
@FeignClient(name = "user-service", fallbackFactory = ...)
public interface UserServiceFeignClient {
    
    @GetMapping("/api/v1/users/{id}")
    UserInfoDTO getUserById(@PathVariable("id") Long id);
}
```

### 2. 启用 Feign

```java
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.orderservice.feign")
public class OrderServiceApplication { ... }
```

### 3. 服务降级

当用户服务不可用时，自动触发降级逻辑，返回默认用户信息。

## API 列表

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/v1/orders | 创建订单 |
| GET | /api/v1/orders/{id} | 查询订单（含用户信息）|
| GET | /api/v1/orders/orderNo/{orderNo} | 根据订单号查询 |
| GET | /api/v1/orders/user/{userId} | 查询用户订单 |
| GET | /api/v1/orders | 搜索订单 |
| PATCH | /api/v1/orders/{id}/status | 更新状态 |
| POST | /api/v1/orders/{id}/cancel | 取消订单 |

## 测试 Feign 调用

```bash
# 1. 先创建用户
curl -X POST http://localhost:8081/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com"}'

# 2. 创建订单（会自动调用用户服务）
curl -X POST http://localhost:8082/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productName": "iPhone 15",
    "quantity": 1,
    "totalAmount": 5999.00,
    "address": "北京市朝阳区"
  }'

# 3. 查询订单（返回结果包含用户信息）
curl http://localhost:8082/api/v1/orders/1
```

## 服务调用链路

```
用户请求 → Order Service → Feign → User Service
                ↓
           订单信息 + 用户信息（聚合）
```