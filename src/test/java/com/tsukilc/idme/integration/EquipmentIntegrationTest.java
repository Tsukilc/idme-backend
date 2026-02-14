package com.tsukilc.idme.integration;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.*;
import com.tsukilc.idme.service.*;
import com.tsukilc.idme.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Equipment（设备）模块集成测试
 * 依赖：BusinessPartner, EquipmentModel, Location, EquipmentClassfication
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipmentIntegrationTest {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    // 依赖数据ID
    private static String testManufacturerId;      // 生产厂家
    private static String testSupplierId;          // 供应商
    private static String testClassficationId;     // 设备分类
    private static String testModelId;             // 设备机型
    private static String testDeptId;              // 部门（Location需要）
    private static String testEmployeeId;          // 员工（Location的manager）
    private static String testLocationId;          // 位置

    // 测试数据ID
    private static String testEquipmentId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建所有依赖数据")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建所有依赖数据 ==========");

        // 1. 创建生产厂家（BusinessPartner）
        BusinessPartnerCreateDTO manufacturerDto = new BusinessPartnerCreateDTO();
        manufacturerDto.setPartnerCode("MANUFACTURER_EQUIP_TEST");
        manufacturerDto.setPartnerName("测试生产厂家_设备");
        manufacturerDto.setPartnerType("Machinery");
        BusinessPartnerVO manufacturer = businessPartnerService.create(manufacturerDto);
        testManufacturerId = manufacturer.getId();
        log.info("生产厂家创建成功，ID: {}", testManufacturerId);

        // 2. 创建供应商（BusinessPartner）
        BusinessPartnerCreateDTO supplierDto = new BusinessPartnerCreateDTO();
        supplierDto.setPartnerCode("SUPPLIER_EQUIP_TEST");
        supplierDto.setPartnerName("测试供应商_设备");
        supplierDto.setPartnerType("Machinery");
        BusinessPartnerVO supplier = businessPartnerService.create(supplierDto);
        testSupplierId = supplier.getId();
        log.info("供应商创建成功，ID: {}", testSupplierId);

        // 3. 创建设备分类（EquipmentClassfication）
        EquipmentClassficationCreateDTO classDto = new EquipmentClassficationCreateDTO();
        classDto.setEquipmentClassName("测试设备分类_设备");
        EquipmentClassficationVO classfication = equipmentClassficationService.create(classDto);
        testClassficationId = classfication.getId();
        log.info("设备分类创建成功，ID: {}", testClassficationId);

        // 4. 创建设备机型（EquipmentModel）
        EquipmentModelCreateDTO modelDto = new EquipmentModelCreateDTO();
        modelDto.setModelCode("MODEL_EQUIP_TEST");
        modelDto.setModelName("测试设备机型_设备");
        modelDto.setManufacturer(testManufacturerId);
        modelDto.setBrand("测试品牌");
        modelDto.setCategory(testClassficationId);
        String modelId = equipmentModelService.create(modelDto);
        testModelId = modelId;
        log.info("设备机型创建成功，ID: {}", testModelId);

        // 5. 创建部门（Department，Location需要）
        DepartmentCreateDTO deptDto = new DepartmentCreateDTO();
        deptDto.setDeptCode("DEPT_EQUIP_TEST");
        deptDto.setDeptName("设备测试部门");
        deptDto.setManager("测试经理");
        DepartmentVO dept = departmentService.create(deptDto);
        testDeptId = dept.getId();
        log.info("部门创建成功，ID: {}", testDeptId);

        // 6. 创建员工（Employee，Location的manager）
        EmployeeCreateDTO empDto = new EmployeeCreateDTO();
        empDto.setEmployeeNo("EMP_EQUIP_MGR");
        empDto.setEmployeeName("设备管理员");
        empDto.setDept(testDeptId);
        empDto.setJobTitle("设备管理员");
        empDto.setStatus("在职");
        empDto.setHireDate(LocalDate.of(2024, 1, 1));
        EmployeeVO emp = employeeService.create(empDto);
        testEmployeeId = emp.getId();
        log.info("员工创建成功，ID: {}", testEmployeeId);

        // 7. 创建位置（Location）
        LocationCreateDTO locDto = new LocationCreateDTO();
        locDto.setLocationCode("LOC_EQUIP_TEST");
        locDto.setLocationName("设备测试位置");
        locDto.setLocationType("Workshop");
        locDto.setManager(testEmployeeId);
        LocationVO location = locationService.create(locDto);
        testLocationId = location.getId();
        log.info("位置创建成功，ID: {}", testLocationId);

        assertNotNull(testManufacturerId);
        assertNotNull(testSupplierId);
        assertNotNull(testClassficationId);
        assertNotNull(testModelId);
        assertNotNull(testLocationId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建设备")
    void test02_CreateEquipment() {
        log.info("========== 测试：创建设备 ==========");

        EquipmentCreateDTO dto = new EquipmentCreateDTO();
        dto.setEquipmentCode("EQUIP_TEST_001");
        dto.setEquipmentName("测试设备");
        dto.setManufacturerName(testManufacturerId);
        dto.setSupplierName(testSupplierId);
        dto.setBrand("测试品牌");
        dto.setModelSpec("V1.0");
        dto.setEquipmentModelRef(testModelId);
        dto.setLocationRef(testLocationId);
        dto.setCategory(testClassficationId);
        dto.setStatus("Standby");
        dto.setSerialNumber("SN-12345");
        // dto.setProductionDate(LocalDateTime.of(2024, 1, 1, 0, 0)); // TODO: 需要自定义序列化器将LocalDateTime转为时间戳
        dto.setServiceLifeYears(10);
        dto.setDepreciationMethod("StraightLine");

        String equipmentId = equipmentService.create(dto);

        assertNotNull(equipmentId);
        testEquipmentId = equipmentId;
        log.info("设备创建成功，ID: {}", testEquipmentId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询设备")
    void test03_GetById() {
        log.info("========== 测试：查询设备 ==========");

        EquipmentVO result = equipmentService.getById(testEquipmentId);

        assertNotNull(result);
        assertEquals(testEquipmentId, result.getId());
        assertEquals("EQUIP_TEST_001", result.getEquipmentCode());
        assertEquals("测试设备", result.getEquipmentName());
        assertEquals(testManufacturerId, result.getManufacturerName());
        assertEquals(testSupplierId, result.getSupplierName());
        assertEquals(testLocationId, result.getLocationRef());
        assertEquals(testClassficationId, result.getCategory());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询设备")
    void test04_List() {
        log.info("========== 测试：分页查询设备 ==========");

        PageResult<EquipmentVO> result = equipmentService.list(1, 1000);

        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getRecords().stream().anyMatch(e -> testEquipmentId.equals(e.getId())));

        log.info("查询成功，共 {} 条记录", result.getRecords().size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新设备")
    void test05_Update() {
        log.info("========== 测试：更新设备 ==========");

        EquipmentCreateDTO updateDto = new EquipmentCreateDTO();
        updateDto.setEquipmentCode("EQUIP_TEST_001_UPDATED");
        updateDto.setEquipmentName("测试设备（已更新）");
        updateDto.setManufacturerName(testManufacturerId);
        updateDto.setSupplierName(testSupplierId);
        updateDto.setBrand("测试品牌V2");
        updateDto.setModelSpec("V2.0");
        updateDto.setEquipmentModelRef(testModelId);
        updateDto.setLocationRef(testLocationId);
        updateDto.setCategory(testClassficationId);
        updateDto.setStatus("InOperation"); // SDK枚举值：Standby, Shutdown, UnderMaintenance, InOperation, Scrap
        updateDto.setSerialNumber("SN-12345-UPDATED");
        // updateDto.setProductionDate(LocalDateTime.of(2024, 1, 1, 0, 0)); // TODO: 需要自定义序列化器
        updateDto.setServiceLifeYears(15);
        updateDto.setDepreciationMethod("Double"); // SDK枚举值：NoDepreciation, StraightLine, Double, SumOfYear

        assertDoesNotThrow(() -> equipmentService.update(testEquipmentId, updateDto));

        EquipmentVO updated = equipmentService.getById(testEquipmentId);
        assertEquals("EQUIP_TEST_001_UPDATED", updated.getEquipmentCode());
        assertEquals("测试品牌V2", updated.getBrand());
        assertEquals("InOperation", updated.getStatus());

        log.info("更新成功: {}", updated);
    }

    @Test
    @Order(6)
    @DisplayName("6. 获取设备技术参数")
    void test06_GetTechParams() {
        log.info("========== 测试：获取设备技术参数 ==========");

        Object techParams = equipmentService.getTechParams(testEquipmentId);
        // 创建时未设置techParams，应该返回null
        log.info("技术参数: {}", techParams);
    }

    @Test
    @Order(7)
    @DisplayName("7. 更新设备技术参数")
    void test07_UpdateTechParams() {
        log.info("========== 测试：更新设备技术参数 ==========");

        java.util.Map<String, Object> techParams = new java.util.HashMap<>();
        techParams.put("maxSpeed", 100);
        techParams.put("power", 500);
        techParams.put("voltage", "380V");

        assertDoesNotThrow(() -> equipmentService.updateTechParams(testEquipmentId, techParams));

        // 验证更新成功
        Object updatedParams = equipmentService.getTechParams(testEquipmentId);
        assertNotNull(updatedParams);
        log.info("技术参数更新成功: {}", updatedParams);
    }

    @Test
    @Order(8)
    @DisplayName("8. 删除设备")
    void test08_Delete() {
        log.info("========== 测试：删除设备 ==========");

        assertDoesNotThrow(() -> equipmentService.delete(testEquipmentId));
        log.info("设备删除成功");
    }

    @Test
    @Order(9)
    @DisplayName("9. 清理：删除所有依赖数据")
    void test09_Cleanup() {
        log.info("========== 清理：删除所有依赖数据 ==========");

        // 删除顺序：先删除依赖者，再删除被依赖者
        if (testLocationId != null) {
            assertDoesNotThrow(() -> locationService.delete(testLocationId));
            log.info("位置删除成功");
        }

        if (testEmployeeId != null) {
            assertDoesNotThrow(() -> employeeService.delete(testEmployeeId));
            log.info("员工删除成功");
        }

        if (testDeptId != null) {
            assertDoesNotThrow(() -> departmentService.delete(testDeptId));
            log.info("部门删除成功");
        }

        if (testModelId != null) {
            assertDoesNotThrow(() -> equipmentModelService.delete(testModelId));
            log.info("设备机型删除成功");
        }

        if (testClassficationId != null) {
            assertDoesNotThrow(() -> equipmentClassficationService.delete(testClassficationId));
            log.info("设备分类删除成功");
        }

        if (testSupplierId != null) {
            assertDoesNotThrow(() -> businessPartnerService.delete(testSupplierId));
            log.info("供应商删除成功");
        }

        if (testManufacturerId != null) {
            assertDoesNotThrow(() -> businessPartnerService.delete(testManufacturerId));
            log.info("生产厂家删除成功");
        }
    }
}
