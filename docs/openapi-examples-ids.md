# OpenAPI 示例数据固定ID文档

本文档记录了 `OpenApiExamplesDataTest` 中使用的所有固定ID及其用途。

**生成时间**: 2026-02-14
**测试文件**: `/src/test/java/com/tsukilc/idme/integration/OpenApiExamplesDataTest.java`
**测试状态**: ✅ 7/7 测试全部通过

---

## ID设计规范

### 格式要求
- **必须使用纯数字ID** - SDK要求（不能包含字母、横线等特殊字符）
- **长度**: 10-16位数字
- **分段规则**: 按模块使用不同的亿段，避免ID冲突

### 分段设计
```
10亿段: Unit（1000000001-1999999999）
20亿段: BusinessPartner（2000000001-2999999999）
30亿段: 分类（3000000001-3999999999）
40亿段: Department/Employee（4000000001-4999999999）
50亿段: Model/Location（5000000001-5999999999）
60亿段: Equipment（6000000001-6999999999）
70亿段: Part（7000000001-7999999999）
80亿段: Procedure/Plan（8000000001-8999999999）
```

---

## 固定ID清单

### Level 0 - 基础主数据（无依赖）

| 模块 | 常量名 | ID值 | 用途（在哪些接口的example中使用） |
|------|--------|------|----------------------------------|
| **Unit** | `UNIT_DEMO_ID` | `1000000001` | PartCreate.unit, BOMItemCreate.uom, EquipmentSparePartLinkCreate.unit |
| **BusinessPartner** | `MANUFACTURER_DEMO_ID` | `2000000001` | EquipmentCreate.manufacturerName, EquipmentModelCreate.manufacturer |
| **BusinessPartner** | `SUPPLIER_DEMO_ID` | `2000000002` | EquipmentCreate.supplierName, PartCreate.supplierName |
| **EquipmentClassfication** | `EQUIP_CLASS_DEMO_ID` | `3000000001` | EquipmentCreate.category, EquipmentModelCreate.category |
| **PartClassfication** | `PART_CLASS_DEMO_ID` | `3000000002` | PartCreate.category |

### Level 1 - 组织架构（依赖Level 0）

| 模块 | 常量名 | ID值 | 用途 |
|------|--------|------|------|
| **Department** | `DEPT_DEMO_ID` | `4000000001` | EmployeeCreate.dept, DepartmentCreate.parentDept (可选) |
| **Employee** | `EMPLOYEE_DEMO_ID` | `4000000002` | LocationCreate.manager, DepartmentCreate.manager, WorkingProcedureCreate.operatorRef, WorkingPlanCreate.operatorRef |

### Level 2 - 设备型号和位置（依赖Level 0-1）

| 模块 | 常量名 | ID值 | 用途 |
|------|--------|------|------|
| **EquipmentModel** | `EQUIP_MODEL_DEMO_ID` | `5000000001` | EquipmentCreate.equipmentModelRef (SDK必填) |
| **Location** | `LOCATION_DEMO_ID` | `5000000002` | EquipmentCreate.locationRef (SDK必填) |

### Level 3 - 设备和物料（依赖Level 0-2）

| 模块 | 常量名 | ID值 | 用途 |
|------|--------|------|------|
| **Equipment** (生产设备) | `EQUIPMENT_PROD_DEMO_ID` | `6000000001` | WorkingProcedureCreate.mainProductionEquipment, ProcedureEquipmentLinkCreate.equipment1 |
| **Equipment** (检测设备) | `EQUIPMENT_INSP_DEMO_ID` | `6000000002` | WorkingProcedureCreate.mainInspectionEquipment |
| **Part** | `PART_DEMO_ID` | `7000000001` | BOMItemCreate.childPart, ProcedurePartLinkCreate.part1, EquipmentSparePartLinkCreate.sparePart |
| **Part Master** | *(SDK自动生成)* | `864256962114625536` | WorkingPlanCreate.productPart (**重要**: WorkingPlan引用Part的master，不是实例ID) |

### Level 4 - 工序和工艺路线（依赖Level 0-3）

| 模块 | 常量名 | ID值 | 用途 |
|------|--------|------|------|
| **WorkingProcedure** | `PROCEDURE_DEMO_ID` | `8000000001` | PlanProcedureLinkCreate.procedure, ProcedureEquipmentLinkCreate.procedure, ProcedurePartLinkCreate.procedure |
| **WorkingPlan** | `PLAN_DEMO_ID` | `8000000002` | PlanProcedureLinkCreate.plan |

---

## 测试数据示例值

### Level 0 示例

#### Unit（计量单位）
```json
{
  "unitName": "件",
  "unitDisplayName": "piece",
  "unitCategory": "数量",
  "unitFactor": "1.0",
  "mesurementSystem": "公制"
}
```

