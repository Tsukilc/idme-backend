package com.tsukilc.idme.integration;

import com.tsukilc.idme.dao.UnitDao;
import com.tsukilc.idme.dto.UnitCreateDTO;
import com.tsukilc.idme.entity.Unit;
import com.tsukilc.idme.service.UnitService;
import com.tsukilc.idme.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit（计量单位）模块集成测试
 * 测试覆盖：CREATE → GET → LIST → UPDATE → DELETE
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitIntegrationTest {

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitDao unitDao;

    private static String testUnitId;

    @Test
    @Order(1)
    @DisplayName("1. 创建计量单位")
    void test01_Create() {
        log.info("========== 测试：创建计量单位 ==========");

        // 1. 构造DTO
        UnitCreateDTO dto = new UnitCreateDTO();
        dto.setUnitName("测试单位_件");
        dto.setUnitDisplayName("piece");
        dto.setUnitCategory("数量");
        dto.setUnitFactor("1.0");
        dto.setMesurementSystem("公制");

        // 2. 调用service.create()
        UnitVO result = unitService.create(dto);

        // 3. 验证返回值
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getId(), "ID应自动生成");
        assertEquals("测试单位_件", result.getUnitName(), "unitName应一致");
        assertEquals("piece", result.getUnitDisplayName(), "unitDisplayName应一致");
        assertEquals("数量", result.getUnitCategory(), "unitCategory应一致");
        assertEquals("1.0", result.getUnitFactor(), "unitFactor应一致");
        assertEquals("公制", result.getMesurementSystem(), "mesurementSystem应一致");

        // 4. 保存testId供后续测试使用
        testUnitId = result.getId();
        log.info("创建成功，ID: {}", testUnitId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 根据ID查询计量单位")
    void test02_GetById() {
        log.info("========== 测试：根据ID查询计量单位 ==========");
        assertNotNull(testUnitId, "testUnitId不应为null（依赖test01）");

        // 1. 调用service.getById()
        UnitVO result = unitService.getById(testUnitId);

        // 2. 验证返回数据与create时一致
        assertNotNull(result, "返回结果不应为null");
        assertEquals(testUnitId, result.getId(), "ID应一致");
        assertEquals("测试单位_件", result.getUnitName(), "unitName应一致");
        assertEquals("piece", result.getUnitDisplayName(), "unitDisplayName应一致");

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(3)
    @DisplayName("3. 分页查询计量单位列表")
    void test03_List() {
        log.info("========== 测试：分页查询计量单位列表 ==========");
        assertNotNull(testUnitId, "testUnitId不应为null（依赖test01）");

        // 1. 调用service.list() - 使用较大pageSize避免SDK历史数据干扰
        List<UnitVO> list = unitService.list(1, 1000);

        // 2. 验证列表包含刚创建的数据
        assertNotNull(list, "列表不应为null");
        assertFalse(list.isEmpty(), "列表不应为空");

        boolean found = list.stream()
            .anyMatch(unit -> testUnitId.equals(unit.getId()));
        assertTrue(found, "列表应包含刚创建的数据（ID: " + testUnitId + "）");

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(4)
    @DisplayName("4. 更新计量单位")
    void test04_Update() {
        log.info("========== 测试：更新计量单位 ==========");
        assertNotNull(testUnitId, "testUnitId不应为null（依赖test01）");

        // 1. 先get获取当前数据
        UnitVO before = unitService.getById(testUnitId);
        log.info("更新前数据: {}", before);

        // 2. 修改字段
        UnitCreateDTO updateDto = new UnitCreateDTO();
        updateDto.setUnitName("测试单位_件（已更新）");
        updateDto.setUnitDisplayName("piece_updated");
        updateDto.setUnitCategory("数量");
        updateDto.setUnitFactor("1.0");
        updateDto.setMesurementSystem("公制");

        // 3. 调用service.update()
        UnitVO result = unitService.update(testUnitId, updateDto);

        // 4. 验证更新成功
        assertNotNull(result, "返回结果不应为null");
        assertEquals(testUnitId, result.getId(), "ID不应变化");
        assertEquals("测试单位_件（已更新）", result.getUnitName(), "unitName应已更新");
        assertEquals("piece_updated", result.getUnitDisplayName(), "unitDisplayName应已更新");

        log.info("更新成功: {}", result);
    }

    @Test
    @Order(5)
    @DisplayName("5. 删除计量单位")
    void test05_Delete() {
        log.info("========== 测试：删除计量单位 ==========");
        assertNotNull(testUnitId, "testUnitId不应为null（依赖test01）");

        // 1. 调用service.delete()
        assertDoesNotThrow(() -> {
            unitService.delete(testUnitId);
        }, "删除操作不应抛出异常");

        // 2. 验证删除成功（查询应抛出异常或返回null）
        Exception exception = assertThrows(Exception.class, () -> {
            unitService.getById(testUnitId);
        }, "删除后查询应抛出异常");

        log.info("删除成功，异常信息: {}", exception.getMessage());
    }

    /**
     * 临时测试：验证SDK是否支持指定ID创建
     * 用于OpenAPI示例数据生成方案的可行性验证
     */
    @Test
    @Order(99)
    @DisplayName("99. [临时] 验证SDK是否支持指定ID创建")
    void test99_VerifyCustomIdSupport() {
        log.info("========== 临时测试：验证SDK是否支持指定ID创建 ==========");

        // SDK要求ID必须是数字格式！使用纯数字ID
        String customId = "9999999999999999";  // 16位数字ID
        String createdId = null;

        try {
            // 1. 构造Entity，指定自定义ID
            Unit unit = new Unit();
            unit.setId(customId);  // 关键：指定固定ID
            unit.setUnitName("临时测试ID");
            unit.setUnitDisplayName("test");
            unit.setUnitCategory("测试");
            unit.setUnitFactor("1.0");
            unit.setMesurementSystem("公制");

            // 2. 调用Dao层创建
            log.info("请求创建的ID: {}", customId);
            Unit created = unitDao.create(unit);
            createdId = created.getId();
            log.info("SDK返回的ID: {}", createdId);

            // 3. 验证结果
            if (customId.equals(createdId)) {
                log.info("✅✅✅ SDK支持指定ID创建！返回的ID与请求的ID一致");
                log.info("✅ 可以继续执行OpenAPI示例数据生成计划");
            } else {
                log.warn("⚠️⚠️⚠️ SDK不支持指定ID创建！");
                log.warn("⚠️ 请求的ID: {}", customId);
                log.warn("⚠️ 返回的ID: {}", createdId);
                log.warn("⚠️ 需要调整OpenAPI示例数据生成方案（使用SDK生成的ID）");
            }

            // 4. 验证对象存在
            Unit retrieved = unitDao.findById(createdId);
            assertNotNull(retrieved, "创建的对象应该可以查询到");
            assertEquals("临时测试ID", retrieved.getUnitName());

        } finally {
            // 5. 清理测试数据
            if (createdId != null) {
                try {
                    unitDao.delete(createdId);
                    log.info("已清理临时测试数据，ID: {}", createdId);
                } catch (Exception e) {
                    log.warn("清理测试数据失败: {}", e.getMessage());
                }
            }
        }
    }
}
