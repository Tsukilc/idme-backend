# iDME SDK CURL 命令参考

本文档提供了常用的 iDME SDK CURL 命令示例，用于快速测试和调试。

> **⚠️ 重要提示**：
> 1. 所有请求的 `params` 中必须显式包含 `"creator": "xdmAdmin"` 和 `"modifier": "xdmAdmin"`（8字符，无尾部空格）
> 2. 不需要 `Authorization` 头
> 3. 必须包含 `X-Auth-Token` 头（任意值，如 "test"）

---

## 基础配置

```bash
# API 基础 URL
BASE_URL="http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api"

# 常用实体 ID（你的环境）
SUPPLIER_ID="863454055270195200"
UNIT_ID="863539214308876288"
CATEGORY_ID="863477803427831808"
```

---

## 📋 完整工作流：Part（物料）版本管理

### 完整流程脚本

```bash
#!/bin/bash
BASE_URL="http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api"

# Step 1: 创建 Part
echo "=== 创建 Part ==="
CREATE_RESPONSE=$(curl -s -X POST "${BASE_URL}/Part/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "partNumber": "DEMO-001",
      "partName": "演示物料",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin",
      "supplierName": {"id": "863454055270195200"},
      "unit": {"id": "863539214308876288"},
      "category": {"id": "863477803427831808"},
      "master": {},
      "branch": {},
      "specification": "100x50x30",
      "material": "不锈钢",
      "stockQty": 100
    }
  }')

MASTER_ID=$(echo "$CREATE_RESPONSE" | jq -r '.data[0].master.id')
echo "✓ Master ID: ${MASTER_ID}"

# Step 2: Checkout（创建工作副本）
echo "=== Checkout ==="
CHECKOUT_RESPONSE=$(curl -s -X POST "${BASE_URL}/Part/checkout" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"masterId\": \"${MASTER_ID}\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }")

WORKING_COPY_ID=$(echo "$CHECKOUT_RESPONSE" | jq -r '.data[0].id')
echo "✓ Working Copy ID: ${WORKING_COPY_ID}"

# Step 3: Update（修改工作副本）
echo "=== Update ==="
curl -s -X POST "${BASE_URL}/Part/update" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"id\": \"${WORKING_COPY_ID}\",
      \"partName\": \"演示物料-已更新\",
      \"specification\": \"200x100x60\",
      \"material\": \"铝合金\",
      \"stockQty\": 200,
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, partName: .data[0].partName}'

# Step 4: Checkin（保存为新版本）
echo "=== Checkin ==="
curl -s -X POST "${BASE_URL}/Part/checkin" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"masterId\": \"${MASTER_ID}\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, version: .data[0].version, partName: .data[0].partName}'

echo "🎉 完整流程成功！"
```

保存为 `test-workflow.sh`，然后：
```bash
chmod +x test-workflow.sh
./test-workflow.sh
```

---

## 1. Part（物料）操作

### 1.1 创建 Part

```bash
curl -X POST "${BASE_URL}/Part/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "partNumber": "PART-001",
      "partName": "测试物料",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin",
      "supplierName": {"id": "863454055270195200"},
      "unit": {"id": "863539214308876288"},
      "category": {"id": "863477803427831808"},
      "master": {},
      "branch": {},
      "specification": "100x50x30",
      "material": "不锈钢",
      "stockQty": 100
    }
  }' | jq '{result, part: .data[0] | {id, masterId: .master.id, partNumber, partName, version, workingState: .workingState.code}}'
```

### 1.2 查询 Part（根据 ID）

```bash
PART_ID="your_part_id"

curl -X POST "${BASE_URL}/Part/get" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"id\": \"${PART_ID}\"
    }
  }" | jq '.data[0] | {id, partNumber, partName, version, workingState: .workingState.code, master: .master.id}'
```

### 1.3 查询 Part 列表

```bash
curl -X POST "${BASE_URL}/Part/list?curPage=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "condition": {}
    }
  }' | jq '.data[] | {id, partNumber, partName, version, workingState: .workingState.code}'
```

### 1.4 Checkout（检出）

**用途**：创建工作副本，使 Part 进入可编辑状态（`INWORK`）。

```bash
MASTER_ID="your_master_id"

curl -X POST "${BASE_URL}/Part/checkout" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"masterId\": \"${MASTER_ID}\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, workingCopy: .data[0] | {id, partName, workingState: .workingState.code, workingCopy}}'
```

### 1.5 Update（更新工作副本）

**注意**：必须使用 Working Copy ID，不是 Part ID！

```bash
WORKING_COPY_ID="your_working_copy_id"

curl -X POST "${BASE_URL}/Part/update" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"id\": \"${WORKING_COPY_ID}\",
      \"partName\": \"更新后的名称\",
      \"specification\": \"200x100x60\",
      \"material\": \"铝合金\",
      \"stockQty\": 200,
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, updated: .data[0] | {id, partName, specification, material, stockQty}}'
```

### 1.6 Checkin（检入，保存为新版本）

```bash
MASTER_ID="your_master_id"

curl -X POST "${BASE_URL}/Part/checkin" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"masterId\": \"${MASTER_ID}\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, newVersion: .data[0] | {id, partNumber, partName, version, workingState: .workingState.code, latest}}'
```

### 1.7 Undo Checkout（撤销检出）

