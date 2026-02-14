package com.tsukilc.idme.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * BusinessPartner Controller 集成测试
 * Phase 1: 测试路径对齐和按类型查询接口
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BusinessPartnerControllerIntegrationTest extends BaseIntegrationTest {

    private static String manufacturerId;  // 生产厂家 ID
    private static String supplierId;      // 供应商 ID
    private static String customerId;      // 客户 ID

    /**
     * 测试 1：创建机械类往来单位
     */
    @Test
    @Order(1)
    @DisplayName("创建机械类往来单位")
    public void test01_createManufacturer() throws Exception {
        String requestBody = """
            {
                "partnerCode": "MFG_001",
                "partnerName": "测试机械厂家",
                "partnerType": "Machinery",
                "phone": "010-88888888",
                "email": "mfg@example.com",
                "addressText": "北京市海淀区"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/business-partner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.partnerName").value("测试机械厂家"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        manufacturerId = jsonNode.get("data").get("id").asText();

        System.out.println("创建生产厂家成功，ID: " + manufacturerId);
    }

    /**
     * 测试 2：创建半导体类往来单位
     */
    @Test
    @Order(2)
    @DisplayName("创建半导体类往来单位")
    public void test02_createSupplier() throws Exception {
        String requestBody = """
            {
                "partnerCode": "SUP_001",
                "partnerName": "测试半导体供应商",
                "partnerType": "Semiconductor",
                "phone": "021-66666666",
                "email": "supplier@example.com"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/business-partner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        supplierId = jsonNode.get("data").get("id").asText();

        System.out.println("创建供应商成功，ID: " + supplierId);
    }

    /**
     * 测试 3：创建航空航天类往来单位
     */
    @Test
    @Order(3)
    @DisplayName("创建航空航天类往来单位")
    public void test03_createCustomer() throws Exception {
        String requestBody = """
            {
                "partnerCode": "CUS_001",
                "partnerName": "测试航空航天客户",
                "partnerType": "Aerospace",
                "phone": "0755-99999999",
                "email": "customer@example.com"
            }
            """;

        MvcResult result = mockMvc.perform(post("/api/business-partner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        customerId = jsonNode.get("data").get("id").asText();

        System.out.println("创建客户成功，ID: " + customerId);
    }

    /**
     * 测试 4：Phase 1 - 验证路径对齐（新路径：/api/business-partner）
     */
    @Test
    @Order(4)
    @DisplayName("Phase 1: 路径对齐 - GET /api/business-partner/{id}")
    public void test04_verifyNewPath() throws Exception {
        mockMvc.perform(get("/api/business-partner/{id}", manufacturerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").value(manufacturerId))
            .andExpect(jsonPath("$.data.partnerName").value("测试机械厂家"));

        System.out.println("✅ 新路径 /api/business-partner 测试通过");
    }

    /**
     * 测试 5：Phase 1 - 按类型查询往来单位（Machinery）
     */
    @Test
    @Order(5)
    @DisplayName("Phase 1: GET /api/business-partner/by-type/{partnerType} - 查询 Machinery")
    public void test05_getByType_Manufacturer() throws Exception {
        mockMvc.perform(get("/api/business-partner/by-type/{partnerType}", "Machinery")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.data[?(@.id == '" + manufacturerId + "')].partnerName").value("测试机械厂家"));

        System.out.println("✅ 按类型查询 Machinery 测试通过");
    }

    /**
     * 测试 6：Phase 1 - 按类型查询往来单位（Semiconductor）
     */
    @Test
    @Order(6)
    @DisplayName("Phase 1: GET /api/business-partner/by-type/{partnerType} - 查询 Semiconductor")
    public void test06_getByType_Supplier() throws Exception {
        mockMvc.perform(get("/api/business-partner/by-type/{partnerType}", "Semiconductor")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.data[?(@.id == '" + supplierId + "')].partnerName").value("测试半导体供应商"));

        System.out.println("✅ 按类型查询 Semiconductor 测试通过");
    }

    /**
     * 测试 7：Phase 1 - 按类型查询往来单位（Aerospace）
     */
    @Test
    @Order(7)
    @DisplayName("Phase 1: GET /api/business-partner/by-type/{partnerType} - 查询 Aerospace")
    public void test07_getByType_Customer() throws Exception {
        mockMvc.perform(get("/api/business-partner/by-type/{partnerType}", "Aerospace")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.data[?(@.id == '" + customerId + "')].partnerName").value("测试航空航天客户"));

        System.out.println("✅ 按类型查询 Aerospace 测试通过");
    }

    /**
     * 测试 8：按类型查询空结果（查询 Chemical）
     */
    @Test
    @Order(8)
    @DisplayName("Phase 1: GET /api/business-partner/by-type/{partnerType} - 查询 Chemical（空结果）")
    public void test08_getByType_Empty() throws Exception {
        mockMvc.perform(get("/api/business-partner/by-type/{partnerType}", "Chemical")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray());

        System.out.println("✅ 按类型查询空结果测试通过");
    }

    /**
     * 测试 9：分页查询往来单位列表
     */
    @Test
    @Order(9)
    @DisplayName("GET /api/business-partner - 分页查询列表")
    public void test09_listBusinessPartners() throws Exception {
        mockMvc.perform(get("/api/business-partner")
                .param("pageNum", "1")
                .param("pageSize", "20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.records").isArray())
            .andExpect(jsonPath("$.data.pageNum").value(1))
            .andExpect(jsonPath("$.data.pageSize").value(20));

        System.out.println("✅ 分页查询列表测试通过");
    }

    /**
     * 后置清理：删除测试数据
     */
    @Test
    @Order(99)
    @DisplayName("清理：删除测试数据")
    public void test99_cleanup() throws Exception {
        // 删除生产厂家
        if (manufacturerId != null) {
            mockMvc.perform(delete("/api/business-partner/{id}", manufacturerId))
                .andExpect(status().isOk());
            System.out.println("删除测试生产厂家成功，ID: " + manufacturerId);
        }

        // 删除供应商
        if (supplierId != null) {
            mockMvc.perform(delete("/api/business-partner/{id}", supplierId))
                .andExpect(status().isOk());
            System.out.println("删除测试供应商成功，ID: " + supplierId);
        }

        // 删除客户
        if (customerId != null) {
            mockMvc.perform(delete("/api/business-partner/{id}", customerId))
                .andExpect(status().isOk());
            System.out.println("删除测试客户成功，ID: " + customerId);
        }
    }
}
