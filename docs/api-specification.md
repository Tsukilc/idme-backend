# iDME 工业 MiniApp API 接口文档

## OpenAPI / Apifox 导入

接口已提供 **OpenAPI 3.0** 规范文件，可直接导入 Apifox / Postman / Swagger 等工具：

- **文件路径**: [`docs/openapi.yaml`](openapi.yaml)
- **导入 Apifox**: 打开 Apifox → 项目设置 → 导入数据 → 选择「OpenAPI/Swagger」→ 选择 `openapi.yaml` 或粘贴 URL/内容 → 导入。
- **全局认证**: 导入后在「环境」或「接口认证」中配置 **Basic 认证**，用户名 `sysadmin`，密码见下方服务器信息。

## 服务器信息

- **基础URL**: `http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services`
- **管理界面**: `http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html`
- **健康检查**: `http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/v1/health`
- **认证方式**: Basic Auth / Token
  - 用户名: `sysadmin`
  - 密码: `DlVya3xYJmR/yiO7`

## 通用说明

### 统一响应格式

```json
{
  "result": "SUCCESS",
  "data": {},
  "errors": []
}
```

### 分页参数

- `pageNum`: 页码（从 1 开始）
- `pageSize`: 每页大小（默认 20）

### 通用查询参数

- `condition`: 查询条件（JSON 对象）
- `orderBy`: 排序字段
- `orderDirection`: 排序方向（ASC/DESC）

---

## 目录

