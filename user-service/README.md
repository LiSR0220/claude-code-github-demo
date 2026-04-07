# User Service - 用户服务微服务

## 项目简介

用户服务是微服务架构中的核心服务之一，负责用户的注册、登录、信息管理等基础功能。

## 技术栈

- **框架**: Spring Boot 2.7.x
- **ORM**: Spring Data JPA
- **数据库**: MySQL 8.0
- **服务注册**: Eureka Client
- **构建工具**: Maven
- **JDK**: 11

## 项目结构

```
user-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/userservice/
│   │   │   ├── UserServiceApplication.java    # 启动类
│   │   │   ├── config/                        # 配置类
│   │   │   ├── controller/                    # REST API
│   │   │   ├── service/                       # 业务逻辑
│   │   │   │   └── impl/                      # 实现类
│   │   │   ├── repository/                    # 数据访问
│   │   │   ├── entity/                        # 实体类
│   │   │   ├── dto/                           # 数据传输对象
│   │   │   └── exception/                     # 异常处理
│   │   └── resources/
│   │       ├── application.yml                # 主配置
│   │       ├── application-dev.yml            # 开发环境
│   │       └── application-prod.yml           # 生产环境
│   └── test/                                  # 测试代码
├── pom.xml                                    # Maven配置
└── README.md                                  # 项目说明
```

## 快速开始

### 1. 数据库准备

```sql
CREATE DATABASE user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置修改

编辑 `application-dev.yml`，修改数据库连接信息。

### 3. 启动服务

```bash
# 编译
mvn clean package

# 运行
mvn spring-boot:run

# 或直接运行 jar
java -jar target/user-service-1.0.0-SNAPSHOT.jar
```

### 4. 验证服务

```bash
# 健康检查
curl http://localhost:8081/api/v1/users/health

# 创建用户
curl -X POST http://localhost:8081/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com"}'
```

## API 文档

### 用户管理接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/v1/users | 创建用户 |
| GET | /api/v1/users/{id} | 根据ID查询 |
| GET | /api/v1/users/username/{username} | 根据用户名查询 |
| GET | /api/v1/users | 分页查询列表 |
| PUT | /api/v1/users/{id} | 更新用户 |
| DELETE | /api/v1/users/{id} | 删除用户 |
| PATCH | /api/v1/users/{id}/status | 更新状态 |
| GET | /api/v1/users/health | 健康检查 |

### 分页查询参数

```
GET /api/v1/users?page=0&size=10&sort=id,desc&keyword=test&status=1
```

- `page`: 页码，从0开始
- `size`: 每页条数
- `sort`: 排序字段和方向
- `keyword`: 搜索关键词（用户名/昵称）
- `status`: 用户状态筛选

## 开发规范

- 遵循 Alibaba Java Coding Guidelines
- 使用 Lombok 简化代码
- RESTful API 设计规范
- 统一返回格式
- 完善单元测试

## 后续规划

- [ ] 集成 Spring Security 实现认证授权
- [ ] 添加 Redis 缓存支持
- [ ] 集成 Swagger 生成 API 文档
- [ ] 添加 Docker 支持
- [ ] 配置中心集成（Spring Cloud Config）

---

*创建于 2026-04-07*