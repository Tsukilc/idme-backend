# 集成测试文档

## 测试框架说明

本项目使用 Spring Boot Test 框架进行 Controller 层的集成测试，测试环境使用真实的 iDME SDK 服务。

### 技术栈
- **Spring Boot Test** - 应用上下文管理
- **MockMvc** - HTTP 请求模拟
- **JUnit 5** - 测试框架
- **Hamcrest** - 断言库

### 测试配置
- 配置文件：`src/test/resources/application-test.properties`
- 测试端口：8081（避免与开发环境冲突）
- 日志级别：DEBUG（便于调试）

---

## 测试结构

```
src/test/java/com/tsukilc/idme/
├── integration/                         # 集成测试包
│   ├── BaseIntegrationTest.java        # 基类（提供通用配置和工具方法）
│   ├── PartnerContactControllerIntegrationTest.java
│   ├── BusinessPartnerControllerIntegrationTest.java
│   ├── DepartmentControllerIntegrationTest.java
│   └── Phase1IntegrationTestSuite.java # 测试套件
└── IdmeApplicationTests.java            # 应用上下文测试
```

---

## 运行测试

### 方式 1：运行单个测试类（推荐）

在 IDE 中：
1. 打开测试类（如 `PartnerContactControllerIntegrationTest.java`）
2. 右键点击类名 → 选择 **Run 'PartnerContactControllerIntegrationTest'**
3. 查看测试结果

使用 Maven：
```bash
mvn test -Dtest=PartnerContactControllerIntegrationTest
```

### 方式 2：运行 Phase 1 测试套件

在 IDE 中：
1. 打开 `Phase1IntegrationTestSuite.java`
2. 右键点击类名 → 选择 **Run 'Phase1IntegrationTestSuite'**

使用 Maven：
```bash
mvn test -Dtest=Phase1IntegrationTestSuite
```

### 方式 3：运行所有测试

```bash
mvn test
```

---

## Phase 1 测试覆盖

### 1. PartnerContact 联系人接口测试

**测试类**：`PartnerContactControllerIntegrationTest`

| 测试用例 | 接口 | 说明 |
|---------|------|------|
| test01_createPartnerContact | POST /api/partner-contact | 创建联系人 |
| test02_getPartnerContactById | **GET /api/partner-contact/{id}** | **Phase 1 新增：按 ID 查询详情** |
| test03_getPartnerContactById_NotFound | GET /api/partner-contact/{id} | 异常场景：查询不存在的联系人 |
| test04_getPartnerContactsByPartner | GET /api/partner-contact/by-partner/{partnerId} | 按往来单位查询列表 |

### 2. BusinessPartner 往来单位接口测试

**测试类**：`BusinessPartnerControllerIntegrationTest`

| 测试用例 | 接口 | 说明 |
|---------|------|------|
| test01_createManufacturer | POST /api/business-partner | 创建生产厂家 |
| test02_createSupplier | POST /api/business-partner | 创建供应商 |
| test03_createCustomer | POST /api/business-partner | 创建客户 |
| test04_verifyNewPath | **GET /api/business-partner/{id}** | **Phase 1：验证路径对齐** |
| test05_getByType_Manufacturer | **GET /api/business-partner/by-type/{partnerType}** | **Phase 1 新增：按类型查询（Manufacturer）** |
| test06_getByType_Supplier | **GET /api/business-partner/by-type/{partnerType}** | **Phase 1 新增：按类型查询（Supplier）** |
| test07_getByType_Customer | **GET /api/business-partner/by-type/{partnerType}** | **Phase 1 新增：按类型查询（Customer）** |
| test08_getByType_Empty | GET /api/business-partner/by-type/{partnerType} | 空结果场景 |
| test09_listBusinessPartners | GET /api/business-partner | 分页查询列表 |

### 3. Department 部门接口测试

**测试类**：`DepartmentControllerIntegrationTest`

| 测试用例 | 接口 | 说明 |
|---------|------|------|
| test01_createRootDepartment | POST /api/department | 创建根部门 |
| test02_createSubDept1 | POST /api/department | 创建子部门1（研发部） |
| test03_createSubDept2 | POST /api/department | 创建子部门2（市场部） |
| test04_createSubSubDept | POST /api/department | 创建孙部门（软件开发组） |
| test05_getDepartmentTree | **GET /api/department/tree** | **Phase 1 新增：查询部门树** |
| test06_verifyTreeStructure | GET /api/department/tree | 验证树形结构正确性 |
| test07_getDepartmentById | GET /api/department/{id} | 查询部门详情 |

---

## 测试数据管理

所有测试类都遵循以下数据管理原则：

1. **数据准备**：每个测试类在 `@Order(1-4)` 的测试方法中创建测试数据
2. **测试执行**：在 `@Order(5-9)` 的测试方法中执行接口测试
3. **数据清理**：在 `@Order(99)` 的测试方法中删除测试数据

**注意**：测试使用真实的 iDME SDK 服务，数据会持久化到数据库。测试完成后会自动清理数据。

---

## 测试结果示例

运行测试后，控制台输出示例：

```
========== 测试开始 ==========
创建测试往来单位成功，ID: XXX_ID_XXX
创建联系人成功，ID: YYY_ID_YYY
✅ 按 ID 查询联系人详情测试通过
✅ 查询不存在联系人异常处理测试通过
✅ 按往来单位查询联系人列表测试通过
删除测试联系人成功，ID: YYY_ID_YYY
删除测试往来单位成功，ID: XXX_ID_XXX
========== 测试结束 ==========
```

---

## 常见问题

### 1. 测试失败：无法连接 SDK 服务

**原因**：iDME SDK 服务未启动或网络不通

**解决**：
- 检查 SDK 服务地址：http://99.suyiiyii.top:8003
- 确认网络连接正常
- 验证用户名密码正确

### 2. 测试失败：数据已存在

**原因**：上次测试数据未清理

**解决**：
- 手动删除测试数据（通过 SDK 管理界面）
- 或修改测试数据的唯一标识（如 partnerCode）

### 3. 测试超时

**原因**：SDK 响应慢或网络延迟

**解决**：
- 增加超时时间（在 `application-test.properties` 中调整 `idme.sdk.timeout`）
- 检查网络状况

---

## 下一步计划

- [ ] Phase 2 集成测试（关联表 CRUD）
- [ ] Phase 3 集成测试（扩展与统计功能）
- [ ] 性能测试
- [ ] 并发测试

---

## 参考文档

- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [MockMvc Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework)
- [项目接口文档](../../docs/openapi.yaml)
- [SDK 使用注意事项](../../transfer.md)
