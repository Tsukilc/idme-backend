package com.tsukilc.idme.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.tsukilc.idme.dto.PartnerContactCreateDTO;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * PartnerContact Controller 集成测试
 * Phase 1: 测试 GET /api/partner-contact/{id} 接口
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PartnerContactControllerIntegrationTest extends BaseIntegrationTest {

    private static String testPartnerId;  // 测试用往来单位 ID
    private static String testContactId;  // 测试用联系人 ID

    /**
     * 前置准备：创建测试用往来单位（用于关联联系人）
     */
    @Test
    @Order(1)
    @DisplayName("准备：创建测试用往来单位")
    public void test00_prepareTestPartner() throws Exception {
        String requestBody = """
            {
                "partnerCode": "TEST_PARTNER_001",
                "partnerName": "测试往来单位",
                "partnerType": "Parts"
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
        testPartnerId = jsonNode.get("data").get("id").asText();

        System.out.println("创建测试往来单位成功，ID: " + testPartnerId);
    }

    /**
     * 测试 1：创建联系人（为后续测试准备数据）
     */
    @Test
    @Order(2)
    @DisplayName("创建联系人")
    public void test01_createPartnerContact() throws Exception {
        PartnerContactCreateDTO dto = new PartnerContactCreateDTO();
        dto.setPartner(testPartnerId);
        dto.setContactName("张三");
        dto.setMobile("13800138000");
        dto.setPhone("010-12345678");
        dto.setEmail("zhangsan@example.com");
        dto.setRole("采购经理");
        dto.setRemarks("测试联系人");

        MvcResult result = mockMvc.perform(post("/api/partner-contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").exists())
            .andExpect(jsonPath("$.data.contactName").exists())
            // .andExpect(jsonPath("$.data.mobile").value("13800138000"))  // SDK 不支持 mobile 字段
            .andExpect(jsonPath("$.data.phone").value("010-12345678"))
            .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        testContactId = jsonNode.get("data").get("id").asText();

        System.out.println("创建联系人成功，ID: " + testContactId);
    }

    /**
     * 测试 2：按 ID 查询联系人详情（Phase 1 新增接口）
     */
    @Test
    @Order(3)
    @DisplayName("Phase 1: GET /api/partner-contact/{id} - 查询联系人详情")
    public void test02_getPartnerContactById() throws Exception {
        mockMvc.perform(get("/api/partner-contact/{id}", testContactId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data.id").value(testContactId))
            .andExpect(jsonPath("$.data.contactName").exists())
            // .andExpect(jsonPath("$.data.mobile").value("13800138000"))  // SDK 不支持
            .andExpect(jsonPath("$.data.phone").value("010-12345678"))
            .andExpect(jsonPath("$.data.email").value("zhangsan@example.com"))
            .andExpect(jsonPath("$.data.role").exists())
            .andExpect(jsonPath("$.data.remarks").exists());

        System.out.println("✅ 按 ID 查询联系人详情测试通过");
    }

    /**
     * 测试 3：按 ID 查询不存在的联系人（异常场景）
     */
    @Test
    @Order(4)
    @DisplayName("Phase 1: GET /api/partner-contact/{id} - 查询不存在的联系人")
    public void test03_getPartnerContactById_NotFound() throws Exception {
        String nonExistentId = "NON_EXISTENT_ID";

        mockMvc.perform(get("/api/partner-contact/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())  // API 统一返回 200，通过 result 字段区分成功/失败
            .andExpect(jsonPath("$.result").value("FAIL"))
            .andExpect(jsonPath("$.errors").isArray());

        System.out.println("✅ 查询不存在联系人异常处理测试通过");
    }

    /**
     * 测试 4：按往来单位查询联系人列表
     */
    @Test
    @Order(5)
    @DisplayName("GET /api/partner-contact/by-partner/{partnerId} - 按往来单位查询")
    public void test04_getPartnerContactsByPartner() throws Exception {
        mockMvc.perform(get("/api/partner-contact/by-partner/{partnerId}", testPartnerId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))))
            .andExpect(jsonPath("$.data[?(@.id == '" + testContactId + "')].contactName").exists());

        System.out.println("✅ 按往来单位查询联系人列表测试通过");
    }

    /**
     * 后置清理：删除测试数据
     */
    @Test
    @Order(99)
    @DisplayName("清理：删除测试数据")
    public void test99_cleanup() throws Exception {
        // 删除联系人
        if (testContactId != null) {
            mockMvc.perform(delete("/api/partner-contact/{id}", testContactId))
                .andExpect(status().isOk());
            System.out.println("删除测试联系人成功，ID: " + testContactId);
        }

        // 删除往来单位
        if (testPartnerId != null) {
            mockMvc.perform(delete("/api/business-partner/{id}", testPartnerId))
                .andExpect(status().isOk());
            System.out.println("删除测试往来单位成功，ID: " + testPartnerId);
        }
    }
}
