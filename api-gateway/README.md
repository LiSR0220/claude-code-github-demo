# API Gateway - API网关服务

## 简介

API Gateway 是微服务架构的统一入口，负责路由转发、负载均衡、认证鉴权、限流熔断等功能。

## 技术栈

- Spring Cloud Gateway
- Netflix Eureka Client
- Spring Cloud LoadBalancer

## 功能特性

### 1. 路由转发
- 用户服务路由: `/api/v1/users/**` → `user-service`
- 订单服务路由: `/api/v1/orders/**` → `order-service`
- 库存服务路由: `/api/v1/inventory/**` → `inventory-service`

### 2. 负载均衡
使用 `lb://service-name` 自动从 Eureka 获取服务实例并负载均衡。

### 3. 过滤器链

#### 全局过滤器
- **LoggingFilter**: 记录请求响应日志
- **AddHeaderFilter**: 添加网关标识头

#### 自定义过滤器
- **AuthFilter**: JWT 认证校验
- **RateLimitFilter**: 接口限流（基于内存）

### 4. 跨域支持
全局 CORS 配置，支持前后端分离开发。

## 启动服务

```bash
cd api-gateway
mvn spring-boot:run
```

## 测试路由

```bash
# 用户服务（无需认证）
curl http://localhost:8080/api/v1/users/health

# 订单服务（需要认证头）
curl http://localhost:8080/api/v1/orders/1 \
  -H "Authorization: Bearer your-token"
```

## 查看路由信息

```bash
# 查看所有路由
curl http://localhost:8080/actuator/gateway/routes

# 健康检查
curl http://localhost:8080/actuator/health
```

## 配置说明

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: service-name           # 路由ID
          uri: lb://service-name     # 负载均衡到服务
          predicates:                # 断言条件
            - Path=/api/**
          filters:                   # 过滤器
            - StripPrefix=1         # 去掉前缀
            - name: Auth             # 自定义认证过滤器
            - name: RateLimit        # 限流过滤器
```