# iDME SDK 版本管理 Bug 报告 ✅ 已解决

> **状态**: ✅ 已解决  
> **解决时间**: 2026-02-13  
> **解决方案**: 在所有请求的 `params` 中显式添加 `"creator": "xdmAdmin"` 和 `"modifier": "xdmAdmin"`

---

## 🐛 原始问题描述

iDME SDK 的 VersionObject（如 `Part`）在执行 `checkout` -> `update` -> `checkin` 流程时会失败，报错：

```
"The current user is not the modifier of the instance data and cannot be updated."
```

---

## 🔍 根本原因

SDK 在不同操作中对 `modifier` 字段的处理不一致：

| 操作 | modifier 值 | 长度 |
|------|------------|------|
| **CREATE (INSERT)** | `"xdmAdmin "` | 9 字符（有空格） |
| **CREATE (UPDATE)** | `"xdmAdmin"` | 8 字符（无空格） |
| **CHECKOUT** | `"xdmAdmin"` | 8 字符（无空格） |
| **权限校验** | `"xdmAdmin "` | 9 字符（有空格） |

**结果**：SDK 写入的 `modifier` 是 8 字符，但权限校验时使用 9 字符，导致不匹配。

### 核心问题

```sql
-- 数据库中的实际值（checkout 写入）
SELECT modifier FROM smarttechsoftware_part WHERE id = '863490878344273920';
-- 结果: 'xdmAdmin' (8字符)

-- SDK 权限校验时使用的值
-- 'xdmAdmin ' (9字符，有尾部空格)

-- 8 ≠ 9 → 权限校验失败！
```

---

## ✅ 解决方案

### 方案 1：显式指定 creator/modifier（推荐）

在所有请求的 `params` 中显式添加 `creator` 和 `modifier` 字段：

```json
{
  "params": {
    "id": "...",
    "creator": "xdmAdmin",
    "modifier": "xdmAdmin",
    // 其他业务字段...
  }
}
```

**优点**：
- ✅ 立即可用，无需修改数据库
- ✅ 确保字段值一致（都是 8 字符）
- ✅ 已在代码中实现（`IdmeSdkClient.enrichWithUserFields()`）

### 方案 2：MySQL 触发器（备选）

如果无法修改请求，可在 MySQL 层添加触发器自动 trim：

```sql
DELIMITER $$
CREATE TRIGGER trim_part_before_insert
BEFORE INSERT ON smarttechsoftware_part
FOR EACH ROW
BEGIN
    SET NEW.creator = TRIM(NEW.creator);
    SET NEW.modifier = TRIM(NEW.modifier);
END$$

CREATE TRIGGER trim_part_before_update
BEFORE UPDATE ON smarttechsoftware_part
FOR EACH ROW
BEGIN
    SET NEW.creator = TRIM(NEW.creator);
    SET NEW.modifier = TRIM(NEW.modifier);
END$$
DELIMITER ;
```

---

## 📋 验证测试

### 测试脚本

```bash
./test-fixed-workflow-v2.sh
```

### 测试结果

```
✅ Part 创建成功       (creator=8, modifier=8)
✅ Checkout 成功       (creator=8, modifier=8)
✅ Update 成功         (之前会失败)
✅ Checkin 成功        (之前会失败)
✅ 完整流程验证通过
```

---

## 🔧 代码修改

### 修改的文件

1. **`IdmeSdkClient.java`**
   - 移除 `Authorization` 头
   - 添加 `enrichWithUserFields()` 方法
   - 在所有请求中自动注入 `creator` 和 `modifier`

2. **`SDK_CURL_COMMANDS.md`**
   - 更新所有示例，添加 `creator`/`modifier` 字段
   - 添加完整工作流示例

### 关键代码

```java
private Map<String, Object> enrichWithUserFields(Object params) {
    Map<String, Object> map = params instanceof Map
        ? (Map<String, Object>) params
        : objectMapper.convertValue(params, Map.class);
    map.put("creator", "xdmAdmin");
    map.put("modifier", "xdmAdmin");
    return map;
}
```

---

## 📚 相关文档

- **CURL 命令参考**: `SDK_CURL_COMMANDS.md`
- **完整测试脚本**: `test-fixed-workflow-v2.sh`
- **开发文档**: `DEVELOPMENT.md`

---

## 📊 影响范围

### 受影响的实体

- ✅ **Part** (VersionObject) - 主要受影响
- ⚠️ **WorkingPlan** (VersionObject) - 可能受影响
- ⚠️ **其他 VersionObject** - 可能受影响

### 不受影响的实体

- ✅ **BusinessPartner** (普通对象)
- ✅ **Unit** (普通对象)
- ✅ **Employee** (普通对象)
- ✅ **Department** (普通对象)

---

## 🎯 总结

### 问题本质

SDK 内部对用户名的处理不一致（有时 trim，有时不 trim），导致版本对象的权限校验失败。

### 解决方法

通过在应用层统一注入 8 字符的 `creator`/`modifier`，绕过 SDK 的不一致行为。

### 状态

✅ **已解决** - 所有测试通过，完整流程正常工作。

---

**最后更新**: 2026-02-13
**报告人**: 开发团队
**解决人**: 开发团队