1. [设备管理 (Equipment)](#1-设备管理-equipment)
2. [物料管理 (Part)](#2-物料管理-part)
3. [工序管理 (WorkingProcedure)](#3-工序管理-workingprocedure)
4. [工艺路线管理 (WorkingPlan)](#4-工艺路线管理-workingplan)
5. [BOM 管理 (BOMItem)](#5-bom-管理-bomitem)
6. [工艺工序关联 (PlanProcedureLink)](#6-工艺工序关联-planprocedurelink)
7. [工序设备关联 (ProcedureEquipmentLink)](#7-工序设备关联-procedureequipmentlink)
8. [工序物料关联 (ProcedurePartLink)](#8-工序物料关联-procedurepartlink)
9. [往来单位管理 (BusinessPartner)](#9-往来单位管理-businesspartner)
10. [联系人管理 (PartnerContact)](#10-联系人管理-partnercontact)
11. [部门管理 (Department)](#11-部门管理-department)
12. [员工管理 (Employee)](#12-员工管理-employee)
13. [位置管理 (Location)](#13-位置管理-location)
14. [设备机型管理 (EquipmentModel)](#14-设备机型管理-equipmentmodel)
15. [设备备品备件关联 (EquipmentSparePartLink)](#15-设备备品备件关联-equipmentsparepartlink)

---

## 1. 设备管理 (Equipment)

### 1.1 创建设备

**接口**: `POST /api/equipment`

**请求示例**:
```json
{
  "equipmentCode": "EQ-0001",
  "equipmentName": "CNC加工中心A",
  "manufacturerName": "BP-001",
  "brand": "华中数控",
  "modelSpec": "HNC-6080",
  "supplierName": "BP-002",
  "productionDate": "2023-01-15",
  "serviceLifeYears": 10,
  "depreciationMethod": "直线法",
  "locationText": "一车间/产线2/工位07",
  "status": "运行",
  "serialNumber": "CNC2023001",
  "category": "CNC",
  "techParams": {
    "maxSpeed": "8000rpm",
    "power": "15KW",
    "precision": "±0.005mm"
  },
  "remarks": "主力生产设备"
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "equipment_uuid_001",
    "equipmentCode": "EQ-0001",
    "equipmentName": "CNC加工中心A",
    "createTime": "2024-02-11T10:00:00Z"
  },
  "errors": []
}
```

### 1.2 查询设备详情

**接口**: `GET /api/equipment/{id}`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "equipment_uuid_001",
    "equipmentCode": "EQ-0001",
    "equipmentName": "CNC加工中心A",
    "manufacturerName": {
      "id": "bp_uuid_001",
      "partnerName": "XX机床厂"
    },
    "brand": "华中数控",
    "modelSpec": "HNC-6080",
    "status": "运行",
    "techParams": {
      "maxSpeed": "8000rpm",
      "power": "15KW"
    },
    "createTime": "2024-02-11T10:00:00Z",
    "lastModifiedTime": "2024-02-11T10:00:00Z"
  },
  "errors": []
}
```

### 1.3 分页查询设备列表

**接口**: `GET /api/equipment`

**请求参数**:
- `pageNum`: 页码（默认 1）
- `pageSize`: 每页大小（默认 20）
- `equipmentName`: 设备名称（模糊搜索）
- `equipmentCode`: 设备编码
- `status`: 设备状态
- `category`: 设备分类

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 20,
    "records": [
      {
        "id": "equipment_uuid_001",
        "equipmentCode": "EQ-0001",
        "equipmentName": "CNC加工中心A",
        "status": "运行",
        "locationText": "一车间/产线2/工位07"
      }
    ]
  },
  "errors": []
}
```

### 1.4 更新设备

**接口**: `PUT /api/equipment/{id}`

**请求示例**:
```json
{
  "equipmentName": "CNC加工中心A（升级版）",
  "status": "维修",
  "remarks": "设备升级维护中"
}
```

### 1.5 删除设备

**接口**: `DELETE /api/equipment/{id}`

### 1.6 按状态统计设备

**接口**: `GET /api/equipment/statistics/by-status`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "运行": 45,
    "待机": 20,
    "维修": 5,
    "报废": 2
  },
  "errors": []
}
```

### 1.7 查询设备扩展信息（技术参数）

**接口**: `GET /api/equipment/{id}/tech-params`

### 1.8 更新设备扩展信息

**接口**: `PUT /api/equipment/{id}/tech-params`

---

## 2. 物料管理 (Part)

### 2.1 创建物料

**接口**: `POST /api/part`

**请求示例**:
```json
{
  "partNumber": "P-0001",
  "partName": "中心轮",
  "modelSpec": "型号A",
  "stockQty": 100.5,
  "unit": "PCS",
  "supplierName": "BP-003",
  "category": "结构件/高强钢安装件",
  "businessVersion": "1.0",
  "description": "行星减速器核心零件",
  "drawingUrl": "http://example.com/drawing.pdf",
  "extra": {
    "material": "45号钢",
    "weight": "2.5kg"
  }
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "part_uuid_001",
    "masterId": "part_master_uuid_001",
    "partNumber": "P-0001",
    "partName": "中心轮",
    "businessVersion": "1.0",
    "versionNumber": "V1"
  },
  "errors": []
}
```

### 2.2 查询物料详情

**接口**: `GET /api/part/{id}`

### 2.3 分页查询物料列表

**接口**: `GET /api/part`

**请求参数**:
- `pageNum`: 页码
- `pageSize`: 每页大小
- `partNumber`: 物料编号
- `partName`: 物料名称（模糊搜索）
- `category`: 物料分类

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "total": 500,
    "pageNum": 1,
    "pageSize": 20,
    "records": [
      {
        "id": "part_uuid_001",
        "partNumber": "P-0001",
        "partName": "中心轮",
        "businessVersion": "1.0",
        "stockQty": 100.5,
        "unit": "PCS"
      }
    ]
  },
  "errors": []
}
```

### 2.4 更新物料

**接口**: `PUT /api/part/{id}`

### 2.5 删除物料

**接口**: `DELETE /api/part/{id}`

### 2.6 创建新版本

**接口**: `POST /api/part/{id}/new-version`

**请求示例**:
```json
{
  "businessVersion": "2.0",
  "description": "优化材质，提升强度"
}
```

### 2.7 查询物料所有版本

**接口**: `GET /api/part/{masterId}/versions`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "part_uuid_001",
      "businessVersion": "1.0",
      "versionNumber": "V1",
      "createTime": "2024-01-01T10:00:00Z"
    },
    {
      "id": "part_uuid_002",
      "businessVersion": "2.0",
      "versionNumber": "V2",
      "createTime": "2024-02-01T10:00:00Z"
    }
  ],
  "errors": []
}
```

### 2.8 按分类查询物料

**接口**: `GET /api/part/by-category/{categoryPath}`

**示例**: `GET /api/part/by-category/结构件/高强钢安装件`

### 2.9 查询物料库存统计

**接口**: `GET /api/part/statistics/stock`

---

## 3. 工序管理 (WorkingProcedure)

### 3.1 创建工序

**接口**: `POST /api/working-procedure`

**请求示例**:
```json
{
  "procedureCode": "WP-001",
  "procedureName": "毛坯制造",
  "steps": "1. 锻造成型\n2. 热处理\n3. 初步检验",
  "mainProductionEquipment": "equipment_uuid_001",
  "mainInspectionEquipment": "equipment_uuid_002",
  "operatorUser": "user_uuid_001",
  "operatorRef": "employee_uuid_001",
  "startTime": "2024-02-11T08:00:00Z",
  "endTime": "2024-02-11T17:00:00Z",
  "status": "未开始",
  "remarks": "首道工序"
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "procedure_uuid_001",
    "procedureCode": "WP-001",
    "procedureName": "毛坯制造",
    "status": "未开始"
  },
  "errors": []
}
```

### 3.2 查询工序详情

**接口**: `GET /api/working-procedure/{id}`

### 3.3 分页查询工序列表

**接口**: `GET /api/working-procedure`

**请求参数**:
- `pageNum`: 页码
- `pageSize`: 每页大小
- `procedureName`: 工序名称
- `status`: 工序状态

### 3.4 更新工序

**接口**: `PUT /api/working-procedure/{id}`

### 3.5 删除工序

**接口**: `DELETE /api/working-procedure/{id}`

### 3.6 批量创建工序

**接口**: `POST /api/working-procedure/batch`

**请求示例**:
```json
{
  "procedures": [
    {
      "procedureCode": "WP-001",
      "procedureName": "毛坯制造"
    },
    {
      "procedureCode": "WP-002",
      "procedureName": "粗加工"
    },
    {
      "procedureCode": "WP-003",
      "procedureName": "精加工"
    },
    {
      "procedureCode": "WP-004",
      "procedureName": "检测"
    },
    {
      "procedureCode": "WP-005",
      "procedureName": "入库"
    }
  ]
}
```

### 3.7 更新工序状态

**接口**: `PATCH /api/working-procedure/{id}/status`

**请求示例**:
```json
{
  "status": "进行中",
  "actualStart": "2024-02-11T08:30:00Z"
}
```

---

## 4. 工艺路线管理 (WorkingPlan)

### 4.1 创建工艺路线

**接口**: `POST /api/working-plan`

**请求示例**:
```json
{
  "planCode": "PL-0001",
  "planName": "中心轮零件加工",
  "businessVersion": "1.0",
  "productPart": "part_uuid_001",
  "description": "行星减速器中心轮的完整加工工艺流程，包含毛坯制造、机械加工、轴类零件加工、尺寸检测、入库等5道工序",
  "operatorUser": "user_uuid_001",
  "operatorRef": "employee_uuid_001",
  "operateTime": "2024-02-11T10:00:00Z",
  "equipmentUsage": {
    "CNC加工中心": 2,
    "车床": 1,
    "三坐标": 1
  },
  "status": "Released",
  "remarks": "标准工艺路线"
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "plan_uuid_001",
    "masterId": "plan_master_uuid_001",
    "planCode": "PL-0001",
    "planName": "中心轮零件加工",
    "businessVersion": "1.0",
    "versionNumber": "V1"
  },
  "errors": []
}
```

### 4.2 查询工艺路线详情

**接口**: `GET /api/working-plan/{id}`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "plan_uuid_001",
    "planCode": "PL-0001",
    "planName": "中心轮零件加工",
    "businessVersion": "1.0",
    "productPart": {
      "id": "part_uuid_001",
      "partName": "中心轮"
    },
    "description": "完整加工工艺流程",
    "procedures": [
      {
        "sequenceNo": 1,
        "procedure": {
          "id": "procedure_uuid_001",
          "procedureName": "毛坯制造"
        },
        "standardDurationMin": 120
      },
      {
        "sequenceNo": 2,
        "procedure": {
          "id": "procedure_uuid_002",
          "procedureName": "粗加工"
        },
        "standardDurationMin": 180
      }
    ],
    "status": "Released"
  },
  "errors": []
}
```

### 4.3 分页查询工艺路线列表

**接口**: `GET /api/working-plan`

**请求参数**:
- `pageNum`: 页码
- `pageSize`: 每页大小
- `planName`: 工艺名称
- `status`: 状态

### 4.4 更新工艺路线

**接口**: `PUT /api/working-plan/{id}`

### 4.5 删除工艺路线

**接口**: `DELETE /api/working-plan/{id}`

### 4.6 创建工艺新版本

**接口**: `POST /api/working-plan/{id}/new-version`

### 4.7 查询工艺路线的所有工序

**接口**: `GET /api/working-plan/{id}/procedures`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "sequenceNo": 1,
      "procedure": {
        "id": "procedure_uuid_001",
        "procedureCode": "WP-001",
        "procedureName": "毛坯制造"
      },
      "standardDurationMin": 120,
      "requirement": "锻造后热处理"
    },
    {
      "sequenceNo": 2,
      "procedure": {
        "id": "procedure_uuid_002",
        "procedureCode": "WP-002",
        "procedureName": "粗加工"
      },
      "standardDurationMin": 180,
      "requirement": "精度±0.1mm"
    }
  ],
  "errors": []
}
```

### 4.8 查询工艺路线的设备使用情况

**接口**: `GET /api/working-plan/{id}/equipment-usage`

---

## 5. BOM 管理 (BOMItem)

### 5.1 创建 BOM 项

**接口**: `POST /api/bom-item`

**请求示例**:
```json
{
  "parentPart": "part_uuid_001",
  "childPart": "part_uuid_002",
  "quantity": 2.0,
  "uom": "PCS",
  "findNumber": 10,
  "effectiveFrom": "2024-01-01",
  "remarks": "主要组件"
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "bom_item_uuid_001",
    "parentPart": {
      "id": "part_uuid_001",
      "partName": "中心轮组件"
    },
    "childPart": {
      "id": "part_uuid_002",
      "partName": "轴承"
    },
    "quantity": 2.0,
    "findNumber": 10
  },
  "errors": []
}
```

### 5.2 查询 BOM 项详情

**接口**: `GET /api/bom-item/{id}`

### 5.3 查询物料的 BOM 结构（树形）

**接口**: `GET /api/bom-item/tree/{partId}`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "part": {
      "id": "part_uuid_001",
      "partNumber": "P-0001",
      "partName": "中心轮组件"
    },
    "children": [
      {
        "findNumber": 10,
        "quantity": 2.0,
        "uom": "PCS",
        "part": {
          "id": "part_uuid_002",
          "partNumber": "P-0002",
          "partName": "轴承"
        },
        "children": []
      },
      {
        "findNumber": 20,
        "quantity": 4.0,
        "uom": "PCS",
        "part": {
          "id": "part_uuid_003",
          "partNumber": "P-0003",
          "partName": "螺栓"
        },
        "children": []
      }
    ]
  },
  "errors": []
}
```

### 5.4 查询物料的父级 BOM（被哪些物料使用）

**接口**: `GET /api/bom-item/where-used/{partId}`

### 5.5 批量创建 BOM 项

**接口**: `POST /api/bom-item/batch`

### 5.6 更新 BOM 项

**接口**: `PUT /api/bom-item/{id}`

### 5.7 删除 BOM 项

**接口**: `DELETE /api/bom-item/{id}`

---

## 6. 工艺工序关联 (PlanProcedureLink)

### 6.1 为工艺路线添加工序

**接口**: `POST /api/plan-procedure-link`

**请求示例**:
```json
{
  "plan": "plan_uuid_001",
  "procedure": "procedure_uuid_001",
  "sequenceNo": 1,
  "standardDurationMin": 120.0,
  "requirement": "锻造成型，热处理后检验"
}
```

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": {
    "id": "link_uuid_001",
    "plan": {
      "id": "plan_uuid_001",
      "planName": "中心轮零件加工"
    },
    "procedure": {
      "id": "procedure_uuid_001",
      "procedureName": "毛坯制造"
    },
    "sequenceNo": 1
  },
  "errors": []
}
```

### 6.2 批量添加工序（按顺序）

**接口**: `POST /api/plan-procedure-link/batch`

**请求示例**:
```json
{
  "planId": "plan_uuid_001",
  "procedures": [
    {
      "procedureId": "procedure_uuid_001",
      "sequenceNo": 1,
      "standardDurationMin": 120,
      "requirement": "锻造成型"
    },
    {
      "procedureId": "procedure_uuid_002",
      "sequenceNo": 2,
      "standardDurationMin": 180,
      "requirement": "粗加工至尺寸"
    },
    {
      "procedureId": "procedure_uuid_003",
      "sequenceNo": 3,
      "standardDurationMin": 150,
      "requirement": "精加工"
    },
    {
      "procedureId": "procedure_uuid_004",
      "sequenceNo": 4,
      "standardDurationMin": 60,
      "requirement": "三坐标检测"
    },
    {
      "procedureId": "procedure_uuid_005",
      "sequenceNo": 5,
      "standardDurationMin": 30,
      "requirement": "入库登记"
    }
  ]
}
```

### 6.3 更新工序顺序

**接口**: `PUT /api/plan-procedure-link/{id}/sequence`

**请求示例**:
```json
{
  "sequenceNo": 2
}
```

### 6.4 删除工艺工序关联

**接口**: `DELETE /api/plan-procedure-link/{id}`

### 6.5 查询工艺路线的工序列表（已排序）

**接口**: `GET /api/plan-procedure-link/by-plan/{planId}`

---

## 7. 工序设备关联 (ProcedureEquipmentLink)

### 7.1 为工序关联设备

**接口**: `POST /api/procedure-equipment-link`

**请求示例**:
```json
{
  "procedure": "procedure_uuid_001",
  "equipment1": "equipment_uuid_001",
  "role": "生产",
  "plannedStart": "2024-02-11T08:00:00Z",
  "plannedEnd": "2024-02-11T10:00:00Z",
  "remarks": "主要生产设备"
}
```

### 7.2 更新设备实际使用时间

**接口**: `PATCH /api/procedure-equipment-link/{id}/actual-time`

**请求示例**:
```json
{
  "actualStart": "2024-02-11T08:05:00Z",
  "actualEnd": "2024-02-11T10:15:00Z"
}
```

### 7.3 查询工序使用的所有设备

**接口**: `GET /api/procedure-equipment-link/by-procedure/{procedureId}`

### 7.4 查询设备参与的所有工序

**接口**: `GET /api/procedure-equipment-link/by-equipment/{equipmentId}`

### 7.5 删除工序设备关联

**接口**: `DELETE /api/procedure-equipment-link/{id}`

---

## 8. 工序物料关联 (ProcedurePartLink)

### 8.1 为工序关联物料

**接口**: `POST /api/procedure-part-link`

**请求示例**:
```json
{
  "procedure": "procedure_uuid_001",
  "part1": "part_uuid_001",
  "role": "Input",
  "quantity": 1.0,
  "uom": "PCS",
  "isMandatory": true,
  "remarks": "主要投入物料"
}
```

### 8.2 查询工序的投入物料

**接口**: `GET /api/procedure-part-link/by-procedure/{procedureId}?role=Input`

### 8.3 查询工序的产出物料

**接口**: `GET /api/procedure-part-link/by-procedure/{procedureId}?role=Output`

### 8.4 查询物料在哪些工序中使用

**接口**: `GET /api/procedure-part-link/by-part/{partId}`

### 8.5 删除工序物料关联

**接口**: `DELETE /api/procedure-part-link/{id}`

---

## 9. 往来单位管理 (BusinessPartner)

### 9.1 创建往来单位

**接口**: `POST /api/business-partner`

**请求示例**:
```json
{
  "partnerCode": "BP-0001",
  "partnerName": "XX机床厂",
  "partnerType": "Manufacturer",
  "phone": "021-12345678",
  "email": "contact@example.com",
  "website": "https://www.example.com",
  "addressText": "上海市浦东新区XX路XX号",
  "extra": {
    "businessLicense": "91310000XXXXXXXXXX",
    "taxNumber": "91310000XXXXXXXXXX"
  }
}
```

### 9.2 查询往来单位详情

**接口**: `GET /api/business-partner/{id}`

### 9.3 分页查询往来单位列表

**接口**: `GET /api/business-partner`

**请求参数**:
- `partnerName`: 单位名称（模糊搜索）
- `partnerType`: 单位类型

### 9.4 按类型查询往来单位

**接口**: `GET /api/business-partner/by-type/{partnerType}`

### 9.5 更新往来单位

**接口**: `PUT /api/business-partner/{id}`

### 9.6 删除往来单位

**接口**: `DELETE /api/business-partner/{id}`

---

## 10. 联系人管理 (PartnerContact)

### 10.1 创建联系人

**接口**: `POST /api/partner-contact`

**请求示例**:
```json
{
  "partner": "bp_uuid_001",
  "contactName": "张三",
  "mobile": "13800138000",
  "phone": "021-12345678-8001",
  "email": "zhangsan@example.com",
  "role": "销售",
  "remarks": "主要联系人"
}
```

### 10.2 查询往来单位的所有联系人

**接口**: `GET /api/partner-contact/by-partner/{partnerId}`

### 10.3 更新联系人

**接口**: `PUT /api/partner-contact/{id}`

### 10.4 删除联系人

**接口**: `DELETE /api/partner-contact/{id}`

---

## 11. 部门管理 (Department)

### 11.1 创建部门

**接口**: `POST /api/department`

**请求示例**:
```json
{
  "deptCode": "DEPT-001",
  "deptName": "生产部",
  "parentDept": null,
  "manager": "employee_uuid_001",
  "remarks": "负责生产制造"
}
```

### 11.2 查询部门详情

**接口**: `GET /api/department/{id}`

### 11.3 查询部门树

**接口**: `GET /api/department/tree`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "dept_uuid_001",
      "deptCode": "DEPT-001",
      "deptName": "生产部",
      "children": [
        {
          "id": "dept_uuid_002",
          "deptCode": "DEPT-001-01",
          "deptName": "一车间",
          "children": []
        }
      ]
    }
  ],
  "errors": []
}
```

