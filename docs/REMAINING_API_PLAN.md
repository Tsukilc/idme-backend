# 剩余接口完成计划 (REMAINING API PLAN)

## 背景与前提

### SDK 与数据模型约定
- **引用对象格式**：SDK 要求引用类型字段必须使用 `{ id, clazz, name }` 格式（详见 [transfer.md](../transfer.md)）
- **schema 约定**（docs/schema.md）：
  - 生产厂家 `manufacturerName`、供应商 `supplierName` 均为引用 BusinessPartner
  - 设备分类 `category` 为 EquipmentClassfication
  - 这些主数据已可创建（SDK 已修复）
- **必填字段**：以 SDK 实际校验为准（详见 transfer.md 第 2 节）
- **枚举值**：必须使用 SDK 规定的英文值，不要传中文（详见 transfer.md 第 3 节）

### 目标
对照 `docs/openapi.yaml` 补全当前未实现或未对齐的接口，按 Phase 1/2/3 顺序实现。

### 接口规范唯一标准
**以 docs/openapi.yaml 为唯一接口标准**，所有路径、参数、响应格式必须严格遵循。

---

## 现状与缺口总览

根据 openapi.yaml 对比现有 Controller 实现，缺失/需对齐的接口共 **27 项**，按模块分类如下：

| 模块 | 缺失/需补接口数量 | 说明 |
|------|-------------------|------|
| PartnerContact | 1 | 缺少按 ID 查询详情 |
| BusinessPartner | 2 | 路径对齐 + 按类型查询 |
| Department | 1 | 缺少部门树 |
| Equipment | 3 | tech-params 查询/更新 + 统计 |
| Part | 4 | 版本管理 + 分类查询 + 库存统计 |
| WorkingPlan | 3 | 版本管理 + 工序列表 + 设备使用情况 |
| BOMItem | 1 | where-used 反向查询 |
| PlanProcedureLink | 3 | batch + 按 ID 查询/更新 |
| ProcedureEquipmentLink | 2 | 按 ID 查询 + 更新实际时间 |
| ProcedurePartLink | 1 | 按 ID 查询 |
| EquipmentSparePartLink | 3 | by-part 查询 + 按 ID 查询/更新 |

---

## 实现计划（按 Phase 1/2/3）

### Phase 1：基础接口补全（主数据与路径对齐）

优先级最高，完成基础查询和路径对齐，影响其他模块依赖。

#### 1.1 PartnerContact - 按 ID 查询详情

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/partner-contact/{id}` |
| **operationId** | `getPartnerContact` (openapi.yaml:1915) |
| **实现位置** | `PartnerContactController` |
| **实现要点** | 新增 `getById` 方法，调用 `PartnerContactService.getById(id)` |
| **依赖** | 无 |
| **返回** | `ApiResponse<PartnerContact>` |
| **测试点** | 创建联系人后，按 ID 查询验证 |

#### 1.2 BusinessPartner - 路径对齐

| 项目 | 内容 |
|------|------|
| **接口** | 所有接口路径从 `/api/businesspartner` 改为 `/api/business-partner` |
| **实现位置** | `BusinessPartnerController` |
| **实现要点** | 修改 `@RequestMapping("/api/business-partner")` |
| **影响范围** | 所有 BusinessPartner 接口 |
| **测试点** | 验证新路径 `/api/business-partner` 可访问 |

#### 1.3 BusinessPartner - 按类型查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/business-partner/by-type/{partnerType}` |
| **operationId** | `listBusinessPartnerByType` (openapi.yaml:1850) |
| **实现位置** | `BusinessPartnerController` |
| **实现要点** | 新增 `getByType` 方法，调用 SDK condition 查询 `partnerType` 等于指定值 |
| **依赖** | 无 |
| **返回** | `ApiResponse<List<BusinessPartner>>` |
| **测试点** | 按 `Manufacturer`、`Supplier` 等类型查询 |

#### 1.4 Department - 部门树查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/department/tree` |
| **operationId** | `getDepartmentTree` (openapi.yaml:1959) |
| **实现位置** | `DepartmentController` |
| **实现要点** | 新增 `getTree` 方法，参考 `LocationController.getTree` 实现部门树构建 |
| **依赖** | 无 |
| **返回** | `ApiResponse<List<DepartmentTreeNode>>` |
| **测试点** | 创建根部门和子部门后，验证树形结构 |

---

