# Microservices Demo - 微服务架构演示项目

[![Java](https://img.shields.io/badge/Java-11-blue.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2021.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 项目简介

这是一个用于学习和演示 **Spring Cloud 微服务架构** 的完整示例项目，采用主流的微服务技术栈。

## 架构设计

```
┌─────────────────────────────────────────────────────────────┐
│                        API Gateway                          │
│                   (Spring Cloud Gateway)                    │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
┌───────▼──────┐ ┌────▼────┐ ┌──────▼──────┐
│ User Service │ │  Order  │ │  Inventory  │
│   (用户服务)   │ │ Service │ │   Service   │
└───────┬──────┘ └────┬────┘ └──────┬──────┘
        │             │             │
        └─────────────┼─────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────┐
│                    Service Registry                         │
│                     (Eureka Server)                         │
└─────────────────────────────────────────────────────────────┘
```

## 服务列表

| 服务名 | 端口 | 描述 | 状态 |
|--------|------|------|------|
| user-service | 8081 | 用户服务 | ✅ 开发中 |
| order-service | 8082 | 订单服务 | ⏳ 计划中 |
| inventory-service | 8083 | 库存服务 | ⏳ 计划中 |
| eureka-server | 8761 | 注册中心 | ⏳ 计划中 |
| api-gateway | 8080 | 网关服务 | ⏳ 计划中 |

## 技术栈

### 核心框架
- **Spring Boot 2.7.x** - 应用框架
- **Spring Cloud 2021.x** - 微服务套件
- **Spring Cloud Netflix Eureka** - 服务注册与发现

### 数据存储
- **MySQL 8.0** - 关系型数据库
- **Spring Data JPA** - ORM 框架
- **Redis** - 缓存（规划中）

### 运维监控
- **Spring Boot Actuator** - 健康检查
- **Micrometer + Prometheus** - 监控（规划中）

### 开发工具
- **Maven** - 构建工具
- **Lombok** - 代码简化
- **JUnit 5** - 单元测试

## 快速开始

### 环境要求
- JDK 11+
- Maven 3.6+
- MySQL 8.0+

### 启动步骤

1. **克隆仓库**
```bash
git clone https://github.com/LiSR0220/claude-code-github-demo.git
cd claude-code-github-demo
```

2. **创建数据库**
```sql
CREATE DATABASE user_db CHARACTER SET utf8mb4;
```

3. **启动用户服务**
```bash
cd user-service
mvn spring-boot:run
```

4. **验证服务**
```bash
curl http://localhost:8081/api/v1/users/health
```

## 项目结构

```
.
├── user-service/          # 用户服务
│   ├── src/
│   ├── pom.xml
│   └── README.md
├── order-service/         # 订单服务（待开发）
├── inventory-service/     # 库存服务（待开发）
├── eureka-server/         # 注册中心（待开发）
├── api-gateway/           # API网关（待开发）
└── README.md              # 项目总览
```

## 开发规范

- 遵循 [Alibaba Java Coding Guidelines](https://github.com/alibaba/p3c)
- 分支命名：`feature/service-name` / `bugfix/issue-id`
- Commit 规范：遵循 [Conventional Commits](https://www.conventionalcommits.org/)

## 学习资源

### 微服务核心概念
1. **服务注册与发现** - Eureka
2. **负载均衡** - Ribbon / LoadBalancer
3. **服务调用** - OpenFeign
4. **服务网关** - Gateway
5. **配置中心** - Config / Nacos
6. **熔断限流** - Sentinel / Hystrix

## 贡献指南

欢迎提交 Issue 和 Pull Request！

## 许可证

[MIT](LICENSE)

---

*本项目用于学习 Spring Cloud 微服务架构*