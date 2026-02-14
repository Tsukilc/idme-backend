# 已知问题库

> 记录所有已知问题和解决方案，避免重复踩坑

## 📋 问题分类

### ✅ 已解决问题

#### 1. SDK版本对象checkout/update/checkin失败 ✅
- **状态**：已解决（2026-02-13）
- **影响**：Part、WorkingPlan等版本对象
- **根本原因**：SDK的creator/modifier字段长度不一致
- **解决方案**：统一注入creator="xdmAdmin"、modifier="xdmAdmin"
- **详细文档**：[SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md)

#### 2. Employee.hireDate时区问题 ✅
- **状态**：已解决（2026-02-12）
- **影响**：Employee模块
- **根本原因**：SDK期望UTC时间戳，直接发送LocalDateTime导致时区错误
- **解决方案**：在IdmeSdkClient中特殊处理，转为UTC时间戳
- **参考**：CLAUDE.md规则库、MODULE_VALIDATION_REPORT.md

#### 3. Equipment.techParams格式不一致 ✅
- **状态**：已解决（2026-02-14）
- **影响**：Equipment模块
- **根本原因**：SDK返回数组`[{}]`而非对象`{}`
- **解决方案**：Entity中定义为Object类型，前端可传Map，Service自动转为数组格式
- **新增接口**：`GET/PUT /api/equipment/{id}/tech-params`
- **参考**：CLAUDE.md、EquipmentIntegrationTest、test-api-fixes.sh

#### 4. 日期时间序列化错误 ✅
- **状态**：已解决（2026-02-14晚）
- **影响**：Equipment, WorkingProcedure, WorkingPlan等所有包含日期字段的模块
- **根本原因**：前端传日期字符串（如"2025-04-05"），DTO定义为LocalDateTime导致反序列化失败
- **解决方案**：
  - DTO统一使用`LocalDate`类型（productionDate, startTime, endTime, operateTime等）
  - Service层使用`.atStartOfDay()`转为LocalDateTime
  - IdmeSdkClient.convertDateFieldsToTimestamp()统一转Unix时间戳（UTC）
- **涉及字段**：productionDate, startTime, endTime, operateTime, plannedStart/End, actualStart/End
- **参考**：CLAUDE.md、test-api-fixes.sh

#### 5. BOMItem循环引用问题 ✅
- **状态**：已解决（2026-02-14晚）
- **影响**：BOMItem模块树形查询和批量创建
- **根本原因**：BOM结构可能形成环路，导致查询死循环或创建非法数据
- **解决方案**：
  - 创建时检测：wouldCreateCycle()使用DFS+visited集合检测环路，发现环抛异常
  - 查询时防护：visited集合 + MAX_TREE_DEPTH(100)双重防护
  - 本地缓存：bomGraph缓存图结构提升性能，create/update/delete后自动刷新
- **参考**：CLAUDE.md、BOMItemIntegrationTest、test-api-fixes.sh

#### 6. SDK条件查询限制 - 改用find接口 ✅
- **状态**：已解决（2026-02-14晚）
- **影响**：Employee按部门查询
- **根本原因**：findByCondition不支持复杂字段过滤（如dept.id）
- **解决方案**：
  - IdmeSdkClient新增find()方法，支持SDK的find接口
  - EmployeeDao.findByDeptUsingFind()使用filter条件查询
  - 新增接口：`GET /api/employee/by-department/{deptId}`
- **参考**：CLAUDE.md、EmployeeDao.java、test-api-fixes.sh

#### 7. WorkingPlan缺失接口 ✅
- **状态**：已解决（2026-02-14晚）
- **影响**：WorkingPlan模块
- **根本原因**：OpenAPI定义的接口未实现
- **解决方案**：
  - 新增创建新版本接口：`POST /api/working-plan/{id}/new-version`（使用checkout/checkin）
  - 新增查询工序接口：`GET /api/working-plan/{id}/procedures`（返回PlanProcedureItem）
  - 新增Part库存统计接口：`GET /api/part/statistics/stock`
- **参考**：CLAUDE.md、WorkingPlanService.java、test-api-fixes.sh

---

### ⚠️ 限制问题（SDK限制，无法解决）

#### 1. SDK条件查询不支持ObjectReference字段 ⚠️
- **状态**：SDK限制
- **影响**：BusinessPartner.getByType()、Employee.findByDept()
- **现象**：`findByCondition({"partnerType": "Aerospace"})`返回所有数据，过滤无效
- **解决方案**：使用应用层过滤或禁用相关查询方法
- **参考**：CLAUDE.md规则库

#### 2. Part Master ID无法指定 ⚠️
- **状态**：SDK限制
- **影响**：OpenAPI示例值、WorkingPlan引用
- **现象**：Part实例ID可指定，但master.id由SDK自动生成
- **解决方案**：从响应提取实际master ID，更新文档
- **参考**：OpenApiExamplesDataTest.java

---

### 🔄 待验证问题

目前无待验证问题。所有核心功能已验证完成（148个测试全部通过）。

---

## 🔍 问题查找索引

### 按模块分类

**Part（物料）**
- ✅ 版本对象checkout/checkin失败 → SDK_VERSION_MANAGEMENT_BUG.md
- ⚠️ Master ID无法指定 → OpenApiExamplesDataTest.java

**Employee（员工）**
- ✅ hireDate时区问题 → CLAUDE.md、MODULE_VALIDATION_REPORT.md
- ⚠️ 按部门过滤失败 → CLAUDE.md规则库

**Equipment（设备）**
- ✅ techParams格式不一致 → CLAUDE.md
- ✅ 枚举字段处理 → MODULE_VALIDATION_REPORT.md

**BusinessPartner（往来单位）**
- ⚠️ 按partnerType过滤失败 → CLAUDE.md规则库

**VersionObject（通用）**
- ✅ checkout/checkin流程 → SDK_VERSION_MANAGEMENT_BUG.md

---

## 📝 问题报告模板

新问题请按以下格式记录：

```markdown
### 问题标题
- **状态**：🔄 待解决 / ⚠️ SDK限制 / ✅ 已解决
- **影响**：受影响的模块
- **根本原因**：问题根因分析
- **解决方案**：具体解决方案或workaround
- **详细文档**：相关文档链接
```

**参考范例**：[SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md)（完美的问题报告范例）

---

## 最后更新
- **时间**：2026-02-14晚
- **更新内容**：
  - 新增7个已解决问题（日期序列化、BOM循环引用、find接口、新增API等）
  - 更新待验证问题（所有核心功能已验证）
  - 所有集成测试通过（148/148）
