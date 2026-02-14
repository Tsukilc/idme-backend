package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.BusinessPartnerCreateDTO;
import com.tsukilc.idme.dto.PartnerContactCreateDTO;
import com.tsukilc.idme.service.BusinessPartnerService;
import com.tsukilc.idme.service.PartnerContactService;
import com.tsukilc.idme.vo.BusinessPartnerVO;
import com.tsukilc.idme.vo.PartnerContactVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PartnerContact（联系人）模块集成测试
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PartnerContactIntegrationTest {

    @Autowired
    private PartnerContactService partnerContactService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    private static String testPartnerId;
    private static String testContactId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建测试往来单位")
    void test01_PreparePartner() {
        log.info("========== 准备：创建测试往来单位 ==========");

        BusinessPartnerCreateDTO dto = new BusinessPartnerCreateDTO();
        dto.setPartnerCode("PARTNER_CONTACT_TEST");
        dto.setPartnerName("测试往来单位_联系人");
        dto.setPartnerType("Machinery");

        BusinessPartnerVO result = businessPartnerService.create(dto);
        testPartnerId = result.getId();

        log.info("测试往来单位创建成功，ID: {}", testPartnerId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建联系人（不含mobile字段）")
    void test02_CreateContact() {
        log.info("========== 测试：创建联系人 ==========");
        assertNotNull(testPartnerId);

        PartnerContactCreateDTO dto = new PartnerContactCreateDTO();
        dto.setPartner(testPartnerId);
        dto.setContactName("张三");
        dto.setPhone("010-12345678");  // 使用phone而非mobile
        dto.setEmail("zhangsan@example.com");
        dto.setRole("销售");

        PartnerContactVO result = partnerContactService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("张三", result.getContactName());

        testContactId = result.getId();
        log.info("联系人创建成功，ID: {}", testContactId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询联系人")
    void test03_GetById() {
        log.info("========== 测试：查询联系人 ==========");

        PartnerContactVO result = partnerContactService.getById(testContactId);

        assertNotNull(result);
        assertEquals("张三", result.getContactName());
        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询联系人")
    void test04_List() {
        log.info("========== 测试：分页查询联系人 ==========");

        List<PartnerContactVO> list = partnerContactService.list(1, 1000);

        assertNotNull(list);
        assertTrue(list.stream().anyMatch(contact -> testContactId.equals(contact.getId())));

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新联系人")
    void test05_Update() {
        log.info("========== 测试：更新联系人 ==========");

        PartnerContactCreateDTO updateDto = new PartnerContactCreateDTO();
        updateDto.setPartner(testPartnerId);
        updateDto.setContactName("张三（已更新）");
        updateDto.setPhone("010-87654321");
        updateDto.setEmail("zhangsan_new@example.com");
        updateDto.setRole("技术支持");

        PartnerContactVO result = partnerContactService.update(testContactId, updateDto);

        assertEquals("张三（已更新）", result.getContactName());
        assertEquals("技术支持", result.getRole());

        log.info("更新成功: {}", result);
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除联系人")
    void test06_Delete() {
        log.info("========== 测试：删除联系人 ==========");

        assertDoesNotThrow(() -> partnerContactService.delete(testContactId));
        log.info("联系人删除成功");
    }

    @Test
    @Order(7)
    @DisplayName("7. 清理：删除测试往来单位")
    void test07_Cleanup() {
        log.info("========== 清理：删除测试往来单位 ==========");

        if (testPartnerId != null) {
            assertDoesNotThrow(() -> businessPartnerService.delete(testPartnerId));
            log.info("测试往来单位删除成功");
        }
    }
}
