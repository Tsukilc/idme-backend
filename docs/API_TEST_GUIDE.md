# API 测试数据初始化指南

本指南提供完整的测试数据创建流程，所有示例数据来自集成测试，**可直接运行**。

## 📋 快速开始

按以下顺序执行API调用，每步记录返回的ID，用于下一步。

---

## 第一层：0依赖基础数据（可直接创建）

### 1.1 创建计量单位 (Unit)

```bash
POST /api/unit
```

```json
{
  "unitName": "测试单位_件",
  "unitDisplayName": "piece",
  "unitCategory": "数量",
  "unitFactor": "1.0",
  "mesurementSystem": "公制"
}
```

**返回示例**：
```json
{
  "result": "SUCCESS",
  "data": "unit_id_001"  ← 记录此ID
}
```

---

### 1.2 创建往来单位-生产厂家 (BusinessPartner)

```bash
POST /api/business-partner
```

```json
{
  "partnerCode": "MANUFACTURER_001",
  "partnerName": "测试生产厂家",
  "partnerType": "Machinery",
  "phone": "010-12345678",
  "email": "manufacturer@example.com"
}
```

**返回**：记录 `data` 字段的ID → **manufacturer_id**

---

### 1.3 创建往来单位-供应商 (BusinessPartner)

```bash
POST /api/business-partner
```

```json
{
  "partnerCode": "SUPPLIER_001",
  "partnerName": "测试供应商",
  "partnerType": "Parts",
  "phone": "021-87654321",
  "email": "supplier@example.com"
}
```

**返回**：记录 `data` 字段的ID → **supplier_id**

---

### 1.4 创建设备分类 (EquipmentClassfication)

```bash
POST /api/equipment-classfication
```

```json
{
  "equipmentClassName": "测试设备分类"
}
```

**返回**：记录 `data` 字段的ID → **equipment_category_id**

---

### 1.5 创建物料分类 (PartClassfication)

```bash
POST /api/part-classfication
```

```json
{
  "partClassName": "电子元件"
}
```

**返回**：记录 `data` 字段的ID → **part_category_id**

---

### 1.6 创建部门 (Department)

```bash
POST /api/department
```

```json
{
  "deptCode": "DEPT_001",
  "deptName": "生产部",
  "manager": "张经理"
}
```

**返回**：记录 `data` 字段的ID → **dept_id**

---

## 第二层：依赖第一层的数据

### 2.1 创建员工 (Employee)

**前置条件**：需要 `dept_id`

```bash
POST /api/employee
```

```json
{
  "employeeNo": "EMP_001",
  "employeeName": "张三",
  "dept": "<填入上面的 dept_id>",
  "jobTitle": "工程师",
  "status": "在职",
  "hireDate": "2024-01-01"
}
```

**返回**：记录 `data` 字段的ID → **employee_id**

---

### 2.2 创建位置 (Location)

**前置条件**：需要 `employee_id`

```bash
POST /api/location
```

```json
{
  "locationCode": "LOC_001",
  "locationName": "生产车间",
  "locationType": "Workshop",
  "manager": "<填入上面的 employee_id>"
}
```

**返回**：记录 `data` 字段的ID → **location_id**

---

### 2.3 创建设备机型 (EquipmentModel)

**前置条件**：需要 `manufacturer_id`, `equipment_category_id`

```bash
POST /api/equipment-model
```

```json
{
  "modelCode": "MODEL_001",
  "modelName": "测试设备机型",
  "manufacturer": "<填入 manufacturer_id>",
  "brand": "测试品牌",
  "category": "<填入 equipment_category_id>"
}
```

**返回**：记录 `data` 字段的ID → **equipment_model_id**

---

## 第三层：业务核心数据

### 3.1 创建设备 (Equipment)

**前置条件**：需要 `manufacturer_id`, `supplier_id`, `equipment_model_id`, `location_id`, `equipment_category_id`

```bash
POST /api/equipment
```

```json
{
  "equipmentCode": "EQUIP_TEST_001",
  "equipmentName": "测试设备",
  "manufacturerName": "<填入 manufacturer_id>",
  "supplierName": "<填入 supplier_id>",
  "brand": "测试品牌",
  "modelSpec": "V1.0",
  "equipmentModelRef": "<填入 equipment_model_id>",
  "locationRef": "<填入 location_id>",
  "category": "<填入 equipment_category_id>",
  "status": "Standby",
  "serialNumber": "SN-12345",
  "serviceLifeYears": 10,
  "depreciationMethod": "StraightLine"
}
```

**返回**：记录 `data` 字段的ID → **equipment_id**

---

### 3.2 创建物料 (Part)

**前置条件**：需要 `unit_id`, `supplier_id`, `part_category_id`

```bash
POST /api/part
```

```json
{
  "partNumber": "PART-TEST-001",
  "partName": "测试物料",
  "modelSpec": "V1.0",
  "stockQty": 100,
  "unit": "<填入 unit_id>",
  "supplierName": "<填入 supplier_id>",
  "category": "<填入 part_category_id>",
  "businessVersion": "1.0",
  "description": "这是一个测试物料"
}
```