### Phase 2：关联表 CRUD 与双向查询补全

完成关联表的完整 CRUD 和双向查询能力。

#### 2.1 PlanProcedureLink - 批量添加工序

| 项目 | 内容 |
|------|------|
| **接口** | `POST /api/plan-procedure-link/batch` |
| **operationId** | `batchCreatePlanProcedureLink` (openapi.yaml:1482) |
| **实现位置** | `PlanProcedureLinkController` |
| **实现要点** | 新增 `batchCreate` 方法，接收 `PlanProcedureLinkBatch` DTO（包含 `planId` 和 `procedures` 数组），循环调用 Service.create |
| **依赖** | 无 |
| **返回** | `ApiResponse<List<String>>` (创建的 ID 列表) |
| **DTO 定义** | `PlanProcedureLinkBatchDTO` { planId, procedures: [{ procedureId, sequenceNo, standardDurationMin, requirement }] } |
| **测试点** | 批量创建 3 个工序关联，验证顺序号 |

#### 2.2 PlanProcedureLink - 按 ID 查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/plan-procedure-link/{id}` |
| **operationId** | 从 openapi.yaml 推断（路径 1523 行有定义，但只有 PUT 和 DELETE） |
| **实现位置** | `PlanProcedureLinkController` |
| **实现要点** | 新增 `getById` 方法，调用 Service.getById |
| **依赖** | 无 |
| **返回** | `ApiResponse<PlanProcedureLink>` |
| **注意** | openapi.yaml 中该路径只定义了 PUT 和 DELETE，建议补充 GET 方法以保持 RESTful 一致性 |

#### 2.3 PlanProcedureLink - 更新顺序

| 项目 | 内容 |
|------|------|
| **接口** | `PUT /api/plan-procedure-link/{id}` |
| **operationId** | `updatePlanProcedureLinkSequence` (openapi.yaml:1525) |
| **实现位置** | `PlanProcedureLinkController` |
| **实现要点** | 新增 `updateSequence` 方法，接收 `SequenceUpdate` DTO（仅 sequenceNo 字段），调用 Service.updateSequence |
| **依赖** | 无 |
| **返回** | `ApiResponse<Void>` |
| **DTO 定义** | `SequenceUpdateDTO` { sequenceNo: Integer } |
| **测试点** | 更新工序顺序号，验证工序列表排序变化 |

#### 2.4 ProcedureEquipmentLink - 按 ID 查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/procedure-equipment-link/{id}` |
| **operationId** | 从 openapi.yaml 推断（路径 1648 行只有 DELETE，建议补充） |
| **实现位置** | `ProcedureEquipmentLinkController` |
| **实现要点** | 新增 `getById` 方法，调用 Service.getById |
| **依赖** | 无 |
| **返回** | `ApiResponse<ProcedureEquipmentLink>` |

#### 2.5 ProcedureEquipmentLink - 更新实际时间

| 项目 | 内容 |
|------|------|
| **接口** | `PATCH /api/procedure-equipment-link/{id}/actual-time` |
| **operationId** | `updateProcedureEquipmentActualTime` (openapi.yaml:1567) |
| **实现位置** | `ProcedureEquipmentLinkController` |
| **实现要点** | 新增 `updateActualTime` 方法，接收 `ProcedureStatusUpdate` DTO，调用 Service.updateActualTime |
| **依赖** | 无 |
| **返回** | `ApiResponse<Void>` |
| **DTO 定义** | `ProcedureStatusUpdateDTO` { actualStart, actualEnd }（参考 openapi.yaml:687） |
| **测试点** | 更新实际开始和结束时间 |

#### 2.6 ProcedurePartLink - 按 ID 查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/procedure-part-link/{id}` |
| **operationId** | 从 openapi.yaml 推断（路径 1745 行只有 DELETE，建议补充） |
| **实现位置** | `ProcedurePartLinkController` |
| **实现要点** | 新增 `getById` 方法，调用 Service.getById |
| **依赖** | 无 |
| **返回** | `ApiResponse<ProcedurePartLink>` |

#### 2.7 EquipmentSparePartLink - 按物料查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/equipment-spare-part-link/by-part/{partId}` |
| **operationId** | `getEquipmentSparePartByPart` (openapi.yaml:2369) |
| **实现位置** | `EquipmentSparePartLinkController` |
| **实现要点** | 新增 `getByPart` 方法，调用 Service.getByPart（使用 SDK condition 查询 sparePart=partId） |
| **依赖** | 无 |
| **返回** | `ApiResponse<List<EquipmentSparePartLinkItem>>` |
| **测试点** | 查询某物料作为备件被哪些设备使用 |

