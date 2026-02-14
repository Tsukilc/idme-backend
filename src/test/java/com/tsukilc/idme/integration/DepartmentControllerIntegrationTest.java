package com.tsukilc.idme.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Department Controller 集成测试
 * Phase 1: 测试部门树查询接口
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentControllerIntegrationTest extends BaseIntegrationTest {

    private static String rootDeptId;      // 根部门 ID
    private static String subDept1Id;      // 子部门1 ID
    private static String subDept2Id;      // 子部门2 ID
    private static String subSubDeptId;    // 孙部门 ID

    /**
     * 测试 1：创建根部门
     */
    @Test
    @Order(1)
    @DisplayName("创建根部门")
    public void test01_createRootDepartment() throws Exception {
        String requestBody = """
            {
                "deptCode": "ROOT_DEPT",
                "deptName": "测试总部",
                "manager": "总经理",
                "remarks": "测试根部门"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.deptName").value("测试总部"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        rootDeptId = jsonNode.get("data").get("id").asText();

        System.out.println("创建根部门成功，ID: " + rootDeptId);
    }

    /**
     * 测试 2：创建子部门1（研发部）
     */
    @Test
    @Order(2)
    @DisplayName("创建子部门1 - 研发部")
    public void test02_createSubDept1() throws Exception {
        String requestBody = String.format("""
            {
                "deptCode": "RD_DEPT",
                "deptName": "研发部",
                "parentDept": "%s",
                "manager": "研发经理",
                "remarks": "测试子部门1"
            }
            """, rootDeptId);

        MvcResult result = mockMvc.perform(post("/api/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        subDept1Id = jsonNode.get("data").get("id").asText();

        System.out.println("创建子部门1成功，ID: " + subDept1Id);
    }

    /**
     * 测试 3：创建子部门2（市场部）
     */
    @Test
    @Order(3)
    @DisplayName("创建子部门2 - 市场部")
    public void test03_createSubDept2() throws Exception {
        String requestBody = String.format("""
            {
                "deptCode": "MARKET_DEPT",
                "deptName": "市场部",
                "parentDept": "%s",
                "manager": "市场经理"
            }
            """, rootDeptId);

        MvcResult result = mockMvc.perform(post("/api/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        subDept2Id = jsonNode.get("data").get("id").asText();

        System.out.println("创建子部门2成功，ID: " + subDept2Id);
    }

    /**
     * 测试 4：创建孙部门（软件开发组，研发部下属）
     */
    @Test
    @Order(4)
    @DisplayName("创建孙部门 - 软件开发组")
    public void test04_createSubSubDept() throws Exception {
        String requestBody = String.format("""
            {
                "deptCode": "SW_DEV_TEAM",
                "deptName": "软件开发组",
                "parentDept": "%s",
                "manager": "组长"
            }
            """, subDept1Id);

        MvcResult result = mockMvc.perform(post("/api/department")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        subSubDeptId = jsonNode.get("data").get("id").asText();

        System.out.println("创建孙部门成功，ID: " + subSubDeptId);
    }

    /**
     * 测试 5：Phase 1 - 查询部门树
     */
    @Test
    @Order(5)
    @DisplayName("Phase 1: GET /api/department/tree - 查询部门树")
    public void test05_getDepartmentTree() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/department/tree")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("部门树结构：");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            objectMapper.readTree(responseBody).get("data")
        ));

        System.out.println("✅ 查询部门树测试通过");
    }

    /**
     * 测试 6：验证部门树结构
     */
    @Test
    @Order(6)
    @DisplayName("Phase 1: 验证部门树层级结构")
    public void test06_verifyTreeStructure() throws Exception {
        // 使用 JSONPath 直接验证，避免中文编码问题
        MvcResult result = mockMvc.perform(get("/api/department/tree")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            // 验证根部门存在
            .andExpect(jsonPath("$.data[?(@.id == '" + rootDeptId + "')]").exists())
            .andExpect(jsonPath("$.data[?(@.id == '" + rootDeptId + "')].deptCode").value("ROOT_DEPT"))
            .andExpect(jsonPath("$.data[?(@.id == '" + rootDeptId + "')].children").isArray())
            .andReturn();

        // 验证子部门数量（通过解析 JSON）
        String responseBody = result.getResponse().getContentAsString();
        JsonNode tree = objectMapper.readTree(responseBody).get("data");
        for (JsonNode node : tree) {
            if (node.get("id").asText().equals(rootDeptId)) {
                JsonNode children = node.get("children");
                Assertions.assertNotNull(children);
                Assertions.assertTrue(children.size() >= 2, "根部门应该至少有2个子部门");
            }
        }

        System.out.println("✅ 部门树层级结构验证通过");
    }

    /**
     * 测试 7：查询部门详情
     */
    @Test
    @Order(7)
    @DisplayName("GET /api/department/{id} - 查询部门详情")
    public void test07_getDepartmentById() throws Exception {
        mockMvc.perform(get("/api/department/{id}", rootDeptId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").value(rootDeptId))
            .andExpect(jsonPath("$.data.deptName").value("测试总部"))
            .andExpect(jsonPath("$.data.deptCode").value("ROOT_DEPT"));

        System.out.println("✅ 查询部门详情测试通过");
    }

    /**
     * 后置清理：删除测试数据
     */
    @Test
    @Order(99)
    @DisplayName("清理：删除测试数据")
    public void test99_cleanup() throws Exception {
        // 从最底层开始删除（孙部门 -> 子部门 -> 根部门）
        if (subSubDeptId != null) {
            mockMvc.perform(delete("/api/department/{id}", subSubDeptId))
                .andExpect(status().isOk());
            System.out.println("删除孙部门成功，ID: " + subSubDeptId);
        }

        if (subDept1Id != null) {
            mockMvc.perform(delete("/api/department/{id}", subDept1Id))
                .andExpect(status().isOk());
            System.out.println("删除子部门1成功，ID: " + subDept1Id);
        }

        if (subDept2Id != null) {
            mockMvc.perform(delete("/api/department/{id}", subDept2Id))
                .andExpect(status().isOk());
            System.out.println("删除子部门2成功，ID: " + subDept2Id);
        }

        if (rootDeptId != null) {
            mockMvc.perform(delete("/api/department/{id}", rootDeptId))
                .andExpect(status().isOk());
            System.out.println("删除根部门成功，ID: " + rootDeptId);
        }
    }
}
