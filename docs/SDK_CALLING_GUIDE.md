# iDME SDK调用指南

本文档详细说明如何在DAO层调用iDME SDK接口，包括请求构造、响应处理、字段规则等。

---

## 1. SDK架构概述

### 1.1 核心组件

**IdmeSdkClient**（`/src/main/java/com/tsukilc/idme/client/IdmeSdkClient.java`）

- **作用**：封装所有与 iDME SDK 的 HTTP 通信
- **技术栈**：OkHttp + Jackson
- **自动处理**：
  - creator/modifier 字段自动注入（固定值："sysadmin 1"）
  - 系统字段过滤（update操作）
  - 数组响应转单对象（create/get/update）
  - 错误统一包装

### 1.2 SDK配置

**配置文件**：`application.properties`

```properties
idme.sdk.base-url=http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services
idme.sdk.timeout=30000
```

**URL构建规则**：
```
{base-url}/dynamic/api/{EntityName}/{operation}
```

例如：
- `POST http://...rdm.../services/dynamic/api/Employee/create`
- `POST http://...rdm.../services/dynamic/api/Part/list?curPage=1&pageSize=20`

### 1.3 请求/响应包装

**请求包装**（`RdmRequest`）：
```java
public class RdmRequest<T> {
    private T params;           // 实际参数（自动注入creator/modifier）
    private String applicationId;  // 可选
}
```

**响应包装**（`RdmResponse`）：
```java
public class RdmResponse<T> {
    private String result;      // "SUCCESS" 或 "FAIL"
    private T data;             // 实际数据
    private List<String> errors;  // 错误信息数组
}
```

---

## 2. 基础调用模式

### 2.1 GET操作 - 根据ID查询单个实体

#### 代码示例

```java
@Autowired
private IdmeSdkClient sdkClient;

// 查询Employee
String employeeId = "863871307018674176";
Employee employee = sdkClient.get("Employee", employeeId, Employee.class);
```

#### 请求JSON

```json
{
  "params": {
    "id": "863871307018674176",
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  },
  "applicationId": null
}
```

#### 响应JSON（SDK返回数组，IdmeSdkClient自动取第一个元素）

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "863871307018674176",
      "employeeNo": "E001",
      "employeeName": "张三",
      "dept": {
        "id": "863870688249782272",
        "displayName": "研发部",
        "className": "Department"
      },
      "status": "Active",
      "phone": "13800138000",
      "email": "zhangsan@example.com",
      "creator": "sysadmin 1",
      "createTime": "2026-02-13T11:56:09.546+0800",
      "modifier": "sysadmin 1",
      "lastUpdateTime": "2026-02-13T11:56:09.546+0800",
      "rdmVersion": 1,
      "rdmDeleteFlag": 0,
      "className": "Employee"
    }
  ],
  "errors": []
}
```

#### 字段说明

| 字段 | 说明 |
|------|------|
| id | 实体唯一标识（由SDK生成） |
| dept | 对象引用，包含id、displayName、className |
| status | 状态枚举值（SDK返回enName） |
| creator/modifier | 创建人/修改人（自动注入） |
| rdmVersion | 版本号（每次update自动递增） |
| rdmDeleteFlag | 删除标记（0=未删除，1=已删除） |

---

### 2.2 LIST操作 - 分页查询列表

#### 2.2.1 无条件查询

```java
QueryRequest queryRequest = new QueryRequest();
queryRequest.setCondition(new HashMap<>());  // 空条件 = 查询所有

List<Unit> units = sdkClient.list("Unit", queryRequest, 1, 10, Unit.class);
// curPage从1开始，pageSize最大值建议不超过1000
```

#### 请求JSON

```json
{
  "params": {
    "condition": {},
    "orderBy": null,
    "orderDirection": null,
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

#### 响应JSON

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "863539214308876288",
      "unitName": "件",
      "unitDisplayName": "件",
      "creator": "xdmAdmin",
      "createTime": "2026-02-12T21:56:32.474+0800",
      "rdmVersion": 1,
      "className": "Unit"
    },
    {
      "id": "863463339341127680",
      "unitName": "米",
      "unitDisplayName": "m",
      "creator": "xdmAdmin",
      "createTime": "2026-02-12T16:55:23.123+0800",
      "rdmVersion": 1,
      "className": "Unit"
    }
  ],
  "errors": []
}
```

#### 2.2.2 条件查询

```java
QueryRequest queryRequest = new QueryRequest();
Map<String, Object> condition = new HashMap<>();