### 11.4 更新部门

**接口**: `PUT /api/department/{id}`

### 11.5 删除部门

**接口**: `DELETE /api/department/{id}`

---

## 12. 员工管理 (Employee)

### 12.1 创建员工

**接口**: `POST /api/employee`

**请求示例**:
```json
{
  "employeeNo": "EMP-0001",
  "employeeName": "李四",
  "userRef": "user_uuid_001",
  "jobTitle": "高级技工",
  "dept": "dept_uuid_001",
  "phone": "13900139000",
  "email": "lisi@example.com",
  "status": "在职",
  "hireDate": "2020-01-01",
  "extra": {
    "certificates": ["车工证", "数控操作证"],
    "skills": ["CNC编程", "机械加工"]
  }
}
```

### 12.2 查询员工详情

**接口**: `GET /api/employee/{id}`

### 12.3 分页查询员工列表

**接口**: `GET /api/employee`

**请求参数**:
- `employeeName`: 员工姓名（模糊搜索）
- `employeeNo`: 员工编号
- `dept`: 部门ID
- `status`: 在职状态

### 12.4 按部门查询员工

**接口**: `GET /api/employee/by-dept/{deptId}`

### 12.5 更新员工

**接口**: `PUT /api/employee/{id}`

### 12.6 删除员工

