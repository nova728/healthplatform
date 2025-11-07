# 健康管理平台 - 后端

[English](./README_EN.md) | 简体中文

## 项目简介

健康管理平台的后端项目，基于 Spring Boot 构建的 RESTful API 服务。提供完整的用户管理、健康数据管理、社区交互等核心功能，支持高并发访问和实时消息推送。

## 技术栈

<p align="center">
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
<img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
<img src="https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=mybatis&logoColor=white" alt="MyBatis">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
<img src="https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white" alt="WebSocket">
<img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
</p>

### 核心技术

- **Spring Boot 3.2.0** - 快速构建生产级应用的框架
- **Spring Security** - 提供认证和授权功能
- **MyBatis** - 优秀的持久层框架
- **MySQL 8.0** - 关系型数据库
- **Redis** - 缓存和会话管理
- **WebSocket** - 实时消息推送
- **JWT** - 无状态身份验证

### 第三方集成

- **文心一言 API** - AI 健康咨询
- **FatSecret Platform API** - 食物营养数据
- **OpenFDA API** - 药品信息查询

## 核心特性

- **用户管理** - 注册、登录、认证、授权
- **健康数据管理** - 心率、血压、体重、BMI、睡眠等指标管理
- **运动管理** - 运动记录、目标管理、排行榜
- **饮食管理** - 营养摄入追踪、食物数据库
- **用药管理** - 用药记录、提醒服务
- **健康报告** - 智能分析、评分、建议生成
- **社区论坛** - 文章管理、评论、点赞、收藏
- **实时通知** - WebSocket 消息推送

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.9.9+
- MySQL 8.0+
- Redis 6.0+ (可选)

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/nova728/healthplatform
cd healthplatform
```

2. **配置数据库**

创建数据库：
```sql
CREATE DATABASE healthplatform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/healthplatform?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

3. **配置第三方 API**

在 `application.yml` 中配置 API 密钥：
```yaml
api:
  ernie-bot:
    api-key: your_ernie_bot_api_key
  fatsecret:
    consumer-key: your_fatsecret_consumer_key
    consumer-secret: your_fatsecret_consumer_secret
  openfda:
    base-url: https://api.fda.gov
```

4. **启动应用**
```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### 构建项目

```bash
mvn clean package
```

生成的 JAR 文件位于 `target/` 目录。

## 项目结构

```
healthplatform/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/healthplatform/
│   │   │       ├── controller/      # 控制器层
│   │   │       ├── service/         # 业务逻辑层
│   │   │       ├── mapper/          # 数据访问层
│   │   │       ├── entity/          # 实体类
│   │   │       ├── dto/             # 数据传输对象
│   │   │       ├── config/          # 配置类
│   │   │       ├── security/        # 安全配置
│   │   │       ├── websocket/       # WebSocket 配置
│   │   │       └── util/            # 工具类
│   │   └── resources/
│   │       ├── mapper/              # MyBatis XML 映射文件
│   │       ├── application.yml      # 应用配置
│   │       └── application-dev.yml  # 开发环境配置
│   └── test/                        # 测试代码
├── pom.xml                          # Maven 配置
└── README.md
```

## API 文档

### 用户模块

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/user/profile` - 获取用户信息
- `PUT /api/user/profile` - 更新用户信息

### 健康数据模块

- `POST /api/health/record` - 添加健康记录
- `GET /api/health/records` - 获取健康记录列表
- `GET /api/health/report` - 生成健康报告
- `GET /api/health/statistics` - 获取健康统计数据

### 运动模块

- `POST /api/exercise/record` - 添加运动记录
- `GET /api/exercise/records` - 获取运动记录
- `POST /api/exercise/goal` - 设置运动目标
- `GET /api/exercise/leaderboard` - 获取排行榜

### 饮食模块

- `POST /api/diet/record` - 添加饮食记录
- `GET /api/diet/records` - 获取饮食记录
- `GET /api/diet/nutrition` - 获取营养分析
- `GET /api/diet/food/search` - 搜索食物

### 用药模块

- `POST /api/medication/record` - 添加用药记录
- `GET /api/medication/records` - 获取用药记录
- `POST /api/medication/reminder` - 设置用药提醒
- `GET /api/medication/info` - 查询药品信息

### 社区模块

- `POST /api/forum/article` - 发布文章
- `GET /api/forum/articles` - 获取文章列表
- `POST /api/forum/comment` - 发表评论
- `POST /api/forum/like` - 点赞
- `POST /api/forum/favorite` - 收藏

## 技术亮点

### 安全性

- **JWT 认证** - 无状态的身份验证机制
- **密码加密** - BCrypt 加密存储
- **权限控制** - 基于角色的访问控制（RBAC）
- **XSS 防护** - 输入验证和输出编码
- **SQL 注入防护** - 预编译语句

### 性能优化

- **Redis 缓存** - 缓存热点数据
- **数据库连接池** - HikariCP 高性能连接池
- **异步处理** - @Async 注解异步执行
- **分页查询** - MyBatis PageHelper 分页
- **索引优化** - 数据库索引优化

### 高可用性

- **异常处理** - 全局异常处理机制
- **日志记录** - SLF4J + Logback 日志框架
- **健康检查** - Spring Boot Actuator
- **事务管理** - 声明式事务

## 数据库设计

### 核心表结构

- `user` - 用户表
- `health_record` - 健康记录表
- `exercise_record` - 运动记录表
- `diet_record` - 饮食记录表
- `medication_record` - 用药记录表
- `article` - 文章表
- `comment` - 评论表
- `notification` - 通知表

## 开发说明

### 代码规范

项目遵循阿里巴巴 Java 开发规范，使用 CheckStyle 进行代码检查。

### 单元测试

```bash
mvn test
```

### API 测试

推荐使用 Postman 或 Swagger UI 进行 API 测试。

Swagger UI 地址：`http://localhost:8080/swagger-ui.html`

## 部署说明

### Docker 部署

```bash
# 构建镜像
docker build -t healthplatform:latest .

# 运行容器
docker run -d -p 8080:8080 healthplatform:latest
```

### 环境配置

生产环境请使用 `application-prod.yml` 配置文件，并确保：

- 修改数据库连接配置
- 配置 Redis 连接
- 更新 API 密钥
- 启用 HTTPS
- 配置日志级别

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 相关链接

- 前端仓库: [healthplatform_front](https://github.com/nova728/healthplatform_front)

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 联系方式

如有问题或建议，请通过 Issue 联系我们。

---

⭐ 如果这个项目对你有帮助，请给我们一个 Star！
