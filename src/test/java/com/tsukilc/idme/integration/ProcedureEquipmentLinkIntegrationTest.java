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

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProcedureEquipmentLink（工序-设备关联）模块集成测试
 * 依赖：WorkingProcedure, Equipment
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProcedureEquipmentLinkIntegrationTest {

    @Autowired
    private ProcedureEquipmentLinkService procedureEquipmentLinkService;

    @Autowired
    private WorkingProcedureService workingProcedureService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LocationService locationService;

    // 依赖数据ID
    private static String testManufacturerId;
    private static String testSupplierId;
    private static String testClassficationId;
    private static String testModelId;
    private static String testDeptId;
    private static String testManagerId;
    private static String testLocationId;
    private static String testEquipmentId;
    private static String testEmployeeId;
    private static String testProcedureId;

    // 测试数据ID
    private static String testLinkId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建所有依赖数据")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建所有依赖数据 ==========");

        // 1. 创建生产厂家
        BusinessPartnerCreateDTO manufacturerDto = new BusinessPartnerCreateDTO();
        manufacturerDto.setPartnerCode("MANUFACTURER_LINK_TEST");
        manufacturerDto.setPartnerName("测试生产厂家_关联");
        manufacturerDto.setPartnerType("Machinery");
        testManufacturerId = businessPartnerService.create(manufacturerDto).getId();
        log.info("生产厂家创建成功，ID: {}", testManufacturerId);

        // 2. 创建供应商
        BusinessPartnerCreateDTO supplierDto = new BusinessPartnerCreateDTO();
        supplierDto.setPartnerCode("SUPPLIER_LINK_TEST");
        supplierDto.setPartnerName("测试供应商_关联");
        supplierDto.setPartnerType("Machinery");
        testSupplierId = businessPartnerService.create(supplierDto).getId();
        log.info("供应商创建成功，ID: {}", testSupplierId);

        // 3. 创建设备分类
        EquipmentClassficationCreateDTO classDto = new EquipmentClassficationCreateDTO();
        classDto.setEquipmentClassName("测试设备分类_关联");
        testClassficationId = equipmentClassficationService.create(classDto).getId();
        log.info("设备分类创建成功，ID: {}", testClassficationId);

        // 4. 创建设备机型
        EquipmentModelCreateDTO modelDto = new EquipmentModelCreateDTO();
        modelDto.setModelCode("MODEL_LINK_TEST");
        modelDto.setModelName("测试设备机型_关联");
        modelDto.setManufacturer(testManufacturerId);
        modelDto.setBrand("测试品牌");
        modelDto.setCategory(testClassficationId);
        testModelId = equipmentModelService.create(modelDto);
        log.info("设备机型创建成功，ID: {}", testModelId);

        // 5. 创建部门
        DepartmentCreateDTO deptDto = new DepartmentCreateDTO();
        deptDto.setDeptCode("DEPT_LINK_TEST");
        deptDto.setDeptName("关联测试部门");
        deptDto.setManager("测试经理");
        testDeptId = departmentService.create(deptDto).getId();
        log.info("部门创建成功，ID: {}", testDeptId);

        // 6. 创建经理员工
        EmployeeCreateDTO mgrDto = new EmployeeCreateDTO();
        mgrDto.setEmployeeNo("EMP_LINK_MGR");
        mgrDto.setEmployeeName("关联管理员");
        mgrDto.setDept(testDeptId);
        mgrDto.setJobTitle("管理员");
        mgrDto.setStatus("在职");
        mgrDto.setHireDate(LocalDate.of(2024, 1, 1));
        testManagerId = employeeService.create(mgrDto).getId();
        log.info("经理员工创建成功，ID: {}", testManagerId);

        // 7. 创建位置
        LocationCreateDTO locDto = new LocationCreateDTO();
        locDto.setLocationCode("LOC_LINK_TEST");
        locDto.setLocationName("关联测试位置");
        locDto.setLocationType("Workshop");
        locDto.setManager(testManagerId);
        testLocationId = locationService.create(locDto).getId();
        log.info("位置创建成功，ID: {}", testLocationId);

        // 8. 创建设备
        EquipmentCreateDTO equipDto = new EquipmentCreateDTO();
        equipDto.setEquipmentCode("EQUIP_LINK_TEST");
        equipDto.setEquipmentName("测试设备_关联");
        equipDto.setManufacturerName(testManufacturerId);
        equipDto.setSupplierName(testSupplierId);
        equipDto.setBrand("测试品牌");
        equipDto.setModelSpec("V1.0");
        equipDto.setEquipmentModelRef(testModelId);
        equipDto.setLocationRef(testLocationId);
        equipDto.setCategory(testClassficationId);
        equipDto.setStatus("Standby");
        equipDto.setSerialNumber("SN-LINK-12345");
        equipDto.setServiceLifeYears(10);
        equipDto.setDepreciationMethod("StraightLine");
        testEquipmentId = equipmentService.create(equipDto);
        log.info("设备创建成功，ID: {}", testEquipmentId);

        // 9. 创建操作员工
        EmployeeCreateDTO empDto = new EmployeeCreateDTO();
        empDto.setEmployeeNo("EMP_LINK_OPERATOR");
        empDto.setEmployeeName("关联操作员");
        empDto.setDept(testDeptId);
        empDto.setJobTitle("操作员");
        empDto.setStatus("在职");
        empDto.setHireDate(LocalDate.of(2024, 1, 1));
        testEmployeeId = employeeService.create(empDto).getId();
        log.info("操作员工创建成功，ID: {}", testEmployeeId);

        // 10. 创建工序
        WorkingProcedureCreateDTO procDto = new WorkingProcedureCreateDTO();
        procDto.setProcedureCode("PROC_LINK_TEST");
        procDto.setProcedureName("测试工序_关联");
        procDto.setSteps("测试步骤");
        procDto.setMainProductionEquipment(testEquipmentId);
        procDto.setMainInspectionEquipment(testEquipmentId); // SDK必填
        procDto.setOperatorRef(testEmployeeId);
        procDto.setStatus("InProgress");
        testProcedureId = workingProcedureService.create(procDto);
        log.info("工序创建成功，ID: {}", testProcedureId);

        assertNotNull(testProcedureId);
        assertNotNull(testEquipmentId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建工序-设备关联")
    void test02_CreateLink() {
        log.info("========== 测试：创建工序-设备关联 ==========");

        ProcedureEquipmentLinkCreateDTO dto = new ProcedureEquipmentLinkCreateDTO();
        dto.setProcedure(testProcedureId);
        dto.setEquipment1(testEquipmentId);
        dto.setRole("Production"); // 角色：生产/检测/辅助
        dto.setRemarks("测试备注");

        String linkId = procedureEquipmentLinkService.create(dto);

        assertNotNull(linkId);
        testLinkId = linkId;
        log.info("工序-设备关联创建成功，ID: {}", testLinkId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询工序-设备关联")
    void test03_GetById() {
        log.info("========== 测试：查询工序-设备关联 ==========");

        ProcedureEquipmentLinkVO result = procedureEquipmentLinkService.getById(testLinkId);

        assertNotNull(result);
        assertEquals(testLinkId, result.getId());
        assertEquals(testProcedureId, result.getProcedure());
        assertEquals(testEquipmentId, result.getEquipment1());
        assertEquals("Production", result.getRole());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询工序-设备关联")
    void test04_List() {
        log.info("========== 测试：分页查询工序-设备关联 ==========");

        PageResult<ProcedureEquipmentLinkVO> result = procedureEquipmentLinkService.list(1, 1000);

        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getRecords().stream().anyMatch(l -> testLinkId.equals(l.getId())));

        log.info("查询成功，共 {} 条记录", result.getRecords().size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新工序-设备关联")
    void test05_Update() {
        log.info("========== 测试：更新工序-设备关联 ==========");

        ProcedureEquipmentLinkCreateDTO updateDto = new ProcedureEquipmentLinkCreateDTO();
        updateDto.setProcedure(testProcedureId);
        updateDto.setEquipment1(testEquipmentId);
        updateDto.setRole("Inspection"); // 修改角色为检测
        updateDto.setRemarks("测试备注（已更新）");

        assertDoesNotThrow(() -> procedureEquipmentLinkService.update(testLinkId, updateDto));

        ProcedureEquipmentLinkVO updated = procedureEquipmentLinkService.getById(testLinkId);
        assertEquals("Inspection", updated.getRole());
        assertEquals("测试备注（已更新）", updated.getRemarks());

        log.info("更新成功: {}", updated);
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除工序-设备关联")
    void test06_Delete() {
        log.info("========== 测试：删除工序-设备关联 ==========");

        assertDoesNotThrow(() -> procedureEquipmentLinkService.delete(testLinkId));
        log.info("工序-设备关联删除成功");
    }

    @Test
    @Order(7)
    @DisplayName("7. 清理：删除所有依赖数据")
    void test07_Cleanup() {
        log.info("========== 清理：删除所有依赖数据 ==========");

        if (testProcedureId != null) {
            assertDoesNotThrow(() -> workingProcedureService.delete(testProcedureId));
            log.info("工序删除成功");
        }

        if (testEmployeeId != null) {
            assertDoesNotThrow(() -> employeeService.delete(testEmployeeId));
            log.info("操作员工删除成功");
        }

        if (testEquipmentId != null) {
            assertDoesNotThrow(() -> equipmentService.delete(testEquipmentId));
            log.info("设备删除成功");
        }

        if (testLocationId != null) {
            assertDoesNotThrow(() -> locationService.delete(testLocationId));
            log.info("位置删除成功");
        }

        if (testManagerId != null) {
            assertDoesNotThrow(() -> employeeService.delete(testManagerId));
            log.info("经理员工删除成功");
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
