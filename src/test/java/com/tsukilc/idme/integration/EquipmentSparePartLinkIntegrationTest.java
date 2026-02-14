package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.*;
import com.tsukilc.idme.service.*;
import com.tsukilc.idme.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EquipmentSparePartLink（设备-备件关联）集成测试
 * 依赖：Equipment + Part（版本对象）
 *
 * 测试覆盖：
 * 1. 数据准备（13个依赖对象）
 * 2. 创建关联
 * 3. 按ID查询
 * 4. 分页列表查询
 * 5. 按设备查询
 * 6. 按物料查询
 * 7. 更新关联
 * 8. 删除关联
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EquipmentSparePartLinkIntegrationTest {

    @Autowired
    private EquipmentSparePartLinkService equipmentSparePartLinkService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private PartService partService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private PartClassficationService partClassficationService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LocationService locationService;

    // 测试数据ID
    private static String testLinkId;
    private static String testEquipmentId;
    private static String testPartId;
    private static String testUnitId;

    // 依赖数据ID
    private static String testSupplierPartnerId;
    private static String testPartClassId;
    private static String testManufacturerPartnerId;
    private static String testEquipClassId;
    private static String testModelId;
    private static String testDeptId;
    private static String testEmployeeId;
    private static String testLocationId;

    @Test
    @Order(1)
    void test01_PrepareData() {
        log.info("=== 开始准备依赖数据 ===");

        // 1. Unit（计量单位）
        UnitCreateDTO unitDTO = new UnitCreateDTO();
        unitDTO.setUnitName("SPARE-LINK-UNIT-" + System.currentTimeMillis());
        unitDTO.setUnitDisplayName("SPARE-LINK");
        UnitVO unit = unitService.create(unitDTO);
        testUnitId = unit.getId();
        assertNotNull(testUnitId, "计量单位创建失败");
        log.info("✅ 计量单位创建成功，ID: {}", testUnitId);

        // 2. BusinessPartner（供应商）
        BusinessPartnerCreateDTO supplierDTO = new BusinessPartnerCreateDTO();
        supplierDTO.setPartnerCode("SPARE-SUPPLIER-" + System.currentTimeMillis());
        supplierDTO.setPartnerName("备件供应商");
        supplierDTO.setPartnerType("Aerospace");
        BusinessPartnerVO supplier = businessPartnerService.create(supplierDTO);
        testSupplierPartnerId = supplier.getId();
        assertNotNull(testSupplierPartnerId, "供应商创建失败");
        log.info("✅ 供应商创建成功，ID: {}", testSupplierPartnerId);

        // 3. PartClassfication（物料分类）
        PartClassficationCreateDTO partClassDTO = new PartClassficationCreateDTO();
        partClassDTO.setPartClassName("SPARE-PART-CLASS-" + System.currentTimeMillis());
        PartClassficationVO partClass = partClassficationService.create(partClassDTO);
        testPartClassId = partClass.getId();
        assertNotNull(testPartClassId, "物料分类创建失败");
        log.info("✅ 物料分类创建成功，ID: {}", testPartClassId);

        // 4. BusinessPartner（设备制造商）
        BusinessPartnerCreateDTO manuDTO = new BusinessPartnerCreateDTO();
        manuDTO.setPartnerCode("SPARE-MANU-" + System.currentTimeMillis());
        manuDTO.setPartnerName("设备制造商");
        manuDTO.setPartnerType("Machinery");
        BusinessPartnerVO manufacturer = businessPartnerService.create(manuDTO);
        testManufacturerPartnerId = manufacturer.getId();
        assertNotNull(testManufacturerPartnerId, "制造商创建失败");
        log.info("✅ 制造商创建成功，ID: {}", testManufacturerPartnerId);

        // 5. EquipmentClassfication（设备分类）
        EquipmentClassficationCreateDTO equipClassDTO = new EquipmentClassficationCreateDTO();
        equipClassDTO.setEquipmentClassName("SPARE-EQUIP-CLASS-" + System.currentTimeMillis());
        EquipmentClassficationVO equipClass = equipmentClassficationService.create(equipClassDTO);
        testEquipClassId = equipClass.getId();
        assertNotNull(testEquipClassId, "设备分类创建失败");
        log.info("✅ 设备分类创建成功，ID: {}", testEquipClassId);

        // 6. EquipmentModel（设备型号）
        EquipmentModelCreateDTO modelDTO = new EquipmentModelCreateDTO();
        modelDTO.setModelName("SPARE-MODEL-" + System.currentTimeMillis());
        modelDTO.setManufacturer(testManufacturerPartnerId);
        modelDTO.setCategory(testEquipClassId);
        String modelId = equipmentModelService.create(modelDTO);
        testModelId = modelId;
        assertNotNull(testModelId, "设备型号创建失败");
        log.info("✅ 设备型号创建成功，ID: {}", testModelId);

        // 7. Department（部门）
        DepartmentCreateDTO deptDTO = new DepartmentCreateDTO();
        deptDTO.setDeptCode("SPARE-DEPT-" + System.currentTimeMillis());
        deptDTO.setDeptName("备件测试部门");
        DepartmentVO dept = departmentService.create(deptDTO);
        testDeptId = dept.getId();
        assertNotNull(testDeptId, "部门创建失败");
        log.info("✅ 部门创建成功，ID: {}", testDeptId);

        // 8. Employee（员工）
        EmployeeCreateDTO empDTO = new EmployeeCreateDTO();
        empDTO.setEmployeeNo("SPARE-EMP-" + System.currentTimeMillis());
        empDTO.setEmployeeName("备件测试员工");
        empDTO.setDept(testDeptId);
        EmployeeVO emp = employeeService.create(empDTO);
        testEmployeeId = emp.getId();
        assertNotNull(testEmployeeId, "员工创建失败");
        log.info("✅ 员工创建成功，ID: {}", testEmployeeId);

        // 9. Location（位置）
        LocationCreateDTO locDTO = new LocationCreateDTO();
        locDTO.setLocationCode("SPARE-LOC-" + System.currentTimeMillis());
        locDTO.setLocationName("备件仓库");
        locDTO.setLocationType("Plant");
        locDTO.setManager(testEmployeeId);
        LocationVO loc = locationService.create(locDTO);
        testLocationId = loc.getId();
        assertNotNull(testLocationId, "位置创建失败");
        log.info("✅ 位置创建成功，ID: {}", testLocationId);

        // 10. Equipment（设备）
        EquipmentCreateDTO equipDTO = new EquipmentCreateDTO();
        equipDTO.setEquipmentCode("SPARE-EQUIP-" + System.currentTimeMillis());
        equipDTO.setEquipmentName("测试设备");
        equipDTO.setEquipmentModelRef(testModelId);
        equipDTO.setManufacturerName(testManufacturerPartnerId);
        equipDTO.setSupplierName(testManufacturerPartnerId);  // SDK必填
        equipDTO.setLocationRef(testLocationId);  // SDK必填
        equipDTO.setCategory(testEquipClassId);  // SDK必填
        equipDTO.setStatus("Standby");
        String equipId = equipmentService.create(equipDTO);
        testEquipmentId = equipId;
        assertNotNull(testEquipmentId, "设备创建失败");
        log.info("✅ 设备创建成功，ID: {}", testEquipmentId);

        // 11. Part（物料，版本对象）
        PartCreateDTO partDTO = new PartCreateDTO();
        partDTO.setPartNumber("SPARE-PART-" + System.currentTimeMillis());
        partDTO.setPartName("测试备件");
        partDTO.setSupplierName(testSupplierPartnerId);
        partDTO.setCategory(testPartClassId);
        partDTO.setUnit(testUnitId);
        PartVO part = partService.create(partDTO);
        testPartId = part.getId();
        assertNotNull(testPartId, "物料创建失败");
        log.info("✅ 物料创建成功，ID: {}", testPartId);

        log.info("=== 依赖数据准备完成 ===");
        log.info("设备ID: {}", testEquipmentId);
        log.info("备件ID: {}", testPartId);
        log.info("单位ID: {}", testUnitId);
    }

    @Test
    @Order(2)
    void test02_CreateLink() {
        log.info("=== 测试创建设备-备件关联 ===");

        EquipmentSparePartLinkCreateDTO dto = new EquipmentSparePartLinkCreateDTO();
        dto.setEquipment(testEquipmentId);
        dto.setSparePart(testPartId);
        dto.setQuantity(10);
        dto.setUnit(testUnitId);  // 使用单位ID
        dto.setIsCritical(true);
        dto.setReplacementCycleDays(30);
        dto.setRemarks("测试备件关联");

        EquipmentSparePartLinkVO result = equipmentSparePartLinkService.create(dto);

        assertNotNull(result, "创建返回结果为空");
        assertNotNull(result.getId(), "创建返回ID为空");
        assertEquals(testEquipmentId, result.getEquipment(), "设备ID不匹配");
        assertEquals(testPartId, result.getSparePart(), "备件ID不匹配");
        assertEquals(10, result.getQuantity(), "数量不匹配");
        assertEquals(testUnitId, result.getUnit(), "单位不匹配");
        assertTrue(result.getIsCritical(), "关键备件标志不匹配");
        assertEquals(30, result.getReplacementCycleDays(), "更换周期不匹配");

        testLinkId = result.getId();
        log.info("✅ 创建成功，关联ID: {}", testLinkId);
    }

    @Test
    @Order(3)
    void test03_GetById() {
        log.info("=== 测试按ID查询 ===");

        EquipmentSparePartLinkVO result = equipmentSparePartLinkService.getById(testLinkId);

        assertNotNull(result, "查询结果为空");
        assertEquals(testLinkId, result.getId(), "ID不匹配");
        assertEquals(testEquipmentId, result.getEquipment(), "设备ID不匹配");
        assertEquals(testPartId, result.getSparePart(), "备件ID不匹配");

        log.info("✅ 查询成功: {}", result);
    }

    @Test
    @Order(4)
    void test04_List() {
        log.info("=== 测试分页列表查询 ===");

        java.util.List<EquipmentSparePartLinkVO> results = equipmentSparePartLinkService.list(1, 1000);

        assertNotNull(results, "查询结果为空");
        assertTrue(results.size() > 0, "查询结果列表为空");

        boolean found = results.stream()
                .anyMatch(vo -> testLinkId.equals(vo.getId()));
        assertTrue(found, "列表中未找到刚创建的关联");

        log.info("✅ 分页查询成功，共 {} 条记录", results.size());
    }

    @Test
    @Order(5)
    void test05_GetByEquipment() {
        log.info("=== 测试按设备查询 ===");

        java.util.List<EquipmentSparePartLinkVO> results =
            equipmentSparePartLinkService.getByEquipment(testEquipmentId);

        assertNotNull(results, "查询结果为空");
        assertTrue(results.size() > 0, "未找到设备的备件关联");

        boolean found = results.stream()
                .anyMatch(vo -> testLinkId.equals(vo.getId()));
        assertTrue(found, "结果中未找到测试关联");

        log.info("✅ 按设备查询成功，共 {} 条记录", results.size());
    }

    @Test
    @Order(6)
    void test06_GetByPart() {
        log.info("=== 测试按物料查询（备件使用情况）===");

        java.util.List<EquipmentSparePartLinkVO> results =
            equipmentSparePartLinkService.getByPart(testPartId);

        assertNotNull(results, "查询结果为空");
        assertTrue(results.size() > 0, "未找到备件的使用情况");

        boolean found = results.stream()
                .anyMatch(vo -> testLinkId.equals(vo.getId()));
        assertTrue(found, "结果中未找到测试关联");

        log.info("✅ 按物料查询成功，共 {} 条记录", results.size());
    }

    @Test
    @Order(7)
    void test07_Update() {
        log.info("=== 测试更新关联 ===");

        EquipmentSparePartLinkCreateDTO dto = new EquipmentSparePartLinkCreateDTO();
        dto.setEquipment(testEquipmentId);
        dto.setSparePart(testPartId);
        dto.setQuantity(20); // 修改数量
        dto.setUnit(testUnitId); // 保持单位ID
        dto.setIsCritical(false); // 修改关键标志
        dto.setReplacementCycleDays(60); // 修改周期
        dto.setRemarks("已更新");

        EquipmentSparePartLinkVO result = equipmentSparePartLinkService.update(testLinkId, dto);

        assertNotNull(result, "更新返回结果为空");
        assertEquals(testLinkId, result.getId(), "ID不匹配");
        assertEquals(20, result.getQuantity(), "数量未更新");
        assertEquals(testUnitId, result.getUnit(), "单位未更新");
        assertFalse(result.getIsCritical(), "关键标志未更新");
        assertEquals(60, result.getReplacementCycleDays(), "更换周期未更新");

        log.info("✅ 更新成功: {}", result);
    }

    @Test
    @Order(8)
    void test08_Delete() {
        log.info("=== 测试删除关联 ===");

        equipmentSparePartLinkService.delete(testLinkId);

        Exception exception = assertThrows(Exception.class, () -> {
            equipmentSparePartLinkService.getById(testLinkId);
        });

        log.info("✅ 删除成功，查询已删除记录抛出异常: {}", exception.getMessage());
    }
}
