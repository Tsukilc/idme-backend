package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.*;
import com.tsukilc.idme.service.*;
import com.tsukilc.idme.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProcedurePartLink（工序-物料关联）集成测试
 *
 * 依赖链：
 * WorkingProcedure → Equipment → (EquipmentModel, Location, Department, Employee, BusinessPartner, EquipmentClassfication)
 * Part → (Unit, BusinessPartner, PartClassfication)
 *
 * 测试流程：
 * 1. 准备完整依赖数据
 * 2. 创建关联
 * 3. 查询关联详情
 * 4. 分页查询列表
 * 5. 更新关联
 * 6. 删除关联
 * 7. 清理所有测试数据
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProcedurePartLinkIntegrationTest {

    @Autowired
    private ProcedurePartLinkService procedurePartLinkService;

    @Autowired
    private WorkingProcedureService workingProcedureService;

    @Autowired
    private PartService partService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private PartClassficationService partClassficationService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    // 测试数据ID
    private static String testLinkId;
    private static String testProcedureId;
    private static String testPartId;
    private static String testPartMasterId;

    // 依赖数据ID
    private static String testUnitId;
    private static String testSupplierId;
    private static String testPartCategoryId;
    private static String testEquipmentId;
    private static String testEquipmentProductionId;
    private static String testEquipmentInspectionId;
    private static String testEmployeeId;

    // 设备依赖数据
    private static String testManufacturerId;
    private static String testModelId;
    private static String testLocationId;
    private static String testDepartmentId;
    private static String testEquipmentCategoryId;

    @Test
    @Order(1)
    void test01_PrepareData() {
        log.info("=== 测试1：准备完整依赖数据 ===");

        // === Part的依赖数据 ===

        // 1. 创建计量单位
        UnitCreateDTO unitDTO = new UnitCreateDTO();
        unitDTO.setUnitName("件");
        unitDTO.setUnitDisplayName("件");
        UnitVO unit = unitService.create(unitDTO);
        testUnitId = unit.getId();
        assertNotNull(testUnitId);
        log.info("创建计量单位成功，ID: {}", testUnitId);

        // 2. 创建供应商
        BusinessPartnerCreateDTO supplierDTO = new BusinessPartnerCreateDTO();
        supplierDTO.setPartnerCode("SUPPLIER-LINK-001");
        supplierDTO.setPartnerName("工序物料测试供应商");
        supplierDTO.setPartnerType("Parts");
        supplierDTO.setPhone("12345678");
        BusinessPartnerVO supplier = businessPartnerService.create(supplierDTO);
        testSupplierId = supplier.getId();
        assertNotNull(testSupplierId);
        log.info("创建供应商成功，ID: {}", testSupplierId);

        // 3. 创建物料分类
        PartClassficationCreateDTO partCategoryDTO = new PartClassficationCreateDTO();
        partCategoryDTO.setPartClassName("消耗品");
        PartClassficationVO partCategory = partClassficationService.create(partCategoryDTO);
        testPartCategoryId = partCategory.getId();
        assertNotNull(testPartCategoryId);
        log.info("创建物料分类成功，ID: {}", testPartCategoryId);

        // === WorkingProcedure的依赖数据（设备依赖链）===

        // 4. 创建设备生产厂家
        BusinessPartnerCreateDTO manufacturerDTO = new BusinessPartnerCreateDTO();
        manufacturerDTO.setPartnerCode("MANU-LINK-001");
        manufacturerDTO.setPartnerName("设备生产厂家");
        manufacturerDTO.setPartnerType("Machinery");
        manufacturerDTO.setPhone("88888888");
        BusinessPartnerVO manufacturer = businessPartnerService.create(manufacturerDTO);
        testManufacturerId = manufacturer.getId();
        assertNotNull(testManufacturerId);
        log.info("创建设备生产厂家成功，ID: {}", testManufacturerId);

        // 5. 创建设备分类
        EquipmentClassficationCreateDTO equipCategoryDTO = new EquipmentClassficationCreateDTO();
        equipCategoryDTO.setEquipmentClassName("生产设备");
        EquipmentClassficationVO equipCategory = equipmentClassficationService.create(equipCategoryDTO);
        testEquipmentCategoryId = equipCategory.getId();
        assertNotNull(testEquipmentCategoryId);
        log.info("创建设备分类成功，ID: {}", testEquipmentCategoryId);

        // 6. 创建设备型号
        EquipmentModelCreateDTO modelDTO = new EquipmentModelCreateDTO();
        modelDTO.setModelCode("MODEL-LINK-001");
        modelDTO.setModelName("测试设备型号");
        modelDTO.setManufacturer(testManufacturerId);
        modelDTO.setCategory(testEquipmentCategoryId);
        String testModelId = equipmentModelService.create(modelDTO);
        this.testModelId = testModelId;
        assertNotNull(testModelId);
        log.info("创建设备型号成功，ID: {}", testModelId);

        // 7. 创建部门
        DepartmentCreateDTO deptDTO = new DepartmentCreateDTO();
        deptDTO.setDeptCode("DEPT-LINK-001");
        deptDTO.setDeptName("测试部门");
        DepartmentVO dept = departmentService.create(deptDTO);
        testDepartmentId = dept.getId();
        assertNotNull(testDepartmentId);
        log.info("创建部门成功，ID: {}", testDepartmentId);

        // 8. 创建员工
        EmployeeCreateDTO empDTO = new EmployeeCreateDTO();
        empDTO.setEmployeeNo("EMP-LINK-001");
        empDTO.setEmployeeName("测试员工");
        empDTO.setDept(testDepartmentId);
        empDTO.setStatus("Active");
        EmployeeVO employee = employeeService.create(empDTO);
        testEmployeeId = employee.getId();
        assertNotNull(testEmployeeId);
        log.info("创建员工成功，ID: {}", testEmployeeId);

        // 9. 创建位置
        LocationCreateDTO locationDTO = new LocationCreateDTO();
        locationDTO.setLocationCode("LOC-LINK-001");
        locationDTO.setLocationName("测试位置");
        locationDTO.setLocationType("Plant");
        locationDTO.setManager(testEmployeeId);
        LocationVO location = locationService.create(locationDTO);
        testLocationId = location.getId();
        assertNotNull(testLocationId);
        log.info("创建位置成功，ID: {}", testLocationId);

        // 10. 创建生产设备
        EquipmentCreateDTO productionEquipDTO = new EquipmentCreateDTO();
        productionEquipDTO.setEquipmentCode("EQUIP-PROD-LINK-001");
        productionEquipDTO.setEquipmentName("生产设备");
        productionEquipDTO.setManufacturerName(testManufacturerId);
        productionEquipDTO.setSupplierName(testSupplierId);
        productionEquipDTO.setEquipmentModelRef(testModelId);  // 必填字段
        productionEquipDTO.setLocationRef(testLocationId);
        productionEquipDTO.setCategory(testEquipmentCategoryId);
        productionEquipDTO.setStatus("InOperation");
        String productionEquipId = equipmentService.create(productionEquipDTO);
        testEquipmentProductionId = productionEquipId;
        assertNotNull(testEquipmentProductionId);
        log.info("创建生产设备成功，ID: {}", testEquipmentProductionId);

        // 11. 创建检测设备
        EquipmentCreateDTO inspectionEquipDTO = new EquipmentCreateDTO();
        inspectionEquipDTO.setEquipmentCode("EQUIP-INSP-LINK-001");
        inspectionEquipDTO.setEquipmentName("检测设备");
        inspectionEquipDTO.setManufacturerName(testManufacturerId);
        inspectionEquipDTO.setSupplierName(testSupplierId);
        inspectionEquipDTO.setEquipmentModelRef(testModelId);  // 必填字段
        inspectionEquipDTO.setLocationRef(testLocationId);
        inspectionEquipDTO.setCategory(testEquipmentCategoryId);
        inspectionEquipDTO.setStatus("InOperation");
        String inspectionEquipId = equipmentService.create(inspectionEquipDTO);
        testEquipmentInspectionId = inspectionEquipId;
        assertNotNull(testEquipmentInspectionId);
        log.info("创建检测设备成功，ID: {}", testEquipmentInspectionId);

        // === 创建工序 ===

        // 12. 创建工序
        WorkingProcedureCreateDTO procedureDTO = new WorkingProcedureCreateDTO();
        procedureDTO.setProcedureCode("PROC-LINK-001");
        procedureDTO.setProcedureName("测试工序");
        procedureDTO.setMainProductionEquipment(testEquipmentProductionId);
        procedureDTO.setMainInspectionEquipment(testEquipmentInspectionId);
        procedureDTO.setOperatorRef(testEmployeeId);
        procedureDTO.setStatus("NotStarted");
        String procedureId = workingProcedureService.create(procedureDTO);
        testProcedureId = procedureId;
        assertNotNull(testProcedureId);
        log.info("创建工序成功，ID: {}", testProcedureId);

        // === 创建物料（版本对象）===

        // 13. 创建物料
        PartCreateDTO partDTO = new PartCreateDTO();
        partDTO.setPartNumber("PART-LINK-001");
        partDTO.setPartName("测试物料");
        partDTO.setModelSpec("V1.0");
        partDTO.setStockQty(100);
        partDTO.setUnit(testUnitId);
        partDTO.setSupplierName(testSupplierId);
        partDTO.setCategory(testPartCategoryId);
        partDTO.setBusinessVersion("1.0");
        partDTO.setDescription("工序关联测试物料");
        PartVO part = partService.create(partDTO);
        testPartId = part.getId();
        testPartMasterId = part.getMasterId();
        assertNotNull(testPartId);
        assertNotNull(testPartMasterId);
        log.info("创建物料成功，ID: {}, MasterID: {}", testPartId, testPartMasterId);

        log.info("依赖数据准备完成！");
    }

    @Test
    @Order(2)
    void test02_CreateLink() {
        log.info("=== 测试2：创建工序-物料关联 ===");

        ProcedurePartLinkCreateDTO dto = new ProcedurePartLinkCreateDTO();
        dto.setProcedure(testProcedureId);
        dto.setPart1(testPartId);
        dto.setRole("Consumable");
        dto.setQuantity(java.math.BigDecimal.valueOf(5));
        dto.setUom(testUnitId);
        dto.setIsMandatory(true);

        ProcedurePartLinkVO result = procedurePartLinkService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testProcedureId, result.getProcedure());
        assertEquals(testPartId, result.getPart());
        assertEquals(5, result.getQuantity());
        assertEquals(testUnitId, result.getUom());
        assertEquals(true, result.getIsMandatory());
        assertEquals("Consumable", result.getRole());

        testLinkId = result.getId();
        log.info("创建关联成功，ID: {}", testLinkId);
    }

    @Test
    @Order(3)
    void test03_GetById() {
        log.info("=== 测试3：查询关联详情 ===");

        ProcedurePartLinkVO result = procedurePartLinkService.getById(testLinkId);

        assertNotNull(result);
        assertEquals(testLinkId, result.getId());
        assertEquals(testProcedureId, result.getProcedure());
        assertEquals(testPartId, result.getPart());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    void test04_List() {
        log.info("=== 测试4：分页查询关联列表 ===");

        List<ProcedurePartLinkVO> list = procedurePartLinkService.list(1, 1000);

        assertNotNull(list);
        assertTrue(list.size() > 0);

        boolean found = list.stream()
            .anyMatch(vo -> testLinkId.equals(vo.getId()));
        assertTrue(found, "列表中应该包含刚创建的关联");

        log.info("查询成功，共 {} 个关联", list.size());
    }

    @Test
    @Order(5)
    void test05_Update() {
        log.info("=== 测试5：更新关联 ===");

        ProcedurePartLinkCreateDTO updateDTO = new ProcedurePartLinkCreateDTO();
        updateDTO.setProcedure(testProcedureId);
        updateDTO.setPart1(testPartId);
        updateDTO.setRole("Input");
        updateDTO.setQuantity(java.math.BigDecimal.valueOf(10));
        updateDTO.setUom(testUnitId);
        updateDTO.setIsMandatory(false);

        ProcedurePartLinkVO result = procedurePartLinkService.update(testLinkId, updateDTO);

        assertNotNull(result);
        assertEquals(testLinkId, result.getId());
        assertEquals(10, result.getQuantity());
        assertEquals(false, result.getIsMandatory());
        assertEquals("Input", result.getRole());

        log.info("更新成功: {}", result);
    }

    @Test
    @Order(6)
    void test06_Delete() {
        log.info("=== 测试6：删除关联 ===");

        procedurePartLinkService.delete(testLinkId);

        // 验证删除成功
        Exception exception = assertThrows(Exception.class, () -> {
            procedurePartLinkService.getById(testLinkId);
        });

        log.info("删除成功，查询已删除记录抛出异常: {}", exception.getMessage());
    }

    @Test
    @Order(7)
    void test07_Cleanup() {
        log.info("=== 测试7：清理所有测试数据 ===");

        // 删除顺序：从依赖最少的开始

        // 1. 删除物料
        try {
            partService.delete(testPartMasterId);
            log.info("删除物料成功");
        } catch (Exception e) {
            log.warn("删除物料失败: {}", e.getMessage());
        }

        // 2. 删除工序
        try {
            workingProcedureService.delete(testProcedureId);
            log.info("删除工序成功");
        } catch (Exception e) {
            log.warn("删除工序失败: {}", e.getMessage());
        }

        // 3. 删除设备
        try {
            equipmentService.delete(testEquipmentProductionId);
            equipmentService.delete(testEquipmentInspectionId);
            log.info("删除设备成功");
        } catch (Exception e) {
            log.warn("删除设备失败: {}", e.getMessage());
        }

        // 4. 删除位置
        try {
            locationService.delete(testLocationId);
            log.info("删除位置成功");
        } catch (Exception e) {
            log.warn("删除位置失败: {}", e.getMessage());
        }

        // 5. 删除员工
        try {
            employeeService.delete(testEmployeeId);
            log.info("删除员工成功");
        } catch (Exception e) {
            log.warn("删除员工失败: {}", e.getMessage());
        }

        // 6. 删除部门
        try {
            departmentService.delete(testDepartmentId);
            log.info("删除部门成功");
        } catch (Exception e) {
            log.warn("删除部门失败: {}", e.getMessage());
        }

        // 7. 删除设备型号
        try {
            equipmentModelService.delete(testModelId);
            log.info("删除设备型号成功");
        } catch (Exception e) {
            log.warn("删除设备型号失败: {}", e.getMessage());
        }

        // 8. 删除设备分类
        try {
            equipmentClassficationService.delete(testEquipmentCategoryId);
            log.info("删除设备分类成功");
        } catch (Exception e) {
            log.warn("删除设备分类失败: {}", e.getMessage());
        }

        // 9. 删除物料分类
        try {
            partClassficationService.delete(testPartCategoryId);
            log.info("删除物料分类成功");
        } catch (Exception e) {
            log.warn("删除物料分类失败: {}", e.getMessage());
        }

        // 10. 删除往来单位
        try {
            businessPartnerService.delete(testSupplierId);
            businessPartnerService.delete(testManufacturerId);
            log.info("删除往来单位成功");
        } catch (Exception e) {
            log.warn("删除往来单位失败: {}", e.getMessage());
        }

        // 11. 删除计量单位
        try {
            unitService.delete(testUnitId);
            log.info("删除计量单位成功");
        } catch (Exception e) {
            log.warn("删除计量单位失败: {}", e.getMessage());
        }

        log.info("清理完成！");
    }
}
