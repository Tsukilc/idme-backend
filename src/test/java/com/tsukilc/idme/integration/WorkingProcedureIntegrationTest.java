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
 * WorkingProcedure（工序）模块集成测试
 * 依赖：Equipment（2个，主生产设备+主检测设备）, Employee（操作人员）
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkingProcedureIntegrationTest {

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

    // 测试数据ID
    private static String testProcedureId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建所有依赖数据")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建所有依赖数据 ==========");

        // 1. 创建生产厂家（BusinessPartner）
        BusinessPartnerCreateDTO manufacturerDto = new BusinessPartnerCreateDTO();
        manufacturerDto.setPartnerCode("MANUFACTURER_PROC_TEST");
        manufacturerDto.setPartnerName("测试生产厂家_工序");
        manufacturerDto.setPartnerType("Machinery");
        BusinessPartnerVO manufacturer = businessPartnerService.create(manufacturerDto);
        testManufacturerId = manufacturer.getId();
        log.info("生产厂家创建成功，ID: {}", testManufacturerId);

        // 2. 创建供应商（BusinessPartner）
        BusinessPartnerCreateDTO supplierDto = new BusinessPartnerCreateDTO();
        supplierDto.setPartnerCode("SUPPLIER_PROC_TEST");
        supplierDto.setPartnerName("测试供应商_工序");
        supplierDto.setPartnerType("Machinery");
        BusinessPartnerVO supplier = businessPartnerService.create(supplierDto);
        testSupplierId = supplier.getId();
        log.info("供应商创建成功，ID: {}", testSupplierId);

        // 3. 创建设备分类（EquipmentClassfication）
        EquipmentClassficationCreateDTO classDto = new EquipmentClassficationCreateDTO();
        classDto.setEquipmentClassName("测试设备分类_工序");
        EquipmentClassficationVO classfication = equipmentClassficationService.create(classDto);
        testClassficationId = classfication.getId();
        log.info("设备分类创建成功，ID: {}", testClassficationId);

        // 4. 创建设备机型（EquipmentModel）
        EquipmentModelCreateDTO modelDto = new EquipmentModelCreateDTO();
        modelDto.setModelCode("MODEL_PROC_TEST");
        modelDto.setModelName("测试设备机型_工序");
        modelDto.setManufacturer(testManufacturerId);
        modelDto.setBrand("测试品牌");
        modelDto.setCategory(testClassficationId);
        String modelId = equipmentModelService.create(modelDto);
        testModelId = modelId;
        log.info("设备机型创建成功，ID: {}", testModelId);

        // 5. 创建部门（Department，Location需要）
        DepartmentCreateDTO deptDto = new DepartmentCreateDTO();
        deptDto.setDeptCode("DEPT_PROC_TEST");
        deptDto.setDeptName("工序测试部门");
        deptDto.setManager("测试经理");
        DepartmentVO dept = departmentService.create(deptDto);
        testDeptId = dept.getId();
        log.info("部门创建成功，ID: {}", testDeptId);

        // 6. 创建经理员工（Employee，Location的manager）
        EmployeeCreateDTO mgrDto = new EmployeeCreateDTO();
        mgrDto.setEmployeeNo("EMP_PROC_MGR");
        mgrDto.setEmployeeName("工序管理员");
        mgrDto.setDept(testDeptId);
        mgrDto.setJobTitle("管理员");
        mgrDto.setStatus("在职");
        mgrDto.setHireDate(LocalDate.of(2024, 1, 1));
        EmployeeVO manager = employeeService.create(mgrDto);
        testManagerId = manager.getId();
        log.info("经理员工创建成功，ID: {}", testManagerId);

        // 7. 创建位置（Location）
        LocationCreateDTO locDto = new LocationCreateDTO();
        locDto.setLocationCode("LOC_PROC_TEST");
        locDto.setLocationName("工序测试位置");
        locDto.setLocationType("Workshop");
        locDto.setManager(testManagerId);
        LocationVO location = locationService.create(locDto);
        testLocationId = location.getId();
        log.info("位置创建成功，ID: {}", testLocationId);

        // 8. 创建设备（Equipment）
        EquipmentCreateDTO equipDto = new EquipmentCreateDTO();
        equipDto.setEquipmentCode("EQUIP_PROC_TEST");
        equipDto.setEquipmentName("测试设备_工序");
        equipDto.setManufacturerName(testManufacturerId);
        equipDto.setSupplierName(testSupplierId);
        equipDto.setBrand("测试品牌");
        equipDto.setModelSpec("V1.0");
        equipDto.setEquipmentModelRef(testModelId);
        equipDto.setLocationRef(testLocationId);
        equipDto.setCategory(testClassficationId);
        equipDto.setStatus("Standby");
        equipDto.setSerialNumber("SN-PROC-12345");
        equipDto.setServiceLifeYears(10);
        equipDto.setDepreciationMethod("StraightLine");
        String equipmentId = equipmentService.create(equipDto);
        testEquipmentId = equipmentId;
        log.info("设备创建成功，ID: {}", testEquipmentId);

        // 9. 创建操作员工（Employee）
        EmployeeCreateDTO empDto = new EmployeeCreateDTO();
        empDto.setEmployeeNo("EMP_PROC_OPERATOR");
        empDto.setEmployeeName("工序操作员");
        empDto.setDept(testDeptId);
        empDto.setJobTitle("操作员");
        empDto.setStatus("在职");
        empDto.setHireDate(LocalDate.of(2024, 1, 1));
        EmployeeVO employee = employeeService.create(empDto);
        testEmployeeId = employee.getId();
        log.info("操作员工创建成功，ID: {}", testEmployeeId);

        assertNotNull(testEquipmentId);
        assertNotNull(testEmployeeId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建工序")
    void test02_CreateProcedure() {
        log.info("========== 测试：创建工序 ==========");

        WorkingProcedureCreateDTO dto = new WorkingProcedureCreateDTO();
        dto.setProcedureCode("PROC_TEST_001");
        dto.setProcedureName("测试工序");
        dto.setSteps("步骤1：准备材料；步骤2：加工；步骤3：检验");
        dto.setMainProductionEquipment(testEquipmentId);
        dto.setMainInspectionEquipment(testEquipmentId); // 使用同一个设备
        dto.setOperatorUser("测试操作员");
        dto.setOperatorRef(testEmployeeId);
        dto.setStatus("InProgress"); // SDK枚举值：NotStarted, InProgress, Completed, Paused
        // dto.setStartTime() 和 dto.setEndTime() 暂时不设置（时间戳问题）
        dto.setRemarks("测试备注");

        String procedureId = workingProcedureService.create(dto);

        assertNotNull(procedureId);
        testProcedureId = procedureId;
        log.info("工序创建成功，ID: {}", testProcedureId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询工序")
    void test03_GetById() {
        log.info("========== 测试：查询工序 ==========");

        WorkingProcedureVO result = workingProcedureService.getById(testProcedureId);

        assertNotNull(result);
        assertEquals(testProcedureId, result.getId());
        assertEquals("PROC_TEST_001", result.getProcedureCode());
        assertEquals("测试工序", result.getProcedureName());
        assertEquals(testEquipmentId, result.getMainProductionEquipment());
        assertEquals(testEquipmentId, result.getMainInspectionEquipment());
        assertEquals(testEmployeeId, result.getOperatorRef());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询工序")
    void test04_List() {
        log.info("========== 测试：分页查询工序 ==========");

        PageResult<WorkingProcedureVO> result = workingProcedureService.list(1, 1000);

        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getRecords().stream().anyMatch(p -> testProcedureId.equals(p.getId())));

        log.info("查询成功，共 {} 条记录", result.getRecords().size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新工序")
    void test05_Update() {
        log.info("========== 测试：更新工序 ==========");

        WorkingProcedureCreateDTO updateDto = new WorkingProcedureCreateDTO();
        updateDto.setProcedureCode("PROC_TEST_001_UPDATED");
        updateDto.setProcedureName("测试工序（已更新）");
        updateDto.setSteps("步骤1：准备材料（已更新）；步骤2：加工（已更新）");
        updateDto.setMainProductionEquipment(testEquipmentId);
        updateDto.setMainInspectionEquipment(testEquipmentId);
        updateDto.setOperatorUser("测试操作员（已更新）");
        updateDto.setOperatorRef(testEmployeeId);
        updateDto.setStatus("Completed"); // SDK枚举值：NotStarted, InProgress, Completed, Paused
        updateDto.setRemarks("测试备注（已更新）");

        assertDoesNotThrow(() -> workingProcedureService.update(testProcedureId, updateDto));

        WorkingProcedureVO updated = workingProcedureService.getById(testProcedureId);
        assertEquals("PROC_TEST_001_UPDATED", updated.getProcedureCode());
        assertEquals("测试工序（已更新）", updated.getProcedureName());
        assertEquals("Completed", updated.getStatus());

        log.info("更新成功: {}", updated);
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除工序")
    void test06_Delete() {
        log.info("========== 测试：删除工序 ==========");

        assertDoesNotThrow(() -> workingProcedureService.delete(testProcedureId));
        log.info("工序删除成功");
    }

    @Test
    @Order(7)
    @DisplayName("7. 清理：删除所有依赖数据")
    void test07_Cleanup() {
        log.info("========== 清理：删除所有依赖数据 ==========");

        // 删除顺序：先删除依赖者，再删除被依赖者
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