#### BusinessPartner - Manufacturer（生产厂家）
```json
{
  "partnerCode": "BP-MFG-DEMO",
  "partnerName": "示例机床厂",
  "partnerType": "Machinery",
  "phone": "010-12345678",
  "email": "contact@example-mfg.com"
}
```

#### BusinessPartner - Supplier（供应商）
```json
{
  "partnerCode": "BP-SUP-DEMO",
  "partnerName": "示例供应商",
  "partnerType": "Parts",
  "phone": "021-87654321"
}
```

#### EquipmentClassfication（设备分类）
```json
{
  "equipmentClassName": "CNC加工中心"
}
```

#### PartClassfication（物料分类）
```json
{
  "partClassName": "结构件"
}
```

### Level 1 示例

#### Department（部门）
```json
{
  "deptCode": "DEPT-DEMO",
  "deptName": "生产部",
  "manager": "张三",
  "remarks": "示例部门"
}
```

#### Employee（员工）
```json
{
  "employeeNo": "EMP-DEMO",
  "employeeName": "李四",
  "dept": "4000000001",
  "jobTitle": "操作员",
  "status": "Active",
  "phone": "13800138000",
  "hireDate": "2024-01-01"
}
```

### Level 2 示例

#### EquipmentModel（设备型号）
```json
{
  "modelCode": "MODEL-DEMO",
  "modelName": "CNC-5000",
  "manufacturer": "2000000001",
  "brand": "华为",
  "category": "3000000001"
}
```

#### Location（位置）
```json
{
  "locationCode": "LOC-DEMO",
  "locationName": "一车间",
  "locationType": "Plant",
  "manager": "4000000002"
}
```

### Level 3 示例

#### Equipment - 生产设备
```json
{
  "equipmentCode": "EQ-PROD-DEMO",
  "equipmentName": "CNC加工中心A",
  "manufacturerName": "2000000001",
  "supplierName": "2000000002",
  "category": "3000000001",
  "equipmentModelRef": "5000000001",
  "locationRef": "5000000002",
  "status": "Standby",
  "brand": "华为",
  "serialNumber": "SN-PROD-001"
}
```

#### Equipment - 检测设备
```json
{
  "equipmentCode": "EQ-INSP-DEMO",
  "equipmentName": "三坐标测量仪",
  "manufacturerName": "2000000001",
  "supplierName": "2000000002",
  "category": "3000000001",
  "equipmentModelRef": "5000000001",
  "locationRef": "5000000002",
  "status": "Standby"
}
```

#### Part（物料 - 版本对象）
```json
{
  "partNumber": "PART-DEMO",
  "partName": "中心轮零件",
  "modelSpec": "M-100",
  "stockQty": 100,
  "unit": "1000000001",
  "supplierName": "2000000002",
  "category": "3000000002",
  "businessVersion": "1.0",
  "description": "中心轮零件，用于传动系统"
}
```

### Level 4 示例

#### WorkingProcedure（工序）
```json
{
  "procedureCode": "WP-DEMO",
  "procedureName": "粗加工",
  "steps": "1.上料 2.加工 3.检测",
  "mainProductionEquipment": "6000000001",
  "mainInspectionEquipment": "6000000002",
  "operatorRef": "4000000002",
  "status": "NotStarted"
}
```

#### WorkingPlan（工艺路线 - 版本对象）
```json
{
  "planCode": "PLAN-DEMO",
  "planName": "中心轮零件加工",
  "businessVersion": "1.0",
  "productPart": "864256962114625536",
  "operatorRef": "4000000002",
  "description": "中心轮零件加工工艺路线"
}
```

**⚠️ 注意**: `productPart` 字段使用 Part 的 **master ID**（`864256962114625536`），而不是 Part 实例 ID（`7000000001`）。这是版本对象的特殊要求。

---

## 在 openapi.yaml 中的使用

### 引用字段说明示例

所有引用字段需要添加清晰的 description，说明字段用途和引用的模块：

```yaml
EquipmentCreate:
  properties:
    manufacturerName:
      type: string
      description: "生产厂家ID（-> BusinessPartner模块，partnerType=Machinery）"
      example: "2000000001"

    category:
      type: string
      description: "设备分类ID（-> EquipmentClassfication模块）"
      example: "3000000001"

    supplierName:
      type: string
      description: "供应商ID（-> BusinessPartner模块，SDK必填）"
      example: "2000000002"

    locationRef:
      type: string
      description: "位置ID（-> Location模块，SDK必填）"
      example: "5000000002"

    status:
      type: string
      enum: [Standby, InOperation, UnderMaintenance, Shutdown, Scrap]
      description: "设备状态"
      example: "Standby"
```

### 版本对象特殊处理

```yaml
PartCreate:
  properties:
    partNumber:
      type: string
      description: "物料编号（建议唯一）"
      example: "PART-DEMO"

    # ... 其他字段

    businessVersion:
      type: string
      description: "业务版本号（版本对象必填）"
      example: "1.0"

WorkingPlanCreate:
  properties:
    productPart:
      type: string
      description: "产品物料ID（-> Part.master.id，注意是master而非实例ID！）"
      example: "864256962114625536"
```