**接口**: `DELETE /api/employee/{id}`

---

## 13. 位置管理 (Location)

### 13.1 创建位置

**接口**: `POST /api/location`

**请求示例**:
```json
{
  "locationCode": "LOC-001",
  "locationName": "一车间",
  "locationType": "车间",
  "parentLocation": null,
  "addressText": "厂区北侧",
  "manager": "employee_uuid_001",
  "remarks": "主生产车间"
}
```

### 13.2 查询位置详情

**接口**: `GET /api/location/{id}`

### 13.3 查询位置树

**接口**: `GET /api/location/tree`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "loc_uuid_001",
      "locationCode": "LOC-001",
      "locationName": "一车间",
      "locationType": "车间",
      "children": [
        {
          "id": "loc_uuid_002",
          "locationCode": "LOC-001-01",
          "locationName": "产线2",
          "locationType": "产线",
          "children": [
            {
              "id": "loc_uuid_003",
              "locationCode": "LOC-001-01-07",
              "locationName": "工位07",
              "locationType": "工位",
              "children": []
            }
          ]
        }
      ]
    }
  ],
  "errors": []
}
```

### 13.4 查询某位置下的所有设备

**接口**: `GET /api/location/{id}/equipments`

### 13.5 更新位置

**接口**: `PUT /api/location/{id}`

### 13.6 删除位置

**接口**: `DELETE /api/location/{id}`

---

## 14. 设备机型管理 (EquipmentModel)

### 14.1 创建设备机型

**接口**: `POST /api/equipment-model`

**请求示例**:
```json
{
  "modelCode": "MODEL-001",
  "modelName": "CNC加工中心标准型",
  "manufacturer": "bp_uuid_001",
  "brand": "华中数控",
  "modelSpec": "HNC-6080",
  "category": "CNC",
  "defaultTechParams": {
    "maxSpeed": "8000rpm",
    "power": "15KW",
    "precision": "±0.005mm",
    "workingArea": "600x800x500mm"
  },
  "remarks": "标准配置"
}
```

### 14.2 查询设备机型详情

**接口**: `GET /api/equipment-model/{id}`

### 14.3 分页查询设备机型列表

**接口**: `GET /api/equipment-model`

### 14.4 更新设备机型

**接口**: `PUT /api/equipment-model/{id}`

### 14.5 删除设备机型

**接口**: `DELETE /api/equipment-model/{id}`

---

## 15. 设备备品备件关联 (EquipmentSparePartLink)

### 15.1 为设备添加备品备件

**接口**: `POST /api/equipment-spare-part-link`

**请求示例**:
```json
{
  "equipment": "equipment_uuid_001",
  "sparePart": "part_uuid_010",
  "quantity": 2,
  "unit": "PCS",
  "isCritical": true,
  "replacementCycleDays": 180,
  "remarks": "关键易损件"
}
```

### 15.2 查询设备的所有备品备件

**接口**: `GET /api/equipment-spare-part-link/by-equipment/{equipmentId}`

**响应示例**:
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "link_uuid_001",
      "sparePart": {
        "id": "part_uuid_010",
        "partNumber": "P-0010",
        "partName": "主轴轴承"
      },
      "quantity": 2,
      "unit": "PCS",
      "isCritical": true,
      "replacementCycleDays": 180
    }
  ],
  "errors": []
}
```