#### 2.8 EquipmentSparePartLink - 按 ID 查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/equipment-spare-part-link/{id}` |
| **operationId** | 从 openapi.yaml 推断（路径 2393 行有 PUT 和 DELETE，建议补充 GET） |
| **实现位置** | `EquipmentSparePartLinkController` |
| **实现要点** | 新增 `getById` 方法，调用 Service.getById |
| **依赖** | 无 |
| **返回** | `ApiResponse<EquipmentSparePartLink>` |

#### 2.9 EquipmentSparePartLink - 更新备件信息

| 项目 | 内容 |
|------|------|
| **接口** | `PUT /api/equipment-spare-part-link/{id}` |
| **operationId** | `updateEquipmentSparePartLink` (openapi.yaml:2395) |
| **实现位置** | `EquipmentSparePartLinkController` |
| **实现要点** | 新增 `update` 方法，调用 Service.update |
| **依赖** | 无 |
| **返回** | `ApiResponse<Void>` |
| **测试点** | 更新备件数量、更换周期等信息 |

---

### Phase 3：扩展与统计功能

完成高级查询、统计和版本管理功能。

#### 3.1 Equipment - 查询技术参数

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/equipment/{id}/tech-params` |
| **operationId** | `getEquipmentTechParams` (openapi.yaml:828) |
| **实现位置** | `EquipmentController` |
| **实现要点** | 新增 `getTechParams` 方法，读取设备的 `techParams` 字段（JSON 对象）并返回 |
| **依赖** | Equipment 已有 techParams 字段 |
| **返回** | `ApiResponse<Map<String, Object>>` (技术参数 JSON) |
| **测试点** | 查询设备的 maxSpeed、power、precision 等参数 |

#### 3.2 Equipment - 更新技术参数

| 项目 | 内容 |
|------|------|
| **接口** | `PUT /api/equipment/{id}/tech-params` |
| **operationId** | `updateEquipmentTechParams` (openapi.yaml:848) |
| **实现位置** | `EquipmentController` |
| **实现要点** | 新增 `updateTechParams` 方法，更新设备的 `techParams` 字段 |
| **依赖** | 无 |
| **返回** | `ApiResponse<Void>` |
| **测试点** | 更新技术参数后再查询验证 |

#### 3.3 Equipment - 按状态统计

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/equipment/statistics/by-status` |
| **operationId** | `getEquipmentStatisticsByStatus` (openapi.yaml:865) |
| **实现位置** | `EquipmentController` |
| **实现要点** | 新增 `getStatisticsByStatus` 方法，查询所有设备，按 `status` 字段分组统计数量 |
| **依赖** | 无 |
| **返回** | `ApiResponse<Map<String, Integer>>` (如 { "运行": 10, "维修": 2, "停机": 1 }) |
| **测试点** | 创建不同状态的设备后统计 |

#### 3.4 BOMItem - 反向查询 where-used

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/bom-item/where-used/{partId}` |
| **operationId** | `getBOMWhereUsed` (openapi.yaml:1441) |
| **实现位置** | `BOMItemController` |
| **实现要点** | 新增 `getWhereUsed` 方法，查询 `childPart=partId` 的所有 BOM 项（反向：该物料被哪些父项使用） |
| **依赖** | 无 |
| **返回** | `ApiResponse<List<BOMItem>>` |
| **测试点** | 查询某子件被哪些父件使用 |

#### 3.5 WorkingPlan - 查询工序列表

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/working-plan/{id}/procedures` |
| **operationId** | `getWorkingPlanProcedures` (openapi.yaml:1295) |
| **实现位置** | `WorkingPlanController` |
| **实现要点** | 新增 `getProcedures` 方法，调用 `PlanProcedureLinkService.getByPlan(id)` 并转换为 `PlanProcedureItem` 列表（含 sequenceNo、procedure 引用、standardDurationMin、requirement） |
| **依赖** | PlanProcedureLinkService.getByPlan 已实现 |
| **返回** | `ApiResponse<List<PlanProcedureItem>>` |
| **测试点** | 查询工艺路线的所有工序，验证按顺序排列 |

