package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.EquipmentClassficationCreateDTO;
import com.tsukilc.idme.service.EquipmentClassficationService;
import com.tsukilc.idme.vo.EquipmentClassficationVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EquipmentClassfication（设备分类）模块集成测试
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipmentClassficationIntegrationTest {

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    private static String testClassficationId;

    @Test
    @Order(1)
    @DisplayName("1. 创建设备分类")
    void test01_Create() {
        log.info("========== 测试：创建设备分类 ==========");

        EquipmentClassficationCreateDTO dto = new EquipmentClassficationCreateDTO();
        dto.setEquipmentClassName("测试设备分类");

        EquipmentClassficationVO result = equipmentClassficationService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("测试设备分类", result.getEquipmentClassName());

        testClassficationId = result.getId();
        log.info("设备分类创建成功，ID: {}", testClassficationId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 查询设备分类")
    void test02_GetById() {
        log.info("========== 测试：查询设备分类 ==========");

        EquipmentClassficationVO result = equipmentClassficationService.getById(testClassficationId);

        assertNotNull(result);
        assertEquals(testClassficationId, result.getId());
        assertEquals("测试设备分类", result.getEquipmentClassName());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(3)
    @DisplayName("3. 分页查询设备分类")
    void test03_List() {
        log.info("========== 测试：分页查询设备分类 ==========");

        List<EquipmentClassficationVO> list = equipmentClassficationService.list(1, 1000);

        assertNotNull(list);
        assertTrue(list.stream().anyMatch(c -> testClassficationId.equals(c.getId())));

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. 更新设备分类")
    void test04_Update() {
        log.info("========== 测试：更新设备分类 ==========");

        EquipmentClassficationCreateDTO updateDto = new EquipmentClassficationCreateDTO();
        updateDto.setEquipmentClassName("测试设备分类（已更新）");

        EquipmentClassficationVO result = equipmentClassficationService.update(testClassficationId, updateDto);

        assertEquals("测试设备分类（已更新）", result.getEquipmentClassName());

        log.info("更新成功: {}", result);
    }

    @Test
    @Order(5)
    @DisplayName("5. 删除设备分类")
    void test05_Delete() {
        log.info("========== 测试：删除设备分类 ==========");

        assertDoesNotThrow(() -> equipmentClassficationService.delete(testClassficationId));
        log.info("设备分类删除成功");
    }
}