### 15.3 查询物料作为备件被哪些设备使用

**接口**: `GET /api/equipment-spare-part-link/by-part/{partId}`

### 15.4 更新备品备件信息

**接口**: `PUT /api/equipment-spare-part-link/{id}`

### 15.5 删除备品备件关联

**接口**: `DELETE /api/equipment-spare-part-link/{id}`

---

## 附录

### A. 枚举值定义

#### 折旧方式 (DepreciationMethod)
- `直线法`
- `双倍余额递减`
- `年数总和`
- `不折旧`

#### 设备状态 (EquipmentStatus)
- `运行`
- `待机`
- `维修`
- `停机`
- `报废`

#### 工序状态 (ProcedureStatus)
- `未开始`
- `进行中`
- `已完成`
- `暂停`

#### 工序设备角色 (ProcedureEquipmentRole)
- `生产`
- `检测`
- `辅助`

#### 工序物料角色 (ProcedurePartRole)
- `Input` (投入)
- `Output` (产出)
- `Consumable` (辅料)
- `Tooling` (工装)
- `Fixture` (夹具)

#### 往来单位类型 (BusinessPartnerType)
- `Manufacturer` (生产厂家)
- `Supplier` (供应商)
- `ServiceProvider` (服务商)
- `Customer` (客户)