#### 3.6 WorkingPlan - 查询设备使用情况

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/working-plan/{id}/equipment-usage` |
| **operationId** | `getWorkingPlanEquipmentUsage` (openapi.yaml:1315) |
| **实现位置** | `WorkingPlanController` |
| **实现要点** | 新增 `getEquipmentUsage` 方法，方案：<br>1. 查询该工艺路线的所有工序（通过 PlanProcedureLink）<br>2. 查询这些工序关联的所有设备（通过 ProcedureEquipmentLink）<br>3. 统计每个设备/设备类型的使用次数，返回 Map |
| **依赖** | PlanProcedureLinkService、ProcedureEquipmentLinkService |
| **返回** | `ApiResponse<Map<String, Integer>>` (设备名称/类型 -> 使用次数) |
| **测试点** | 验证设备使用统计数据 |

#### 3.7 WorkingPlan - 创建新版本

| 项目 | 内容 |
|------|------|
| **接口** | `POST /api/working-plan/{id}/new-version` |
| **operationId** | `createWorkingPlanNewVersion` (openapi.yaml:1282) |
| **实现位置** | `WorkingPlanController` |
| **实现要点** | 新增 `createNewVersion` 方法，调用 SDK 的版本创建接口（依赖 SDK 版本管理能力） |
| **依赖** | **依赖 SDK 版本管理接口**（参考 [SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md)） |
| **返回** | `ApiResponse<String>` (新版本 ID) |
| **测试点** | 创建工艺路线新版本，验证 businessVersion 变化 |
| **注意** | 若 SDK 版本接口未就绪，此接口可暂缓实现，标注 "依赖 SDK" |

#### 3.8 Part - 创建新版本

| 项目 | 内容 |
|------|------|
| **接口** | `POST /api/part/{id}/new-version` |
| **operationId** | `createPartNewVersion` (openapi.yaml:978) |
| **实现位置** | `PartController` |
| **实现要点** | 新增 `createNewVersion` 方法，调用 SDK 的版本创建接口 |
| **依赖** | **依赖 SDK 版本管理接口** |
| **返回** | `ApiResponse<String>` (新版本 ID) |
| **请求体** | `PartNewVersionDTO` { businessVersion, description } |
| **测试点** | 创建物料新版本，验证版本号递增 |
| **注意** | 若 SDK 版本接口未就绪，可暂缓实现 |

#### 3.9 Part - 查询所有版本

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/part/{masterId}/versions` |
| **operationId** | `getPartVersions` (openapi.yaml:995) |
| **实现位置** | `PartController` |
| **实现要点** | 新增 `getVersions` 方法，查询 `masterId` 下的所有版本（调用 SDK 版本查询接口） |
| **依赖** | **依赖 SDK 版本管理接口** |
| **返回** | `ApiResponse<List<PartVersionItem>>` (id, businessVersion, versionNumber, createTime) |
| **测试点** | 查询物料的所有版本列表 |

#### 3.10 Part - 按分类查询

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/part/by-category/{categoryPath}` |
| **operationId** | `listPartByCategory` (openapi.yaml:1019) |
| **实现位置** | `PartController` |
| **实现要点** | 新增 `listByCategory` 方法，使用 SDK condition 查询 `category` 匹配指定 categoryPath（需与前端/SDK 约定分类路径格式，如 "结构件/高强钢安装件"） |
| **依赖** | 需确认 SDK 分类字段查询格式 |
| **返回** | `ApiResponse<List<Part>>` |
| **测试点** | 按分类路径查询物料 |
| **注意** | categoryPath 格式需与 schema/前端约定 |

#### 3.11 Part - 库存统计

| 项目 | 内容 |
|------|------|
| **接口** | `GET /api/part/statistics/stock` |
| **operationId** | `getPartStockStatistics` (openapi.yaml:1044) |
| **实现位置** | `PartController` |
| **实现要点** | 新增 `getStockStatistics` 方法，查询所有物料，按分类或 ID 聚合 `stockQty` |
| **依赖** | 无 |
| **返回** | `ApiResponse<Map<String, Double>>` (物料分类/ID -> 库存数量) |
| **测试点** | 验证库存统计数据 |

---

## 实现顺序建议

```
Phase 1（基础）
  ├─ 1.1 PartnerContact GET by id
  ├─ 1.2 BusinessPartner path align
  ├─ 1.3 BusinessPartner by-type
  └─ 1.4 Department tree

