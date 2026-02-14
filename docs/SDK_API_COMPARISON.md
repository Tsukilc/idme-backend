# iDME SDK 查询接口对比文档

## 官方文档参考
- Query接口: https://support.huaweicloud.com/bestpractice-idme/idme_bestpractice_0014.html
- Find接口: https://support.huaweicloud.com/bestpractice-idme/idme_bestpractice_0013.html
- Select接口: https://support.huaweicloud.com/bestpractice-idme/idme_bestpractice_0016.html

## 接口对比（已实测验证）

| 接口 | URL模式 | 返回字段范围 | 是否需要指定字段 | 适用场景 | 我们的选择 |
|------|---------|-------------|-----------------|---------|-----------|
| **query** | `/query?curPage=X&pageSize=Y` | **仅系统字段**（id, creator, createTime等） | ❌ 否 | 仅需系统字段的场景 | ❌ **不推荐** |
| **list** | `/list?curPage=X&pageSize=Y` | **完整基础属性**（系统字段+业务字段） | ❌ 否 | 通用列表查询 | ✅ **推荐使用** |
| **find** | `/find?curPage=X&pageSize=Y` | **完整属性+关联对象**（基础+级联+扩展） | ❌ 否 | 需要关联数据的场景 | ⚠️ 按需使用 |
| **select** | `/select?curPage=X&pageSize=Y` | **自定义字段集**（只返回指定字段） | ✅ 必须 | 优化性能、自定义VO | ⚠️ 按需使用 |

## 实测验证结果（以BusinessPartner为例）

### 1. query接口 - 仅返回系统字段 ❌

```bash
curl -X POST "http://{SDK}/dynamic/api/BusinessPartner/query?curPage=1&pageSize=1" \
  -H "Content-Type: application/json" \
  -d '{"params":{"condition":{}}}'
```

**返回字段**（8个系统字段）：
```json
[
  "className",
  "createTime",
  "creator",
  "id",
  "lastUpdateTime",
  "modifier",
  "rdmExtensionType",
  "tenant"
]
```

**结论**: ❌ **缺少业务字段**（partnerCode, partnerName, partnerType等）

---

### 2. list接口 - 返回完整基础属性 ✅

```bash
curl -X POST "http://{SDK}/dynamic/api/BusinessPartner/list?curPage=1&pageSize=1" \
  -H "Content-Type: application/json" \
  -d '{"params":{"condition":{}}}'
```

**返回字段**（18个，系统字段+业务字段）：
```json
[
  "addressText",        // 业务字段
  "className",          // 系统字段
  "createTime",         // 系统字段
  "creator",            // 系统字段
  "email",              // 业务字段
  "extra",              // 业务字段
  "id",                 // 系统字段
  "lastUpdateTime",     // 系统字段
  "modifier",           // 系统字段
  "partnerCode",        // 业务字段
  "partnerName",        // 业务字段
  "partnerType",        // 业务字段
  "phone",              // 业务字段
  "rdmDeleteFlag",      // 系统字段
  "rdmExtensionType",   // 系统字段
  "rdmVersion",         // 系统字段
  "tenant",             // 系统字段
  "website"             // 业务字段
]
```

**结论**: ✅ **包含所有基础属性，无需手动指定字段**

---

### 3. find接口 - 返回完整属性+关联对象 ⚠️

```bash
curl -X POST "http://{SDK}/dynamic/api/BusinessPartner/find?curPage=1&pageSize=1" \
  -H "Content-Type: application/json" \
  -d '{"params":{"filter":{}}}'
```

**返回字段**（19个，比list多1个关联字段）：
```json
[
  "addressText",
  "className",
  "createTime",
  "creator",
  "email",
  "extra",
  "id",
  "lastUpdateTime",
  "modifier",
  "partnerCode",
  "partnerContactList",  // ← 多了级联关联对象
  "partnerName",
  "partnerType",
  "phone",
  "rdmDeleteFlag",
  "rdmExtensionType",
  "rdmVersion",
  "tenant",
  "website"
]
```

**结论**: ⚠️ **返回级联数据，数据量更大，性能可能受影响**

---

### 4. select接口 - 自定义字段集 ⚠️

```bash
# 必须指定 selectedField
curl -X POST "http://{SDK}/dynamic/api/BusinessPartner/select?curPage=1&pageSize=1" \
  -H "Content-Type: application/json" \
  -d '{
    "params": {
      "selectedField": [
        {"name": "partnerCode", "nameAs": "code"},
        {"name": "partnerName", "nameAs": "name"}
      ]
    }
  }'
```

**返回字段**（只返回指定的2个）：
```json
{
  "code": "BP-001",
  "name": "XX机床制造公司"
}
```

**不指定字段时的错误**：
```json
{
  "result": "FAIL",
  "error_code": "rdm.coresdk.select.field.is.empty",
  "error_msg": "selectedField is empty."
}
```

