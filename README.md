# iDME 工业 MiniApp 后端

> 基于华为 iDME (xDM-F) 数字模型引擎的后端管理系统

## 快速开始

### 1. 环境要求
- Java 17+
- Maven 3.6+
- IDE: IntelliJ IDEA（推荐）

### 2. 启动项目
```bash
# 1. 克隆项目
git clone <repository-url>
cd idme

# 2. 编译
mvn clean compile

# 3. 运行
mvn spring-boot:run

# 4. 访问
http://localhost:8080/api/health
```

### 3. 新人入门路线（推荐阅读顺序）
1. **DEVELOPMENT.md** - 了解架构和技术栈（15分钟）
2. **PROJECT_KNOWLEDGE.md** - 掌握关键决策和技术难题（30分钟）
3. **CLAUDE.md** - 学习编码规范和SDK限制（15分钟）
4. **docs/INDEX.md** - 文档导航（按需查阅）

### 4. 核心功能
- ✅ 18个核心模块（14个已验证）
- ✅ 统一DAO层封装SDK调用
- ✅ 完整的DTO/VO转换
- ✅ OpenAPI 3.0规范（含完整示例）
- ✅ 集成测试覆盖

### 5. 技术栈
- Spring Boot 3.2.4
- Java 17
- OkHttp 4.12.0
- Jackson
- JUnit 5

## 文档导航

📚 **完整文档**：[docs/INDEX.md](docs/INDEX.md)

🔑 **核心文档**：
- [PROJECT_KNOWLEDGE.md](PROJECT_KNOWLEDGE.md) - 项目知识库
- [DEVELOPMENT.md](DEVELOPMENT.md) - 开发指南
- [CLAUDE.md](CLAUDE.md) - 编码规范
- [MODULE_VALIDATION_REPORT.md](MODULE_VALIDATION_REPORT.md) - 模块验证报告

📖 **API文档**：
- [docs/openapi.yaml](docs/openapi.yaml) - OpenAPI 3.0规范
- [API_TEST.md](API_TEST.md) - API测试指南

## 项目结构
```
idme/
├── src/main/java/com/tsukilc/idme/
│   ├── controller/      # REST API层
│   ├── service/         # 业务逻辑层
│   ├── dao/             # DAO数据访问层
│   ├── entity/          # SDK实体
│   ├── dto/             # 请求对象
│   ├── vo/              # 响应对象
│   ├── client/          # SDK客户端
│   └── common/          # 公共类
├── src/test/java/com/tsukilc/idme/
│   └── integration/     # 集成测试
├── docs/                # 文档目录
│   ├── openapi.yaml     # API规范
│   └── INDEX.md         # 文档导航
├── DEVELOPMENT.md       # 开发指南
├── PROJECT_KNOWLEDGE.md # 知识库
└── CLAUDE.md            # 编码规范
```

## 开发流程

### 开发新模块
1. 查看依赖关系：MODULE_VALIDATION_REPORT.md
2. 创建Entity/DAO/Service/Controller/DTO/VO
3. 编写集成测试
4. 更新openapi.yaml
5. 更新CLAUDE.md规则库

### 遇到SDK问题
1. 检查CLAUDE.md规则库
2. 查看PROJECT_KNOWLEDGE.md技术难题
3. 参考docs/SDK_VERSION_MANAGEMENT_BUG.md（问题报告范例）

## 许可证
[MIT License](LICENSE)

## 联系方式
- 技术支持：查看DEVELOPMENT.md
- 问题反馈：提交Issue
