package com.tsukilc.idme.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 调试测试 - 查看详细的错误信息
 */
public class DebugTest extends BaseIntegrationTest {

    @Test
    public void testCreateBusinessPartner() throws Exception {
        // 先创建往来单位
        String partnerBody = """
            {
                "partnerCode": "DEBUG_PARTNER",
                "partnerName": "调试往来单位",
                "partnerType": "Parts"
            }
            """;

        MvcResult partnerResult = mockMvc.perform(post("/api/business-partner")
                .contentType(MediaType.APPLICATION_JSON)
                .content(partnerBody))
            .andReturn();

        System.out.println("========== 创建往来单位响应 ==========");
        System.out.println(partnerResult.getResponse().getContentAsString());

        String partnerId = objectMapper.readTree(partnerResult.getResponse().getContentAsString())
            .get("data").get("id").asText();

        // 创建联系人
        String contactBody = String.format("""
            {
                "partner": "%s",
                "contactName": "调试联系人",
                "mobile": "13800138000",
                "phone": "010-12345678",
                "email": "debug@example.com",
                "role": "经理",
                "remarks": "测试"
            }
            """, partnerId);

        MvcResult contactResult = mockMvc.perform(post("/api/partner-contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contactBody))
            .andReturn();

        System.out.println("========== 创建联系人响应状态码 ==========");
        System.out.println(contactResult.getResponse().getStatus());

        System.out.println("========== 创建联系人响应体 ==========");
        System.out.println(contactResult.getResponse().getContentAsString());

        if (contactResult.getResponse().getStatus() == 200) {
            String contactId = objectMapper.readTree(contactResult.getResponse().getContentAsString())
                .get("data").get("id").asText();

            // 查询联系人
            MvcResult getResult = mockMvc.perform(get("/api/partner-contact/{id}", contactId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

            System.out.println("========== 查询联系人响应 ==========");
            System.out.println(getResult.getResponse().getContentAsString());
        }
    }
}