Phase 2（关联补全）
  ├─ 2.1 PlanProcedureLink batch
  ├─ 2.2 PlanProcedureLink GET by id
  ├─ 2.3 PlanProcedureLink PUT by id
  ├─ 2.4 ProcedureEquipmentLink GET by id
  ├─ 2.5 ProcedureEquipmentLink PATCH actual-time
  ├─ 2.6 ProcedurePartLink GET by id
  ├─ 2.7 EquipmentSparePartLink by-part
  ├─ 2.8 EquipmentSparePartLink GET by id
  └─ 2.9 EquipmentSparePartLink PUT by id

Phase 3（扩展与统计）
  ├─ 3.1 Equipment tech-params GET
  ├─ 3.2 Equipment tech-params PUT
  ├─ 3.3 Equipment statistics by-status
  ├─ 3.4 BOMItem where-used
  ├─ 3.5 WorkingPlan procedures
  ├─ 3.6 WorkingPlan equipment-usage
  ├─ 3.7 WorkingPlan new-version (依赖 SDK)
  ├─ 3.8 Part new-version (依赖 SDK)
  ├─ 3.9 Part versions (依赖 SDK)
  ├─ 3.10 Part by-category
  └─ 3.11 Part stock statistics
```

---

## 实现要点与注意事项

### 通用要点
1. **严格遵循 openapi.yaml**：所有路径、参数名、响应格式必须与 openapi.yaml 一致
2. **引用对象序列化**：使用 `ObjectReference` 表示引用，并确保 `ObjectReferenceSerializer` 输出 `{ id, clazz, name }` 格式（见 [transfer.md](../transfer.md) 第 1.3 节）
3. **必填字段校验**：DTO 上使用 `@NotBlank`/`@NotNull` 标注必填字段
4. **枚举值使用**：传递给 SDK 的枚举值必须为英文（如 `InOperation`、`Standby`），不要传中文
5. **异常处理**：统一使用 `ApiResponse` 包装响应，Service 层抛出业务异常由全局异常处理器捕获

### Controller 层规范
- **路径命名**：严格使用 openapi.yaml 中定义的路径（如 `/api/business-partner`，不是 `/api/businesspartner`）
- **方法命名**：建议与 operationId 保持一致（如 `listBusinessPartnerByType`）
- **日志记录**：使用 `@Slf4j` 记录关键操作（创建、更新、删除）
- **参数校验**：使用 `@Validated` 或 `@Valid` 校验请求体

### Service 层规范
- **SDK 调用**：通过 `SdkClient` 调用 SDK 接口
- **DTO/VO 转换**：Controller 接收 DTO，Service 返回 VO
- **条件查询**：使用 SDK `Condition` API 构建查询条件
- **分页查询**：使用 SDK `PageCondition` API

### DTO 定义规范
- **命名规范**：`{EntityName}CreateDTO`、`{EntityName}UpdateDTO`
- **字段说明**：使用 `@Schema` 注解说明字段含义
- **校验注解**：使用 `@NotBlank`、`@NotNull`、`@Min`、`@Max` 等

---

## 依赖 SDK 能力的接口

以下接口依赖 SDK 特定能力，若 SDK 未就绪可暂缓实现：

| 接口 | 依赖能力 | 参考文档 |
|------|----------|----------|
| 3.7 WorkingPlan new-version | SDK 版本管理接口 | [SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md) |
| 3.8 Part new-version | SDK 版本管理接口 | [SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md) |
| 3.9 Part versions | SDK 版本查询接口 | [SDK_VERSION_MANAGEMENT_BUG.md](SDK_VERSION_MANAGEMENT_BUG.md) |
| 3.10 Part by-category | SDK 分类查询格式约定 | 需与前端确认分类路径格式 |

---

## 测试计划

每个接口实现后，在 [API_TEST.md](../API_TEST.md) 中补充对应的测试用例（curl 命令）。

### 测试数据准备顺序
1. **基础数据**：BusinessPartner、Department、Location、Employee
2. **主实体**：Equipment、Part、WorkingProcedure、WorkingPlan
3. **关联关系**：BOMItem、PlanProcedureLink、ProcedureEquipmentLink、ProcedurePartLink、EquipmentSparePartLink

### 测试覆盖点
- **CRUD 完整性**：创建 -> 查询 -> 更新 -> 删除
- **关联查询**：双向查询（如 by-plan、by-procedure）
- **分页功能**：验证 pageNum、pageSize、total
- **数据校验**：必填字段、枚举值、引用对象格式
- **业务逻辑**：版本管理、树形查询、统计功能

---

## 关键文件索引

| 用途 | 文件路径 |
|------|----------|
| 接口规范（唯一标准） | `docs/openapi.yaml` |
| 数据模型与引用约定 | `docs/schema.md` |
| SDK 使用注意事项 | `transfer.md` |
| SDK 版本管理问题 | `docs/SDK_VERSION_MANAGEMENT_BUG.md` |
| API 测试用例 | `API_TEST.md` |
| Controller 示例 | `src/main/java/com/tsukilc/idme/controller/` |
| Service 示例 | `src/main/java/com/tsukilc/idme/service/` |
| DTO 示例 | `src/main/java/com/tsukilc/idme/dto/` |
| VO 示例 | `src/main/java/com/tsukilc/idme/vo/` |

---

## 完成检查清单

### Phase 1 完成标准
- [ ] PartnerContact GET by id 接口可用
- [ ] BusinessPartner 路径改为 `/api/business-partner`
- [ ] BusinessPartner by-type 接口可用
- [ ] Department tree 接口返回正确树形结构

### Phase 2 完成标准
- [ ] PlanProcedureLink batch、GET、PUT 接口可用
- [ ] ProcedureEquipmentLink GET、PATCH actual-time 接口可用
- [ ] ProcedurePartLink GET by id 接口可用
- [ ] EquipmentSparePartLink by-part、GET、PUT 接口可用

### Phase 3 完成标准
- [ ] Equipment tech-params GET/PUT 接口可用
- [ ] Equipment statistics by-status 返回正确统计数据
- [ ] BOMItem where-used 反向查询可用
- [ ] WorkingPlan procedures、equipment-usage 接口可用
- [ ] Part new-version、versions、by-category、stock statistics 接口可用（或标注 "依赖 SDK"）
- [ ] WorkingPlan new-version 接口可用（或标注 "依赖 SDK"）

### 整体完成标准
- [ ] 所有接口路径、参数、响应格式与 openapi.yaml 完全一致
- [ ] 所有接口在 API_TEST.md 中有对应测试用例
- [ ] 引用对象序列化格式正确（`{ id, clazz, name }`）
- [ ] 枚举值使用英文而非中文
- [ ] 所有 Controller 有日志记录
- [ ] 所有 DTO 有校验注解

---

## 附录：openapi.yaml 路径快速索引

| 接口 | operationId | 行号 |
|------|-------------|------|
| GET /api/partner-contact/{id} | - | 1915 |
| GET /api/business-partner/by-type/{partnerType} | listBusinessPartnerByType | 1850 |
| GET /api/department/tree | getDepartmentTree | 1959 |
| GET /api/equipment/{id}/tech-params | getEquipmentTechParams | 828 |
| PUT /api/equipment/{id}/tech-params | updateEquipmentTechParams | 848 |
| GET /api/equipment/statistics/by-status | getEquipmentStatisticsByStatus | 865 |
| POST /api/part/{id}/new-version | createPartNewVersion | 978 |
| GET /api/part/{masterId}/versions | getPartVersions | 995 |
| GET /api/part/by-category/{categoryPath} | listPartByCategory | 1019 |
| GET /api/part/statistics/stock | getPartStockStatistics | 1044 |
| POST /api/working-plan/{id}/new-version | createWorkingPlanNewVersion | 1282 |
| GET /api/working-plan/{id}/procedures | getWorkingPlanProcedures | 1295 |
| GET /api/working-plan/{id}/equipment-usage | getWorkingPlanEquipmentUsage | 1315 |
| GET /api/bom-item/where-used/{partId} | getBOMWhereUsed | 1441 |
| POST /api/plan-procedure-link/batch | batchCreatePlanProcedureLink | 1482 |
| PUT /api/plan-procedure-link/{id} | updatePlanProcedureLinkSequence | 1525 |
| PATCH /api/procedure-equipment-link/{id}/actual-time | updateProcedureEquipmentActualTime | 1567 |
| GET /api/equipment-spare-part-link/by-part/{partId} | getEquipmentSparePartByPart | 2369 |
| PUT /api/equipment-spare-part-link/{id} | updateEquipmentSparePartLink | 2395 |

---

**文档版本**：v1.0
**创建日期**：2026-02-13
**最后更新**：2026-02-13
**维护者**：Claude Code