#### 位置类型 (LocationType)
- `Plant` (工厂)
- `Workshop` (车间)
- `Line` (产线)
- `Station` (工位)
- `Warehouse` (仓库)

#### 工艺状态 (PlanStatus)
- `Draft` (草稿)
- `Released` (已发布)
- `Obsolete` (已废弃)

#### 员工状态 (EmployeeStatus)
- `在职`
- `离职`
- `外协`

### B. 分类树示例

#### 物料分类 (PartCategory)
```
结构件
├── 高强钢安装件
├── 铝合金支架
└── 塑料外壳
电子元器件
├── 无源分立元件
│   └── 磁性元件
└── 集成电路
```

#### 设备分类 (EquipmentCategory)
```
机械加工设备
├── 车
├── 铣
├── 磨
└── CNC
检测设备
├── 三坐标
├── 投影仪
└── 硬度计
热处理设备
```

### C. 错误码说明

| 错误码 | 说明 |
|--------|------|
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 409 | 资源冲突（如唯一键冲突） |
| 500 | 服务器内部错误 |

### D. 接口调用示例（curl）

```bash
# 健康检查
curl -X GET "http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/v1/health"

# 创建设备（需要认证）
curl -X POST "http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/api/equipment" \
  -u "sysadmin:DlVya3xYJmR/yiO7" \
  -H "Content-Type: application/json" \
  -d '{
    "equipmentCode": "EQ-0001",
    "equipmentName": "CNC加工中心A",
    "status": "运行"
  }'

# 查询设备列表
curl -X GET "http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/api/equipment?pageNum=1&pageSize=20" \
  -u "sysadmin:DlVya3xYJmR/yiO7"
```

---

## 更新日志

- **v1.0.2**: 修复 OpenAPI 文档，为所有 GET 接口的 `data` 字段补充详细 schema（如 `EquipmentDetail`、`Part`、`PartListItem`、`BOMTreeRoot` 等），与本文档响应示例保持一致。
- **v1.0.1**: 新增 OpenAPI 3.0 规范文件 `openapi.yaml`，支持 Apifox/Swagger/Postman 导入。
- **v1.0.0** (2024-02-11): 初始版本，包含所有核心实体和关系实体的完整 CRUD 接口。