// 精确匹配
condition.put("status", "Active");

// 嵌套对象引用查询
condition.put("dept.id", "DEPT001");

// 版本对象特殊查询
condition.put("latest", true);  // 只查询最新版本
condition.put("partNumber", "P001");  // 物料编号

queryRequest.setCondition(condition);
queryRequest.setOrderBy("employeeNo");  // 排序字段
queryRequest.setOrderDirection("ASC");  // ASC 或 DESC

List<Employee> employees = sdkClient.list("Employee", queryRequest, 1, 10, Employee.class);
```

#### 条件查询请求JSON

```json
{
  "params": {
    "condition": {
      "status": "Active",
      "dept.id": "DEPT001"
    },
    "orderBy": "employeeNo",
    "orderDirection": "ASC",
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

---

### 2.3 CREATE操作 - 创建实体

#### 2.3.1 普通实体创建（Location）

```java
Location location = new Location();
location.setLocationCode("LOC001");
location.setLocationName("总装车间");
location.setLocationType("Workshop");
location.setAddressText("一号厂房");
location.setRemarks("测试位置");
// 注意：id不需要填写，会自动生成
// creator/modifier 会被 IdmeSdkClient 自动注入

Location created = sdkClient.create("Location", location, Location.class);
```

#### 请求JSON

```json
{
  "params": {
    "locationCode": "LOC001",
    "locationName": "总装车间",
    "locationType": "Workshop",
    "addressText": "一号厂房",
    "remarks": "测试位置",
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

**注意**：
- ❌ **id字段不需要填写**，SDK会自动生成
- ❌ **needSetNullAttrs字段不需要填写**（当前代码不使用）
- ✅ creator/modifier会被IdmeSdkClient自动注入

#### 响应JSON（SDK返回数组，取第一个元素）

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "864114310232678400",
      "locationCode": "LOC001",
      "locationName": "总装车间",
      "locationType": "Workshop",
      "addressText": "一号厂房",
      "remarks": "测试位置",
      "creator": "sysadmin 1",
      "createTime": "2026-02-14T03:52:41.123+0800",
      "modifier": "sysadmin 1",
      "lastUpdateTime": "2026-02-14T03:52:41.123+0800",
      "rdmVersion": 1,
      "rdmDeleteFlag": 0,
      "className": "Location"
    }
  ],
  "errors": []
}
```

#### 2.3.2 包含对象引用的创建（Equipment）

```java
Equipment equipment = new Equipment();
equipment.setEquipmentCode("EQU001");
equipment.setEquipmentName("数控车床");
equipment.setSerialNumber("SN123456");
equipment.setStatus("Standby");

// 对象引用：制造商（BusinessPartner）
ObjectReference manufacturer = new ObjectReference();
manufacturer.setId("1");  // create时只需要设置id即可
equipment.setManufacturerName(manufacturer);

// 对象引用：供应商
ObjectReference supplier = new ObjectReference();
supplier.setId("1");
equipment.setSupplierName(supplier);

// 对象引用：位置
ObjectReference location = new ObjectReference();
location.setId("864114310232678400");
equipment.setLocationRef(location);

Equipment created = sdkClient.create("Equipment", equipment, Equipment.class);
```

#### 请求JSON

```json
{
  "params": {
    "equipmentCode": "EQU001",
    "equipmentName": "数控车床",
    "serialNumber": "SN123456",
    "status": "Standby",
    "manufacturerName": {
      "id": "1"
    },
    "supplierName": {
      "id": "1"
    },
    "locationRef": {
      "id": "864114310232678400"
    },
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

**对象引用规则**：
- create时：只需设置 `id` 字段
- SDK返回：会包含完整的 `id`、`displayName`、`className` 等字段

#### 响应JSON（显示对象引用完整信息）

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "864115032261140480",
      "equipmentCode": "EQU001",
      "equipmentName": "数控车床",
      "manufacturerName": {
        "id": "1",
        "displayName": "华为技术有限公司",
        "className": "BusinessPartner"
      },
      "supplierName": {
        "id": "1",
        "displayName": "华为技术有限公司",
        "className": "BusinessPartner"
      },
      "locationRef": {
        "id": "864114310232678400",
        "displayName": "总装车间",
        "className": "Location"
      },
      "creator": "sysadmin 1",
      "createTime": "2026-02-14T04:04:38.172+0800",
      "rdmVersion": 1,
      "className": "Equipment"
    }
  ],
  "errors": []
}
```

#### 2.3.3 版本对象创建（Part/WorkingPlan）

版本对象（VersionObject）需要特殊处理：首次创建时传入**空的ObjectReference**，SDK会自动创建master和branch。

```java
Part part = new Part();
part.setPartNumber("P001");
part.setPartName("螺丝");
part.setModelSpec("M6");

// 版本对象必需字段：传空的ObjectReference
ObjectReference master = new ObjectReference();
ObjectReference branch = new ObjectReference();
part.setMaster(master);
part.setBranch(branch);

Part created = sdkClient.create("Part", part, Part.class);
```

#### 请求JSON

```json
{
  "params": {
    "partNumber": "P001",
    "partName": "螺丝",
    "modelSpec": "M6",
    "master": {},
    "branch": {},
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

#### 响应JSON（SDK自动创建版本管理字段）

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "863478364051087361",
      "partNumber": "P001",
      "partName": "螺丝",
      "modelSpec": "M6",
      "master": {
        "id": "863478364051087362",
        "className": "PartMaster"
      },
      "branch": {
        "id": "863478364051087363",
        "className": "PartBranch"
      },
      "version": "A.1",
      "versionCode": 1,
      "iteration": 1,
      "latest": true,
      "latestVersion": true,
      "latestIteration": true,
      "workingCopy": false,
      "workingState": {
        "code": "CHECKED_IN",
        "cnName": "已检入",
        "enName": "Checked In"
      },
      "creator": "sysadmin 1",
      "createTime": "2026-02-12T12:34:56.789+0800",
      "rdmVersion": 1,
      "className": "Part"
    }
  ],
  "errors": []
}
```

**版本对象关键字段说明**：

| 字段 | 说明 |
|------|------|
| master | 主对象引用（SDK自动创建，用于版本管理） |
| branch | 分支对象引用（SDK自动创建） |
| version | 版本号（如"A.1"，系统生成） |
| latest | 是否最新有效版本（查询时常用） |
| workingCopy | 是否工作副本（已检出=true） |
| workingState | 工作状态（CHECKED_IN/CHECKED_OUT） |

---

### 2.4 UPDATE操作 - 更新实体

#### 代码示例

```java
// 1. 先GET获取当前数据
Employee employee = sdkClient.get("Employee", "863871307018674176", Employee.class);

// 2. 修改字段
employee.setEmployeeName("张三（已更新）");
employee.setPhone("13900139000");

// 3. 执行UPDATE（系统字段会被自动过滤）
Employee updated = sdkClient.update("Employee", employee, Employee.class);
```

#### 字段更新规则

**IdmeSdkClient自动过滤的只读字段**：
- `createTime`
- `lastUpdateTime`
- `rdmDeleteFlag`
- `rdmExtensionType`
- `className`
- `tenant`

**自动注入字段**：
- `creator` = "sysadmin 1"
- `modifier` = "sysadmin 1"

#### 请求JSON

```json
{
  "params": {
    "id": "863871307018674176",
    "employeeNo": "E001",
    "employeeName": "张三（已更新）",
    "phone": "13900139000",
    "dept": {
      "id": "863870688249782272"
    },
    "status": "Active",
    "rdmVersion": 1,
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

**注意**：
- ✅ **id字段必填**（标识要更新哪个实体）
- ✅ **rdmVersion字段建议保留**（乐观锁）
- ❌ 只读字段（createTime等）已被自动过滤

#### 响应JSON

```json
{
  "result": "SUCCESS",
  "data": [
    {
      "id": "863871307018674176",
      "employeeName": "张三（已更新）",
      "phone": "13900139000",
      "modifier": "sysadmin 1",
      "lastUpdateTime": "2026-02-14T05:12:34.567+0800",
      "rdmVersion": 2,
      "className": "Employee"
    }
  ],
  "errors": []
}
```

**版本号自动递增**：`rdmVersion` 从 1 → 2

---

### 2.5 DELETE操作 - 删除实体

#### 代码示例

```java
sdkClient.delete("Location", "864114310232678400");
```

#### 请求JSON

```json
{
  "params": {
    "id": "864114310232678400",
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

#### 响应

DELETE操作无返回值（void）。

**注意**：SDK可能执行软删除（设置rdmDeleteFlag=1）而非物理删除，具体行为取决于实体配置。

---

## 3. 高级模式

### 3.1 对象引用（ObjectReference）

#### 什么是对象引用

对象引用用于表示实体之间的关联关系，类似于数据库的外键。

#### ObjectReference结构

```java
public class ObjectReference {
    private String id;            // 对象ID（必填）
    private String displayName;   // 显示名称（SDK返回）
    private String className;     // 类名（SDK返回）
}
```

#### create时如何构造

```java
// 方式1：通过构造函数
ObjectReference ref = new ObjectReference("DEPT001");

// 方式2：通过setter
ObjectReference ref = new ObjectReference();
ref.setId("DEPT001");

// ❌ 不需要设置displayName和className（SDK会自动填充）
```

#### 响应中的结构

SDK返回的ObjectReference包含完整信息：

```json
{
  "dept": {
    "id": "863870688249782272",
    "displayName": "研发部",
    "className": "Department"
  }
}
```

#### DAO层如何处理

**Entity中的定义**：
```java
@Data
public class Employee {
    private ObjectReference dept;  // 对象引用
}
```

**Service层转换**：
```java
// DTO → Entity（前端传字符串ID）
if (dto.getDept() != null) {
    entity.setDept(new ObjectReference(dto.getDept()));
}

// Entity → VO（提取ID返回给前端）
if (entity.getDept() != null) {
    vo.setDept(entity.getDept().getId());
}
```

---

### 3.2 枚举类型（PartnerTypeRef等）

某些字段是枚举引用类型，需要自定义序列化/反序列化。

#### 枚举引用结构

```java
public class PartnerTypeRef {
    private String code;      // 枚举代码
    private String cnName;    // 中文名
    private String enName;    // 英文名（常用）
    private String alias;     // 别名
}
```

#### 发送格式（序列化）

create时只发送枚举的 `enName`：

```java
// Entity定义
@JsonSerialize(using = PartnerTypeRefSerializer.class)
private PartnerTypeRef partnerType;

// 序列化器实现
public class PartnerTypeRefSerializer extends JsonSerializer<PartnerTypeRef> {
    @Override
    public void serialize(PartnerTypeRef value, JsonGenerator gen, SerializerProvider serializers) {
        gen.writeString(value.getEnName());  // 只发送enName
    }
}
```

**请求JSON**：
```json
{
  "partnerType": "Aerospace"
}
```

#### 接收格式（反序列化）

SDK返回完整的枚举对象：

**响应JSON**：
```json
{
  "partnerType": {
    "code": "ASP",
    "cnName": "航空航天",
    "enName": "Aerospace",
    "alias": "Aerospace"
  }
}
```

**反序列化器**：
```java
public class PartnerTypeRefDeserializer extends JsonDeserializer<PartnerTypeRef> {
    @Override
    public PartnerTypeRef deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode node = p.getCodec().readTree(p);
        PartnerTypeRef ref = new PartnerTypeRef();
        ref.setCode(node.get("code").asText());
        ref.setCnName(node.get("cnName").asText());
        ref.setEnName(node.get("enName").asText());
        ref.setAlias(node.get("alias").asText());
        return ref;
    }
}
```

---

### 3.3 关联对象（Link实体）

Link实体用于表示两个实体之间的多对多关联关系，如设备-备件、工序-物料等。

#### 关联实体示例（ProcedurePartLink）

```java
ProcedurePartLink link = new ProcedurePartLink();
link.setRole("Fixture");  // 角色（如：夹具、工具等）
link.setQuantity(10);
link.setName("工序-物料关联");

// source：工序
ObjectReference procedureRef = new ObjectReference("PROC001");
link.setSource(procedureRef);
link.setProcedure(procedureRef);  // 同时设置procedure字段

// target：物料
ObjectReference partRef = new ObjectReference("PART001");
link.setTarget(partRef);
link.setPart1(partRef);  // 同时设置part1字段

// UOM：计量单位
ObjectReference uomRef = new ObjectReference("UNIT001");
link.setUom(uomRef);

ProcedurePartLink created = sdkClient.create("ProcedurePartLink", link, ProcedurePartLink.class);
```

#### 请求JSON

```json
{
  "params": {
    "role": "Fixture",
    "quantity": 10,
    "name": "工序-物料关联",
    "source": {
      "id": "PROC001"
    },
    "procedure": {
      "id": "PROC001"
    },
    "target": {
      "id": "PART001"
    },
    "part1": {
      "id": "PART001"
    },
    "uom": {
      "id": "UNIT001"
    },
    "creator": "sysadmin 1",
    "modifier": "sysadmin 1"
  }
}
```

**Link实体标准字段**：
- `source`：源对象引用
- `target`：目标对象引用
- 通常还有对应的业务字段（如`procedure`、`part1`），值与source/target相同

#### 常见Link实体

| Link实体 | source | target | 说明 |
|---------|--------|--------|------|
| ProcedurePartLink | WorkingProcedure | Part | 工序-物料关联 |
| ProcedureEquipmentLink | WorkingProcedure | Equipment | 工序-设备关联 |
| PlanProcedureLink | WorkingPlan | WorkingProcedure | 计划-工序关联 |
| EquipmentSparePartLink | Equipment | Part | 设备-备件关联 |

---

## 4. DAO层封装

### 4.1 AbstractIdmeDao使用

所有DAO继承 `AbstractIdmeDao<T, ID>` 即可自动获得6个标准CRUD方法。

#### 继承方式

```java
@Repository
public class EmployeeDao extends AbstractIdmeDao<Employee, String> {

    @Override
    protected String getEntityName() {
        return "Employee";  // 对应SDK中的实体名称
    }

    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    // 自动获得以下6个方法：
    // - T create(T entity)
    // - T update(T entity)
    // - void delete(ID id)
    // - T findById(ID id)
    // - List<T> findAll(int pageNum, int pageSize)
    // - List<T> findByCondition(Map<String, Object> condition, int pageNum, int pageSize)
}
```

#### 自动获得的方法

| 方法 | 说明 |
|------|------|
| `create(T entity)` | 创建实体 |
| `update(T entity)` | 更新实体 |
| `delete(ID id)` | 删除实体 |
| `findById(ID id)` | 根据ID查询 |
| `findAll(int pageNum, int pageSize)` | 分页查询所有 |
| `findByCondition(Map, int, int)` | 条件查询 |

---

### 4.2 自定义查询

#### 在DAO中添加自定义方法

```java
@Repository
public class EmployeeDao extends AbstractIdmeDao<Employee, String> {

    // ... getEntityName() 和 getEntityClass() ...

    /**
     * 根据部门ID查询员工
     */
    public List<Employee> findByDept(String deptId, int pageNum, int pageSize) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("dept.id", deptId);  // 嵌套字段查询
        return findByCondition(condition, pageNum, pageSize);
    }

    /**
     * 查询在职员工
     */
    public List<Employee> findActiveEmployees(int pageNum, int pageSize) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", "Active");
        return findByCondition(condition, pageNum, pageSize);
    }

    /**
     * 根据员工编号查询
     */
    public Employee findByEmployeeNo(String employeeNo) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("employeeNo", employeeNo);
        List<Employee> results = findByCondition(condition, 1, 1);
        return results.isEmpty() ? null : results.get(0);
    }
}
```

#### 条件查询语法

| 条件类型 | 示例 | 说明 |
|---------|------|------|
| 精确匹配 | `condition.put("status", "Active")` | 字段值精确匹配 |
| 嵌套对象 | `condition.put("dept.id", "DEPT001")` | 引用对象的字段 |
| 版本对象 | `condition.put("latest", true)` | 查询最新版本 |

---

## 5. 公共字段处理

### 5.1 自动注入字段

**IdmeSdkClient自动注入**（无需手动设置）：

| 字段 | 值 | 说明 |
|------|---|------|
| creator | "sysadmin 1" | 创建人（固定值） |
| modifier | "sysadmin 1" | 修改人（固定值） |

**tenant字段**：
- 当前代码**不主动填充** tenant
- SDK会根据用户权限自动分配租户

---

### 5.2 只读字段

以下字段由SDK管理，**update时会被自动过滤**：

| 字段 | 说明 |
|------|------|
| createTime | 创建时间（SDK自动设置） |
| lastUpdateTime | 最后更新时间（SDK自动更新） |
| rdmDeleteFlag | 删除标记（0=未删除，1=已删除） |
| rdmExtensionType | 扩展类型（SDK自动设置） |
| className | 类名（SDK自动设置） |
| tenant | 租户（SDK自动分配） |

---

### 5.3 needSetNullAttrs字段

#### SDK功能说明

`needSetNullAttrs` 是SDK的高级功能，用于**明确指定哪些字段应该被设置为null**（而不是保持原值）。

#### 当前代码状态

✅ **当前代码不使用该字段**
- IdmeSdkClient 不主动填充 needSetNullAttrs
- update操作采用字段值更新方式，未用到这个功能

#### 何时需要使用

如果未来需要明确将某个字段置空（如清空备注），可以手动构造带needSetNullAttrs的请求：

```java
Map<String, Object> params = new HashMap<>();
params.put("id", "EMP001");
params.put("employeeName", "张三");
params.put("needSetNullAttrs", Arrays.asList("remarks"));  // 明确置空remarks字段

Employee updated = sdkClient.update("Employee", params, Employee.class);
```

---

## 6. 错误处理

### 6.1 统一响应格式

SDK统一返回 **HTTP 200** 状态码，通过 `result` 字段区分成功/失败。

#### 成功响应

```json
{
  "result": "SUCCESS",
  "data": [...],
  "errors": []
}
```

#### 失败响应

```json
{
  "result": "FAIL",
  "data": null,
  "errors": [
    "[manager] can not be null"
  ],
  "error_msg": "字段验证失败",
  "error_code": "com.huawei.innovation.rdm.coresdk.basic.field.notNull"
}
```

---

### 6.2 错误解析

**IdmeSdkClient自动解析错误**：

```java
if (!"SUCCESS".equals(rdmResponse.getResult())) {
    String errorMsg = "SDK 调用失败";
    if (rdmResponse.getErrors() != null && !rdmResponse.getErrors().isEmpty()) {
        errorMsg += ": " + String.join(", ", rdmResponse.getErrors());
    }
    // 尝试提取error_msg和error_code
    if (jsonNode.has("error_msg")) {
        errorMsg += " - " + jsonNode.get("error_msg").asText();
    }
    if (jsonNode.has("error_code")) {
        errorMsg += " (错误码: " + jsonNode.get("error_code").asText() + ")";
    }
    throw new IdmeException(errorMsg);
}
```

#### 常见错误码

| 错误码 | 说明 | 解决方案 |
|--------|------|---------|
| `com.huawei.innovation.rdm.coresdk.basic.field.notNull` | 必填字段为空 | 检查实体必填字段 |
| `com.huawei.innovation.rdm.coresdk.basic.unique.violated` | 唯一性约束冲突 | 检查唯一字段（如编号） |
| `com.huawei.innovation.rdm.coresdk.basic.version.conflict` | 版本冲突（乐观锁） | 重新GET最新数据后再update |

---

## 7. 完整示例代码

### 7.1 新增一个实体支持的完整流程

假设需要新增一个"供应商"实体的支持，完整步骤如下：

#### Step 1: 定义Entity

`/src/main/java/com/tsukilc/idme/entity/Supplier.java`

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    // === SDK系统字段 ===
    private String id;
    private String creator;
    private String modifier;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;
    private Integer rdmVersion;
    private Integer rdmDeleteFlag;
    private String rdmExtensionType;
    private String className;

    // === 业务字段 ===
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String phone;
    private String email;
    private ObjectReference category;  // 分类
}
```

#### Step 2: 实现DAO

`/src/main/java/com/tsukilc/idme/dao/SupplierDao.java`

```java
@Repository
public class SupplierDao extends AbstractIdmeDao<Supplier, String> {

    @Override
    protected String getEntityName() {
        return "Supplier";  // 对应SDK实体名
    }

    @Override
    protected Class<Supplier> getEntityClass() {
        return Supplier.class;
    }

    // 自定义查询：根据供应商编码查询
    public Supplier findByCode(String supplierCode) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("supplierCode", supplierCode);
        List<Supplier> results = findByCondition(condition, 1, 1);
        return results.isEmpty() ? null : results.get(0);
    }
}
```

#### Step 3: 定义DTO

`/src/main/java/com/tsukilc/idme/dto/SupplierCreateDTO.java`

```java
@Data
public class SupplierCreateDTO {
    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode;

    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    private String contactPerson;
    private String phone;
    private String email;
    private String category;  // 前端传分类ID（字符串）
}
```

#### Step 4: 定义VO

`/src/main/java/com/tsukilc/idme/vo/SupplierVO.java`

```java
@Data
public class SupplierVO {
    private String id;
    private String supplierCode;
    private String supplierName;
    private String contactPerson;
    private String phone;
    private String email;
    private String category;  // 返回分类ID（字符串）
}
```

#### Step 5: 实现Service

`/src/main/java/com/tsukilc/idme/service/SupplierService.java`

```java
@Service
public class SupplierService {

    @Autowired
    private SupplierDao supplierDao;

    // CREATE
    public SupplierVO create(SupplierCreateDTO dto) {
        Supplier entity = convertToEntity(dto);
        Supplier created = supplierDao.create(entity);
        return convertToVO(created);
    }

    // UPDATE
    public SupplierVO update(String id, SupplierCreateDTO dto) {
        Supplier existing = supplierDao.findById(id);
        if (existing == null) {
            throw new IdmeException("Supplier not found: " + id);
        }
        updateEntityFromDTO(existing, dto);
        Supplier updated = supplierDao.update(existing);
        return convertToVO(updated);
    }

    // DELETE
    public void delete(String id) {
        supplierDao.delete(id);
    }

    // GET
    public SupplierVO getById(String id) {
        Supplier entity = supplierDao.findById(id);
        if (entity == null) {
            throw new IdmeException("Supplier not found: " + id);
        }
        return convertToVO(entity);
    }

    // LIST
    public List<SupplierVO> list(int pageNum, int pageSize) {
        List<Supplier> entities = supplierDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    // DTO -> Entity转换
    private Supplier convertToEntity(SupplierCreateDTO dto) {
        Supplier entity = new Supplier();
        entity.setSupplierCode(dto.getSupplierCode());
        entity.setSupplierName(dto.getSupplierName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());

        // 对象引用：分类
        if (dto.getCategory() != null) {
            entity.setCategory(new ObjectReference(dto.getCategory()));
        }

        return entity;
    }

    // Entity -> VO转换
    private SupplierVO convertToVO(Supplier entity) {
        SupplierVO vo = new SupplierVO();
        vo.setId(entity.getId());
        vo.setSupplierCode(entity.getSupplierCode());
        vo.setSupplierName(entity.getSupplierName());
        vo.setContactPerson(entity.getContactPerson());
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());

        // 提取分类ID
        if (entity.getCategory() != null) {
            vo.setCategory(entity.getCategory().getId());
        }

        return vo;
    }

    private void updateEntityFromDTO(Supplier entity, SupplierCreateDTO dto) {
        entity.setSupplierName(dto.getSupplierName());
        entity.setContactPerson(dto.getContactPerson());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        if (dto.getCategory() != null) {
            entity.setCategory(new ObjectReference(dto.getCategory()));
        }
    }
}
```

#### Step 6: 实现Controller

`/src/main/java/com/tsukilc/idme/controller/SupplierController.java`

```java
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ApiResponse<List<SupplierVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<SupplierVO> suppliers = supplierService.list(pageNum, pageSize);
        return ApiResponse.success(suppliers);
    }

    @PostMapping
    public ApiResponse<SupplierVO> create(@RequestBody @Valid SupplierCreateDTO dto) {
        SupplierVO supplier = supplierService.create(dto);
        return ApiResponse.success(supplier);
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierVO> getById(@PathVariable String id) {
        SupplierVO supplier = supplierService.getById(id);
        return ApiResponse.success(supplier);
    }

    @PutMapping("/{id}")
    public ApiResponse<SupplierVO> update(
            @PathVariable String id,
            @RequestBody @Valid SupplierCreateDTO dto) {
        SupplierVO supplier = supplierService.update(id, dto);
        return ApiResponse.success(supplier);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        supplierService.delete(id);
        return ApiResponse.success(null);
    }
}
```

---

## 8. 常见问题

### Q1: needSetNullAttrs是否必填？

**A**: ❌ **不需要填写**。当前IdmeSdkClient不使用该字段，SDK会根据请求中字段的存在性自动判断。

---

### Q2: id字段create时是否需要？

**A**: ❌ **不需要填写**。SDK会自动生成ID并在响应中返回。如果手动设置id，可能导致创建失败。

---

### Q3: 如何处理关联对象？

**A**: 使用 `ObjectReference` 表示引用：

```java
// create时只设置id
ObjectReference ref = new ObjectReference("DEPT001");
entity.setDept(ref);

// SDK返回包含displayName和className
// { "id": "DEPT001", "displayName": "研发部", "className": "Department" }
```

---

### Q4: 版本对象如何创建？

**A**: 首次创建时传**空的ObjectReference**：

```java
Part part = new Part();
part.setPartNumber("P001");

// 传空的master和branch，SDK会自动创建
part.setMaster(new ObjectReference());
part.setBranch(new ObjectReference());

Part created = sdkClient.create("Part", part, Part.class);
// created.getMaster().getId() 不为空，SDK已自动创建
```

---

### Q5: 如何查询最新版本的物料？

**A**: 使用条件查询，设置 `latest = true`：

```java
Map<String, Object> condition = new HashMap<>();
condition.put("latest", true);  // 只查询最新版本
condition.put("partNumber", "P001");  // 可选：按物料编号

List<Part> parts = partDao.findByCondition(condition, 1, 10);
```

---

### Q6: creator/modifier需要手动设置吗？

**A**: ❌ **不需要**。IdmeSdkClient会自动注入固定值"sysadmin 1"，确保版本管理的modifier一致性。

---

### Q7: 分页参数curPage是从0还是从1开始？

**A**: **从1开始**。SDK的分页从第1页开始，不是0。

```java
List<Unit> page1 = sdkClient.list("Unit", queryRequest, 1, 10, Unit.class);  // 第1页
List<Unit> page2 = sdkClient.list("Unit", queryRequest, 2, 10, Unit.class);  // 第2页
```

---

### Q8: 为什么create/get/update返回的data是数组？

**A**: 这是SDK设计规范。IdmeSdkClient已自动处理，取数组第一个元素返回，调用者无需关心。

---

### Q9: 如何处理枚举类型字段？

**A**: 为枚举引用类型创建自定义序列化/反序列化器：

```java
// Entity定义
@JsonSerialize(using = StatusRefSerializer.class)
@JsonDeserialize(using = StatusRefDeserializer.class)
private StatusRef status;

// create时只发送enName，SDK返回完整对象
```

---

### Q10: update失败提示版本冲突怎么办？

**A**: 版本冲突（乐观锁）：

```java
// 1. 重新GET最新数据
Employee latest = employeeDao.findById(id);

// 2. 应用修改
latest.setEmployeeName("新名称");

// 3. 再次update
Employee updated = employeeDao.update(latest);
```

---

## 9. 参考资料

| 文档 | 路径 |
|------|------|
| SDK实体定义（权威） | `/docs/sdk_entities.yaml` |
| SDK调用示例（curl） | `/sdk_samples.md` |
| IdmeSdkClient源码 | `/src/main/java/com/tsukilc/idme/client/IdmeSdkClient.java` |
| AbstractIdmeDao源码 | `/src/main/java/com/tsukilc/idme/dao/AbstractIdmeDao.java` |
| 项目编码规范 | `/CLAUDE.md` |

---

**文档生成时间**：2026-02-14
**SDK版本**：基于华为RDM SDK动态API
**维护者**：iDME项目组
