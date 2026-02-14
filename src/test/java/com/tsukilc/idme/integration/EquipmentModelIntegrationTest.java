package com.tsukilc.idme.integration;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.BusinessPartnerCreateDTO;
import com.tsukilc.idme.dto.EquipmentClassficationCreateDTO;
import com.tsukilc.idme.dto.EquipmentModelCreateDTO;
import com.tsukilc.idme.service.BusinessPartnerService;
import com.tsukilc.idme.service.EquipmentClassficationService;
import com.tsukilc.idme.service.EquipmentModelService;
import com.tsukilc.idme.vo.BusinessPartnerVO;
import com.tsukilc.idme.vo.EquipmentClassficationVO;
import com.tsukilc.idme.vo.EquipmentModelVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EquipmentModel（设备机型）模块集成测试
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipmentModelIntegrationTest {

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    private static String testPartnerId;
    private static String testClassficationId;
    private static String testModelId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建测试依赖数据")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建测试依赖数据 ==========");

        // 1. 创建测试往来单位（manufacturer）
        BusinessPartnerCreateDTO partnerDto = new BusinessPartnerCreateDTO();
        partnerDto.setPartnerCode("PARTNER_MODEL_TEST");
        partnerDto.setPartnerName("测试厂家_设备机型");
        partnerDto.setPartnerType("Machinery");

        BusinessPartnerVO partner = businessPartnerService.create(partnerDto);
        testPartnerId = partner.getId();
        log.info("测试往来单位创建成功，ID: {}", testPartnerId);

        // 2. 创建测试设备分类（category）
        EquipmentClassficationCreateDTO classDto = new EquipmentClassficationCreateDTO();
        classDto.setEquipmentClassName("测试设备分类_机型");

        EquipmentClassficationVO classfication = equipmentClassficationService.create(classDto);
        testClassficationId = classfication.getId();
        log.info("测试设备分类创建成功，ID: {}", testClassficationId);

        assertNotNull(testPartnerId);
        assertNotNull(testClassficationId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建设备机型")
    void test02_CreateModel() {
        log.info("========== 测试：创建设备机型 ==========");
        assertNotNull(testPartnerId);

        EquipmentModelCreateDTO dto = new EquipmentModelCreateDTO();
        dto.setModelCode("MODEL_TEST_001");
        dto.setModelName("测试设备机型");
        dto.setManufacturer(testPartnerId);
        dto.setBrand("测试品牌");
        dto.setModelSpec("V1.0");
        dto.setCategory(testClassficationId);

        String modelId = equipmentModelService.create(dto);

        assertNotNull(modelId);
        testModelId = modelId;
        log.info("设备机型创建成功，ID: {}", testModelId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询设备机型")
    void test03_GetById() {
        log.info("========== 测试：查询设备机型 ==========");

        EquipmentModelVO result = equipmentModelService.getById(testModelId);

        assertNotNull(result);
        assertEquals(testModelId, result.getId());
        assertEquals("MODEL_TEST_001", result.getModelCode());
        assertEquals("测试设备机型", result.getModelName());
        assertEquals(testPartnerId, result.getManufacturer());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询设备机型")
    void test04_List() {
        log.info("========== 测试：分页查询设备机型 ==========");

        PageResult<EquipmentModelVO> result = equipmentModelService.list(1, 1000);

        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getRecords().stream().anyMatch(m -> testModelId.equals(m.getId())));

        log.info("查询成功，共 {} 条记录", result.getRecords().size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新设备机型")
    void test05_Update() {
        log.info("========== 测试：更新设备机型 ==========");

        EquipmentModelCreateDTO updateDto = new EquipmentModelCreateDTO();
        updateDto.setModelCode("MODEL_TEST_001_UPDATED");
        updateDto.setModelName("测试设备机型（已更新）");
        updateDto.setManufacturer(testPartnerId);
        updateDto.setBrand("测试品牌V2");
        updateDto.setModelSpec("V2.0");
        updateDto.setCategory(testClassficationId);

        assertDoesNotThrow(() -> equipmentModelService.update(testModelId, updateDto));

        EquipmentModelVO updated = equipmentModelService.getById(testModelId);
        assertEquals("MODEL_TEST_001_UPDATED", updated.getModelCode());
        assertEquals("测试品牌V2", updated.getBrand());

        log.info("更新成功: {}", updated);
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除设备机型")
    void test06_Delete() {
        log.info("========== 测试：删除设备机型 ==========");

        assertDoesNotThrow(() -> equipmentModelService.delete(testModelId));
        log.info("设备机型删除成功");
    }

    @Test
    @Order(7)
    @DisplayName("7. 清理：删除测试依赖数据")
    void test07_Cleanup() {
        log.info("========== 清理：删除测试依赖数据 ==========");

        if (testClassficationId != null) {
            assertDoesNotThrow(() -> equipmentClassficationService.delete(testClassficationId));
            log.info("测试设备分类删除成功");
        }

        if (testPartnerId != null) {
            assertDoesNotThrow(() -> businessPartnerService.delete(testPartnerId));
            log.info("测试往来单位删除成功");
        }
    }
}
