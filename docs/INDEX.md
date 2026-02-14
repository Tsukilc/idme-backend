# iDME 项目文档导航

> 快速找到你需要的文档 📚

---

## 🚀 新人入门（按顺序阅读）

1. **README.md** - 项目概述和快速开始
2. **DEVELOPMENT.md** - 开发环境搭建和架构设计
3. **PROJECT_KNOWLEDGE.md** - 关键决策和技术难题（必读！）
4. **CLAUDE.md** - 编码规范和SDK限制规则库

**预计时间**：60分钟

---

## 📖 按角色分类

### 👨‍💻 开发者
- **架构设计**：DEVELOPMENT.md
- **编码规范**：CLAUDE.md、agents.md
- **API文档**：docs/openapi.yaml、API_TEST.md
- **知识库**：PROJECT_KNOWLEDGE.md
- **模块进度**：MODULE_VALIDATION_REPORT.md

### 🧪 测试人员
- **测试指南**：API_TEST.md
- **集成测试**：src/test/java/com/tsukilc/idme/integration/
- **已知问题**：docs/KNOWN_ISSUES.md
- **测试数据**：docs/openapi-examples-ids.md

### 📝 技术写作
- **API规范**：docs/openapi.yaml
- **示例数据**：docs/openapi-examples-ids.md
- **OpenAPI改进**：docs/OPENAPI_IMPROVEMENTS_SUMMARY.md

---

## 📚 按任务分类

### 🔨 开发新模块
1. 查看模块依赖：MODULE_VALIDATION_REPORT.md
2. 参考已完成模块：src/test/java/.../UnitIntegrationTest.java
3. 查阅SDK规则：CLAUDE.md
4. 编写集成测试：参考测试结构
5. 更新文档：MODULE_VALIDATION_REPORT.md、CLAUDE.md

### 🐛 排查SDK问题
1. 检查规则库：CLAUDE.md
2. 查看问题库：docs/KNOWN_ISSUES.md
3. 参考问题报告：docs/SDK_VERSION_MANAGEMENT_BUG.md
4. 使用CURL测试：docs/SDK_CURL_COMMANDS.md
5. 记录新问题：按SDK_VERSION_MANAGEMENT_BUG.md格式

### 📝 修改OpenAPI
1. 阅读改进总结：docs/OPENAPI_IMPROVEMENTS_SUMMARY.md
2. 查找固定ID：docs/openapi-examples-ids.md
3. 参考现有示例：docs/openapi.yaml已有example
4. 验证YAML语法
5. 运行集成测试：OpenApiExamplesDataTest

### 🧪 编写集成测试
1. 参考测试结构：DepartmentIntegrationTest.java
2. 准备依赖数据：按Level 0-4顺序
3. 使用@Order注解：保证执行顺序
4. 验证结果：CREATE → GET → LIST → UPDATE → DELETE
5. 更新报告：MODULE_VALIDATION_REPORT.md

---

## 📋 按文档类型分类

### 核心规范文档
| 文档 | 用途 | 优先级 |
|------|------|--------|
| **CLAUDE.md** | 编码规范与SDK规则库 | ⭐⭐⭐⭐⭐ |
| **PROJECT_KNOWLEDGE.md** | 决策记录与技术难题 | ⭐⭐⭐⭐⭐ |
| **DEVELOPMENT.md** | 架构设计与开发指南 | ⭐⭐⭐⭐ |
| docs/openapi.yaml | API接口规范（权威） | ⭐⭐⭐⭐⭐ |

### SDK相关文档
| 文档 | 用途 | 优先级 |
|------|------|--------|
| docs/SDK_API_COMPARISON.md | query/list接口对比（必读） | ⭐⭐⭐⭐ |
| docs/SDK_CALLING_GUIDE.md | SDK调用详细指南 | ⭐⭐⭐ |
| docs/SDK_CURL_COMMANDS.md | CURL命令示例 | ⭐⭐⭐ |
| sdk_samples.md | SDK代码示例汇总 | ⭐⭐⭐ |
| transfer.md | SDK交接文档（引用对象格式） | ⭐⭐⭐ |

### 测试与验证
| 文档 | 用途 | 优先级 |
|------|------|--------|
| MODULE_VALIDATION_REPORT.md | 14/18模块验证报告 | ⭐⭐⭐⭐ |
| API_TEST.md | 完整API测试指南 | ⭐⭐⭐⭐ |
| docs/TEST_FIXES.md | Phase 1测试修复方案 | ⭐⭐⭐ |
| docs/openapi-examples-ids.md | OpenAPI固定ID清单 | ⭐⭐⭐⭐ |

### 问题报告与解决方案
| 文档 | 用途 | 优先级 |
|------|------|--------|
| docs/SDK_VERSION_MANAGEMENT_BUG.md | 版本对象Bug（已解决，范例） | ⭐⭐⭐⭐ |
| docs/KNOWN_ISSUES.md | 已知问题库索引 | ⭐⭐⭐ |

### 计划与待办
| 文档 | 用途 | 状态 |
|------|------|------|
| TODO.md | 高/中/低优先级待办 | ⚠️ 需更新 |
| docs/REMAINING_API_PLAN.md | Phase 1/2/3接口计划 | ⚠️ 需更新 |

---

## 🔍 快速查找

### 我想知道...

**"如何调用SDK查询接口？"**
→ docs/SDK_API_COMPARISON.md（优先list接口）

**"SDK枚举字段怎么处理？"**
→ CLAUDE.md规则库 → MODULE_VALIDATION_REPORT.md技术发现

**"版本对象的checkout/checkin流程？"**
→ docs/SDK_VERSION_MANAGEMENT_BUG.md

**"OpenAPI示例值怎么写？"**
→ docs/OPENAPI_IMPROVEMENTS_SUMMARY.md → docs/openapi-examples-ids.md

**"哪些模块已经完成？"**
→ MODULE_VALIDATION_REPORT.md（14/18）

**"遇到SDK报错怎么办？"**
→ CLAUDE.md规则库 → docs/KNOWN_ISSUES.md → PROJECT_KNOWLEDGE.md FAQ

**"DTO和VO有什么区别？"**
→ DEVELOPMENT.md架构设计 → PROJECT_KNOWLEDGE.md决策2

**"集成测试怎么写？"**
→ src/test/.../DepartmentIntegrationTest.java（参考范例）

---

## 📝 文档更新记录

| 时间 | 更新内容 | 负责人 |
|------|---------|--------|
| 2026-02-14 | 创建文档导航首页 | 开发团队 |
| 2026-02-14 | 创建PROJECT_KNOWLEDGE.md | 开发团队 |
| 2026-02-14 | 补充OpenAPI改进文档 | 开发团队 |

---

## 💡 提示

- 📌 **优先级5星**的文档是核心必读
- 🔄 定期更新MODULE_VALIDATION_REPORT.md和CLAUDE.md
- 📝 新问题参考SDK_VERSION_MANAGEMENT_BUG.md格式记录
- 🧹 定期清理过时的TODO.md和计划文档
