# 交接文档：SDK 与开发注意事项（供后续完成剩余接口使用）

本文档总结开发过程中遇到的注意点、用户提示以及 SDK 使用约定，便于 Claude Code 或后续开发者完成剩余接口时少踩坑。

---

## 1. SDK 引用对象（Reference）传参格式

### 1.1 正确格式（SDK 期望）

调用 iDME SDK 创建/更新实体时，**引用类型字段**必须使用以下结构，不能只传 ID 字符串：

```json
{
  "id": "目标实体的ID",
  "clazz": "目标实体类名（如 BusinessPartner、Equipment、Location）",
  "name": ""
}
```

- **id**：被引用实体的主键 ID（必填）。
- **clazz**：SDK 实体类名，必须与数据模型中的类名一致（必填）。常见值：`BusinessPartner`、`EquipmentClassfication`、`Equipment`、`Location`、`WorkingProcedure`、`Employee`、`Part`、`WorkingPlan`、`Tenant` 等。
- **name**：展示名，可为空字符串 `""`。

### 1.2 用户提供的 curl 示例（可直接参考）

**EquipmentClassfication 创建**（无引用时）：

```bash
# params 里 needSetNullAttrs 可忽略
{"params":{"rdmExtensionType":"EquipmentClassfication","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"}, ...}}
```

**EquipmentModel 创建**（含 manufacturer、category 引用）：

```json
"manufacturer": {"id":"1","clazz":"BusinessPartner","name":""},
"category": {"id":"863851758470828032","clazz":"EquipmentClassfication","name":""}
```

**Employee 创建**（含 dept 引用）：

```json
"dept": {"id":"863870688249782272","clazz":"Department","name":""}
```

### 1.3 当前项目中的序列化方式与待对齐点

- 项目内使用 **ObjectReference** 表示引用，并有 **ObjectReferenceSerializer** 序列化后发给 SDK。
- 若当前序列化结果为 `{"id":"xxx","displayName":"yyy"}`，而 SDK 要求 `id` + `clazz` + `name`，则需修改 [ObjectReferenceSerializer](src/main/java/com/tsukilc/idme/entity/ObjectReferenceSerializer.java)，使输出包含 **clazz**（可由 ObjectReference 的 className 或构造时传入的类名提供），**name** 可为空字符串。
- 前端/API 入参可以只传 **ID 字符串**；在 Service 层或序列化前将 ID 转成 `{ id, clazz, name }` 再交给 SDK。

---

## 2. 必填字段：OpenAPI 与 SDK 不一致时的处理

- **以 SDK 实际校验为准**。OpenAPI 中标记为可选的字段，若 SDK 报错 “can not be null” 或 “does not meet the system requirements”，则应在本项目中**改为必填**（DTO 上加 `@NotBlank`/`@NotNull`，并在 API 文档中说明）。
- 已按此规则调整的模块示例：
    - **EquipmentModel**：`manufacturer`、`category` 已标为必填（SDK 必填）。
    - **Equipment**：`supplierName` 已标为必填（SDK 必填）。
    - **WorkingProcedure**：`mainProductionEquipment` 已标为必填（SDK 必填）。

---

## 3. 枚举字段必须用 SDK 规定值

- **不要传中文**。SDK 枚举一般为英文或固定编码。
- **Equipment.status** 示例：  
  有效值为 `InOperation`、`Standby`、`UnderMaintenance`、`Shutdown`、`Scrap`（对应运行/待机/维修/停机/报废）。传 “运行” 会报错。
- 其他实体若遇 “not one of the values accepted for Enum” 类错误，需查 [docs/sdk_entities.yaml](docs/sdk_entities.yaml) 或 SDK 文档中的枚举定义，在 API 层做“中文/展示值 → SDK 枚举值”的映射，或直接在接口文档中写明只接受英文枚举值。

---

## 4. 特殊字段与 SDK 限制

- **defaultTechParams（EquipmentModel） / techParams（Equipment）**  
  SDK 对 JSON 结构可能有约束（如类型、层级）。若简单 key-value 报错 “does not meet the system requirements”，可先不传或传 null，待确认 SDK 要求的格式后再支持。
- **category**  
  多为“分类”类型，在部分实体中对应 **EquipmentClassfication** 等。创建前需先有分类数据；引用格式同样为 `{ id, clazz, name }`，clazz 如 `EquipmentClassfication`。
- **needSetNullAttrs**  
  用户提供的 curl 中出现的 `needSetNullAttrs` 可忽略，不要求在业务代码中维护。

---

## 5. 数据依赖与创建顺序

- **BusinessPartner**：Equipment 的 supplierName、EquipmentModel 的 manufacturer 等依赖它，需先创建。
- **EquipmentClassfication**：EquipmentModel 的 category 依赖它，需先创建（或通过 Web UI 创建）。
- **Equipment**：WorkingProcedure 的 mainProductionEquipment 依赖它。
- **Department**：Employee 的 dept 依赖它；用户已确认部门可创建（含根部门）。
- 实现“剩余接口”时，若某接口依赖上述主数据，在 API_TEST.md 或 REMAINING_API_PLAN 中注明依赖关系及建议测试数据准备顺序。

---

## 6. 用户提示过的要点（需遵守）

- **引用对象**：按 “id + clazz + name” 格式传参（见第 1 节）。
- **编码规范**：禁止全类名引入，必须用 import + 类名（见 [AGENTS.md](AGENTS.md)）。
- **id 不手填**：创建时 id 由 SDK 生成，请求体里不要传 i