**返回**：
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "part_version_id_001",  ← 版本ID
    "masterId": "part_master_id_001"  ← 主对象ID（用于版本管理）
  }
}
```

记录 `data.id` → **part_id**
记录 `data.masterId` → **part_master_id**

---

### 3.3 创建工序 (WorkingProcedure)

**前置条件**：需要 `equipment_id`, `employee_id`

```bash
POST /api/working-procedure
```

```json
{
  "procedureCode": "PROC_TEST_001",
  "procedureName": "测试工序",
  "steps": "步骤1：准备材料；步骤2：加工；步骤3：检验",
  "mainProductionEquipment": "<填入 equipment_id>",
  "mainInspectionEquipment": "<填入 equipment_id>",
  "operatorUser": "测试操作员",
  "operatorRef": "<填入 employee_id>",
  "status": "InProgress",
  "remarks": "测试备注"
}
```

**返回**：记录 `data` 字段的ID → **procedure_id**

---

### 3.4 创建工艺路线 (WorkingPlan)

**前置条件**：需要 `part_id` (或 `part_master_id`)

```bash
POST /api/working-plan
```

```json
{
  "planCode": "PLAN_TEST_001",
  "planName": "测试工艺路线",
  "businessVersion": "1.0",
  "productPart": "<填入 part_master_id>",
  "description": "测试工艺路线描述",
  "status": "Draft"
}
```

**返回**：
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "plan_version_id_001",
    "masterId": "plan_master_id_001"
  }
}
```

记录 `data.id` → **plan_id**
记录 `data.masterId` → **plan_master_id**

---

## 第四层：关联关系

### 4.1 创建 BOM 项 (BOMItem)

**前置条件**：需要 2 个 `part_id` (父件和子件), `unit_id`

先创建第二个物料作为子件（参考3.2，使用不同的partNumber），然后：

```bash
POST /api/bom-item
```

```json
{
  "parentPart": "<填入父件 part_id>",
  "childPart": "<填入子件 part_id>",
  "quantity": 2.5,
  "uom": "<填入 unit_id>",
  "findNumber": 10,
  "remarks": "测试BOM项"
}
```

---

### 4.2 为工艺路线添加工序 (PlanProcedureLink)

**前置条件**：需要 `plan_id`, `procedure_id`

```bash
POST /api/plan-procedure-link
```

```json
{
  "plan": "<填入 plan_id>",
  "procedure": "<填入 procedure_id>",
  "sequenceNo": 10,
  "standardDurationMin": 30,
  "requirement": "严格按照工艺要求执行"
}
```

---

### 4.3 为工序关联设备 (ProcedureEquipmentLink)

**前置条件**：需要 `procedure_id`, `equipment_id`

```bash
POST /api/procedure-equipment-link
```

```json
{
  "procedure": "<填入 procedure_id>",
  "equipment1": "<填入 equipment_id>",
  "role": "Production",
  "remarks": "主要生产设备"
}
```

---

### 4.4 为工序关联物料 (ProcedurePartLink)

**前置条件**：需要 `procedure_id`, `part_id`, `unit_id`

```bash
POST /api/procedure-part-link
```

```json
{
  "procedure": "<填入 procedure_id>",
  "part1": "<填入 part_id>",
  "role": "Input",
  "quantity": 10,
  "uom": "<填入 unit_id>",
  "isMandatory": true
}
```

---

### 4.5 为设备添加备品备件 (EquipmentSparePartLink)

**前置条件**：需要 `equipment_id`, 备件的 `part_id`, `unit_id`

```bash
POST /api/equipment-spare-part-link
```

```json
{
  "equipment": "<填入 equipment_id>",
  "sparePart": "<填入备件 part_id>",
  "quantity": 2,
  "unit": "<填入 unit_id>",
  "isCritical": true,
  "replacementCycleDays": 90,
  "remarks": "关键易损件"
}
```

---

## ✅ 测试数据初始化完成

现在您已经创建了一套完整的测试数据，包含：
- ✅ 1 个计量单位
- ✅ 2 个往来单位（生产厂家 + 供应商）
- ✅ 1 个设备分类
- ✅ 1 个物料分类
- ✅ 1 个部门
- ✅ 1 个员工
- ✅ 1 个位置
- ✅ 1 个设备机型
- ✅ 1 个设备
- ✅ 2 个物料（父件 + 子件）
- ✅ 1 个工序
- ✅ 1 个工艺路线
- ✅ 1 个 BOM 项
- ✅ 工艺路线、工序、设备、物料的关联关系

## 📌 重要提示

1. **ID 的保存**：每次创建后，务必记录返回的 ID，用于后续步骤
2. **枚举值**：所有枚举字段（status、role、partnerType 等）使用的都是 SDK 有效值
3. **版本对象**：Part 和 WorkingPlan 是版本对象，返回包含 `id` 和 `masterId` 两个字段
4. **依赖顺序**：必须按照上述顺序创建，否则会因缺少依赖数据而失败

## 🔄 批量初始化脚本

如需批量初始化，可将上述请求保存为 Postman Collection 或编写自动化脚本。
