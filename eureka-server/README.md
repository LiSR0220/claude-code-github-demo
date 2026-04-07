# Eureka Server - 服务注册中心

## 简介

Eureka Server 是微服务架构的服务注册中心，所有微服务都在这里注册和发现。

## 启动

```bash
cd eureka-server
mvn spring-boot:run
```

访问控制台：http://localhost:8761

## 注册的服务

| 服务名 | 状态 |
|--------|------|
| user-service | UP |
| order-service | UP |
| api-gateway | UP |

## 集群配置（生产环境）

```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka1:8761/eureka/,http://eureka2:8762/eureka/
```