**结论**: ⚠️ **必须显式指定字段，不支持通配符或"全选"**

---

## 官方文档说明 vs 实际行为

| 接口 | 官方文档描述 | 实际验证结果 | 差异说明 |
|------|-------------|-------------|---------|
| **query** | "返回数据模型的所有列表属性" | ❌ 只返回系统字段 | **官方文档不准确**，实际只返回系统字段 |
| **list** | 未在提供的文档中详细说明 | ✅ 返回完整基础属性 | **实际最实用**的接口 |
| **find** | "返回所有属性及关联信息" | ✅ 返回基础属性+级联 | 符合文档描述 |
| **select** | "返回指定属性的结果集" | ✅ 只返回指定字段 | 符合文档描述，但不支持通配符 |

---

## 我们的技术选型

### 当前实现（AbstractIdmeDao）

```java
@Override
public List<T> findAll(int pageNum, int pageSize) {
    QueryRequest queryRequest = new QueryRequest();
    queryRequest.setCondition(new HashMap<>());
    // 使用list接口，返回完整的基础属性，无需手动指定字段
    List<T> results = sdkClient.list(getEntityName(), queryRequest, pageNum, pageSize, getEntityClass());
    return results;
}
```

### 选择理由

1. **无需维护字段列表**: `list`接口自动返回所有基础属性，不像`select`需要手动枚举
2. **避免高耦合**: 新增字段时不需要修改DAO代码
3. **性能适中**: 比`find`轻量（不包含级联），比`query`完整
4. **满足大部分场景**: 列表查询通常只需要基础属性，不需要级联关联

### 废弃的方案

我们曾尝试使用`query`接口，但发现只返回系统字段，导致业务字段全为`null`。

也考虑过`select`接口，但需要为每个实体手动维护完整字段列表：
```java
// ❌ 不推荐：高耦合，维护困难
@Override
protected List<String> getSelectFields() {
    return Arrays.asList(
        "id", "creator", "modifier", "createTime", "lastUpdateTime",
        "rdmVersion", "rdmDeleteFlag", "rdmExtensionType", "className",
        "partnerCode", "partnerName", "partnerType", "phone", "email",
        "website", "addressText", "extra"
    );
}
```

---

## 推荐使用指南

### 场景1：通用列表查询（推荐）

```java
// 使用 list 接口
List<BusinessPartner> partners = sdkClient.list(
    "BusinessPartner", 
    new QueryRequest(), 
    1, 10, 
    BusinessPartner.class
);
```

**适用场景**: 
- 用户列表
- 订单列表
- 产品列表
- 任何标准的CRUD列表查询

---

### 场景2：需要关联数据

```java
// 使用 find 接口
List<BusinessPartner> partnersWithContacts = sdkClient.find(
    "BusinessPartner", 
    new QueryRequest(), 
    1, 10, 
    BusinessPartner.class
);
// 返回包含 partnerContactList 的完整数据
```

**适用场景**:
- 需要显示主从表数据
- 需要一次性获取关联对象
- 详情页面展示

**注意**: 数据量更大，性能可能受影响

---

### 场景3：优化性能（自定义字段）

```java
// 使用 select 接口
QueryRequest request = new QueryRequest();
request.setSelectedFields(Arrays.asList(
    new SelectedField("partnerCode", "code"),
    new SelectedField("partnerName", "name")
));
List<Map<String, Object>> simplified = sdkClient.select(
    "BusinessPartner", 
    request, 
    1, 100
);
```

**适用场景**:
- 下拉选择框（只需要ID+名称）
- 大数据量导出（减少网络传输）
- 移动端API（节省流量）

---

### 场景4：仅需系统字段（极少使用）

```java
// 使用 query 接口（已标记为@Deprecated）
List<BusinessPartner> systemFieldsOnly = sdkClient.query(
    "BusinessPartner", 
    new QueryRequest(), 
    1, 10, 
    BusinessPartner.class
);
```

**适用场景**:
- 审计日志查询（只关心创建人、时间）
- 系统管理功能

---

## 性能对比（估算）

| 接口 | 数据量 | 网络传输 | 反序列化开销 | 推荐场景 |
|------|--------|---------|-------------|---------|
| query | 最小 | ~1KB/条 | 低 | 极少使用 |
| **list** | **适中** | **~2-3KB/条** | **适中** | **通用推荐** ✅ |
| find | 大 | ~5-10KB/条 | 高 | 详情页 |
| select | 可控 | 按需 | 低 | 优化场景 |

---

## 总结

1. ✅ **验证了`query`接口确实只返回系统字段，不是代码问题**
2. ✅ **`list`接口是最佳选择，无需手动维护字段列表**
3. ⚠️ **`select`接口必须指定字段，不支持通配符**
4. ✅ **我们的技术选型正确，架构解耦成功**

---

**最后更新**: 2026-02-12  
**验证人**: iDME项目团队