```bash
MASTER_ID="your_master_id"

curl -X POST "${BASE_URL}/Part/undoCheckout" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"masterId\": \"${MASTER_ID}\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result}'
```

---

## 2. BusinessPartner（供应商/合作伙伴）

### 2.1 创建 BusinessPartner

```bash
curl -X POST "${BASE_URL}/BusinessPartner/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "partnerCode": "BP-001",
      "partnerName": "测试供应商",
      "partnerType": "Machinery",
      "phone": "021-88888888",
      "email": "test@example.com",
      "website": "http://example.com",
      "addressText": "上海市浦东新区",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin"
    }
  }' | jq '{result, partner: .data[0] | {id, partnerCode, partnerName, partnerType: .partnerType.code}}'
```

### 2.2 查询 BusinessPartner

```bash
curl -X POST "${BASE_URL}/BusinessPartner/list?curPage=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "condition": {}
    }
  }' | jq '.data[] | {id, partnerCode, partnerName, phone, email}'
```

### 2.3 更新 BusinessPartner

```bash
PARTNER_ID="your_partner_id"

curl -X POST "${BASE_URL}/BusinessPartner/update" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d "{
    \"params\": {
      \"id\": \"${PARTNER_ID}\",
      \"partnerName\": \"更新后的供应商名称\",
      \"phone\": \"021-99999999\",
      \"creator\": \"xdmAdmin\",
      \"modifier\": \"xdmAdmin\"
    }
  }" | jq '{result, updated: .data[0] | {id, partnerName, phone}}'
```

---

## 3. Unit（单位）

### 3.1 创建 Unit

```bash
curl -X POST "${BASE_URL}/Unit/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "unitCode": "PCS",
      "unitName": "件",
      "unitNameEn": "Piece",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin"
    }
  }' | jq '{result, unit: .data[0] | {id, unitCode, unitName}}'
```

### 3.2 查询 Unit

```bash
curl -X POST "${BASE_URL}/Unit/list?curPage=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "condition": {}
    }
  }' | jq '.data[] | {id, unitCode, unitName}'
```

---

## 4. Employee（员工）

### 4.1 创建 Employee

```bash
curl -X POST "${BASE_URL}/Employee/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "employeeCode": "EMP-001",
      "employeeName": "张三",
      "email": "zhangsan@example.com",
      "phone": "13800138000",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin"
    }
  }' | jq '{result, employee: .data[0] | {id, employeeCode, employeeName, email}}'
```

### 4.2 查询 Employee

```bash
curl -X POST "${BASE_URL}/Employee/list?curPage=1&pageSize=10" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "condition": {}
    }
  }' | jq '.data[] | {id, employeeCode, employeeName, phone, email}'
```

---

## 5. Department（部门）

### 5.1 创建 Department

```bash
curl -X POST "${BASE_URL}/Department/create" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{
    "params": {
      "departmentCode": "DEPT-001",
      "departmentName": "研发部",
      "creator": "xdmAdmin",
      "modifier": "xdmAdmin"
    }
  }' | jq '{result, department: .data[0] | {id, departmentCode, departmentName}}'
```

---

## 🔍 调试技巧

### 查看完整响应

```bash
curl -X POST "${BASE_URL}/Part/get" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{"params": {"id": "YOUR_ID"}}' | jq '.'
```

### 检查 creator/modifier 字段长度

```bash
curl -s -X POST "${BASE_URL}/Part/get" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{"params": {"id": "YOUR_ID"}}' | jq '{
  creator: .data[0].creator,
  modifier: .data[0].modifier,
  creatorLength: (.data[0].creator | length),
  modifierLength: (.data[0].modifier | length)
}'
```

**预期结果**：`creatorLength` 和 `modifierLength` 都应该是 `8`。

### 查看版本状态

```bash
curl -s -X POST "${BASE_URL}/Part/get" \
  -H "Content-Type: application/json" \
  -H "X-Auth-Token: test" \
  -d '{"params": {"id": "YOUR_ID"}}' | jq '{
  id: .data[0].id,
  version: .data[0].version,
  workingState: .data[0].workingState.code,
  workingCopy: .data[0].workingCopy,
  latest: .data[0].latest
}'
```

---

## 📚 常见问题

### Q1: 为什么必须添加 creator 和 modifier？

**A**: SDK 在版本管理（checkout/update/checkin）时会校验当前操作用户与 `modifier` 字段是否一致。如果不显式指定，SDK 会自动设置，但可能导致字段长度不一致（8字符 vs 9字符带空格），从而导致权限校验失败。

### Q2: 为什么不需要 Authorization 头？

**A**: SDK 已改为通过请求体中的 `creator`/`modifier` 字段识别用户身份，不再使用 HTTP Basic Auth。

### Q3: checkout 后如何找到 Working Copy ID？

**A**: checkout 返回的 `.data[0].id` 就是 Working Copy ID，用于后续的 update 操作。

### Q4: Part 的版本号如何递增？

**A**: 每次 checkin 后，SDK 会自动递增版本号（A → B → C...）。

---

## 📖 相关文档

- **Bug 报告（已解决）**: `SDK_VERSION_MANAGEMENT_BUG.md`
- **开发文档**: `DEVELOPMENT.md`
- **完整测试脚本**: `test-fixed-workflow-v2.sh`

---

**最后更新**: 2026-02-13