---

## 依赖关系图

```
Level 0（无依赖）:
  ├─ Unit
  ├─ BusinessPartner (Manufacturer)
  ├─ BusinessPartner (Supplier)
  ├─ EquipmentClassfication
  └─ PartClassfication

Level 1:
  ├─ Department
  └─ Employee
      └─ 依赖: Department

Level 2:
  ├─ EquipmentModel
  │   └─ 依赖: BusinessPartner, EquipmentClassfication
  └─ Location
      └─ 依赖: Employee

Level 3:
  ├─ Equipment (生产设备)
  │   └─ 依赖: BusinessPartner(x2), EquipmentClassfication, EquipmentModel, Location
  ├─ Equipment (检测设备)
  │   └─ 依赖: 同上
  └─ Part
      └─ 依赖: Unit, BusinessPartner, PartClassfication

Level 4:
  ├─ WorkingProcedure
  │   └─ 依赖: Equipment(x2), Employee
  └─ WorkingPlan
      └─ 依赖: Part.master, Employee
```

---

## 测试重现

### 运行测试
```bash
cd /Users/zbj/IdeaProjects/idme
mvn test -Dtest=OpenApiExamplesDataTest
```

### 预期输出
```
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 测试包含的步骤
1. `test00_Cleanup` - 清理旧数据（反向依赖顺序删除）
2. `test01_CreateLevel0` - 创建5个基础主数据对象
3. `test02_CreateLevel1` - 创建2个组织架构对象
4. `test03_CreateLevel2` - 创建2个设备型号和位置对象
5. `test04_CreateLevel3` - 创建3个设备和物料对象
6. `test05_CreateLevel4` - 创建2个工序和工艺路线对象
7. `test99_VerifyAll` - 验证所有14个对象可正常查询

---

## SDK特殊要求记录

### ID格式
- ❌ 不能使用: `"test-custom-id-12345"` (包含字母和横线)
- ✅ 必须使用: `"9999999999999999"` (纯数字)
- SDK错误: `Invalid numeric value: test-custom-id-12345`

### 必填字段（易遗漏）
- **Equipment.equipmentModelRef** - SDK必填（文档未明确标注）
- **Equipment.supplierName** - SDK必填（文档未明确标注）
- **Equipment.locationRef** - SDK必填（文档未明确标注）
- **WorkingProcedure.mainInspectionEquipment** - SDK必填
- **WorkingPlan.operatorRef** - SDK必填

### 枚举值
- **BusinessPartner.partnerType**: 使用 `Machinery`、`Parts`（不是 `Manufacturer`、`Supplier`）
- **Equipment.status**: `Standby`, `InOperation`, `UnderMaintenance`, `Shutdown`, `Scrap`
- **Location.locationType**: `Plant`, `Workshop`, `Warehouse`, `Office`
- **Employee.status**: `Active`, `Inactive`, `OnLeave`
- **WorkingProcedure.status**: `NotStarted`, `InProgress`, `Completed`, `Paused`

### 版本对象特殊处理
- **Part** 和 **WorkingPlan** 是版本对象
- 创建时需要设置 `master` 和 `branch` 为空 ObjectReference（包含 tenant 和 needSetNullAttrs）
- **WorkingPlan.productPart** 引用 Part 时，使用 **master.id**（如 `864256962114625536`），而不是 Part 实例 ID（如 `7000000001`）

---

## 维护说明

### 如何重新生成示例数据

1. **删除旧数据**（可选，test00_Cleanup会自动处理）:
   ```bash
   # 按反向依赖顺序手动删除（如果需要）
   # Level 4 → Level 3 → Level 2 → Level 1 → Level 0
   ```

2. **运行测试**:
   ```bash
   mvn test -Dtest=OpenApiExamplesDataTest
   ```

3. **提取Part Master ID**（如果SDK重新生成）:
   ```bash
   mvn test -Dtest=OpenApiExamplesDataTest | grep "Part Master ID"
   # 输出示例: ✅ Part created: 7000000001, Master ID: 864256962114625536
   ```

4. **更新本文档** 中的 Part Master ID（如果有变化）

5. **更新 openapi.yaml** 中 WorkingPlanCreate.productPart 的 example 值

### ID冲突处理

如果固定ID已被其他开发者使用：
1. 修改 `OpenApiExamplesDataTest.java` 中的常量（使用新的ID段）
2. 重新运行测试
3. 更新本文档和 openapi.yaml

### 注意事项

- 示例数据仅用于文档和前端开发参考，不应用于生产环境
- ID使用特定的亿段分隔，避免与真实业务数据冲突
- Part Master ID 由 SDK 自动生成，**无法指定固定值**
- 定期检查 SDK 枚举值是否有更新（参考 CLAUDE.md）
