package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.*;
import com.tsukilc.idme.entity.PlanProcedureLink;
import com.tsukilc.idme.service.*;
import com.tsukilc.idme.vo.PartVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlanProcedureLink（计划-工序关联）模块集成测试
 * 依赖：WorkingPlan（版本对象）, WorkingProcedure
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlanProcedureLinkIntegrationTest {

    @Autowired
    private PlanProcedureLinkService planProcedureLinkService;

    @Autowired
    private WorkingPlanService workingPlanService;

    @Autowired
    private WorkingProcedureService workingProcedureService;

    @Autowired
    private PartService partService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentModelService equipmentModelService;

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PartClassficationService partClassficationService;

    // 依赖数据ID
    private static String testUnitId;
    private static String testSupplierId;
    private static String testPartCategoryId;
    private static String testPartId;
    private static String testManufacturerId;
    private static String testEquipmentClassId;
    private static String testModelId;
    private static String testDeptId;
    private static String testManagerId;
    private static String testLocationId;
    private static String testEquipmentId;
    private static String testEmployeeId;
    private static String testProcedureId1;
    private static String testProcedureId2;
    private static String testWorkingPlanId;

    // 测试数据ID
    private static String testLinkId1;
    private static String testLinkId2;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建所有依赖数据")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建所有依赖数据 ==========");

        // 1. 创建计量单位
        UnitCreateDTO unitDto = new UnitCreateDTO();
        unitDto.setUnitName("个_PLAN_LINK_TEST");
        unitDto.setUnitDisplayName("个");
        testUnitId = unitService.create(unitDto).getId();
        log.info("计量单位创建成功，ID: {}", testUnitId);

        // 2. 创建供应商
        BusinessPartnerCreateDTO supplierDto = new BusinessPartnerCreateDTO();
        supplierDto.setPartnerCode("SUPPLIER_PLAN_LINK_" + System.currentTimeMillis());
        supplierDto.setPartnerName("测试供应商_计划关联");
        supplierDto.setPartnerType("Parts");
        testSupplierId = businessPartnerService.create(supplierDto).getId();
        log.info("供应商创建成功，ID: {}", testSupplierId);

        // 3. 创建物料分类
        PartClassficationCreateDTO partCategoryDto = new PartClassficationCreateDTO();
        partCategoryDto.setPartClassName("标准件_PLAN_LINK");
        testPartCategoryId = partClassficationService.create(partCategoryDto).getId();
        log.info("物料分类创建成功，ID: {}", testPartCategoryId);

        // 4. 创建Part（版本对象）- 作为WorkingPlan.productPart
        PartCreateDTO partDto = new PartCreateDTO();
        partDto.setPartNumber("PART_PLAN_LINK_" + System.currentTimeMillis());
        partDto.setPartName("测试物料_计划关联");
        partDto.setStockQty(100);
        partDto.setUnit(testUnitId);
        partDto.setSupplierName(testSupplierId);
        partDto.setCategory(testPartCategoryId);
        PartVO partVO = partService.create(partDto);
        testPartId = partVO.getMasterId();  // 使用 masterId 而不是 id，因为 WorkingPlan 引用的是 PartMaster
        log.info("物料创建成功，Part ID: {}, MasterID: {}", partVO.getId(), testPartId);

        // 5. 创建生产厂家
        BusinessPartnerCreateDTO manufacturerDto = new BusinessPartnerCreateDTO();
        manufacturerDto.setPartnerCode("MANUFACTURER_PLAN_LINK_" + System.currentTimeMillis());
        manufacturerDto.setPartnerName("测试厂家_计划关联");
        manufacturerDto.setPartnerType("Machinery");
        testManufacturerId = businessPartnerService.create(manufacturerDto).getId();
        log.info("生产厂家创建成功，ID: {}", testManufacturerId);

        // 6. 创建设备分类
        EquipmentClassficationCreateDTO equipClassDto = new EquipmentClassficationCreateDTO();
        equipClassDto.setEquipmentClassName("测试分类_计划关联");
        testEquipmentClassId = equipmentClassficationService.create(equipClassDto).getId();
        log.info("设备分类创建成功，ID: {}", testEquipmentClassId);

        // 7. 创建设备型号
        EquipmentModelCreateDTO modelDto = new EquipmentModelCreateDTO();
        modelDto.setModelCode("MODEL_PLAN_LINK_" + System.currentTimeMillis());
        modelDto.setModelName("测试型号_计划关联");
        modelDto.setManufacturer(testManufacturerId);
        modelDto.setCategory(testEquipmentClassId);
        testModelId = equipmentModelService.create(modelDto);
        log.info("设备型号创建成功，ID: {}", testModelId);

        // 8. 创建部门
        DepartmentCreateDTO deptDto = new DepartmentCreateDTO();
        deptDto.setDeptCode("DEPT_PLAN_LINK_" + System.currentTimeMillis());
        deptDto.setDeptName("计划关联测试部门");
        deptDto.setManager("测试经理");
        testDeptId = departmentService.create(deptDto).getId();
        log.info("部门创建成功，ID: {}", testDeptId);

        // 9. 创建员工（作为Location.manager）
        EmployeeCreateDTO mgrDto = new EmployeeCreateDTO();
        mgrDto.setEmployeeNo("EMP_PLAN_LINK_MGR_" + System.currentTimeMillis());
        mgrDto.setEmployeeName("计划关联管理员");
        mgrDto.setDept(testDeptId);
        mgrDto.setJobTitle("管理员");
        mgrDto.setStatus("在职");
        mgrDto.setHireDate(LocalDate.of(2024, 1, 1));
        testManagerId = employeeService.create(mgrDto).getId();
        log.info("员工创建成功，ID: {}", testManagerId);

        // 10. 创建位置
        LocationCreateDTO locDto = new LocationCreateDTO();
        locDto.setLocationCode("LOC_PLAN_LINK_" + System.currentTimeMillis());
        locDto.setLocationName("计划关联测试位置");
        locDto.setLocationType("Workshop");
        locDto.setManager(testManagerId);
        testLocationId = locationService.create(locDto).getId();
        log.info("位置创建成功，ID: {}", testLocationId);

        // 11. 创建设备
        EquipmentCreateDTO equipDto = new EquipmentCreateDTO();
        equipDto.setEquipmentCode("EQUIP_PLAN_LINK_" + System.currentTimeMillis());
        equipDto.setEquipmentName("测试设备_计划关联");
        equipDto.setManufacturerName(testManufacturerId);
        equipDto.setSupplierName(testSupplierId);
        equipDto.setEquipmentModelRef(testModelId);
        equipDto.setLocationRef(testLocationId);
        equipDto.setCategory(testEquipmentClassId);
        equipDto.setStatus("Standby");
        testEquipmentId = equipmentService.create(equipDto);
        log.info("设备创建成功，ID: {}", testEquipmentId);

        // 12. 创建员工（作为Procedure.operatorRef）
        EmployeeCreateDTO empDto = new EmployeeCreateDTO();
        empDto.setEmployeeNo("EMP_PLAN_LINK_OP_" + System.currentTimeMillis());
        empDto.setEmployeeName("计划关联操作员");
        empDto.setDept(testDeptId);
        empDto.setJobTitle("操作员");
        empDto.setStatus("在职");
        empDto.setHireDate(LocalDate.of(2024, 1, 1));
        testEmployeeId = employeeService.create(empDto).getId();
        log.info("操作员创建成功，ID: {}", testEmployeeId);

        // 13. 创建工序1
        WorkingProcedureCreateDTO proc1Dto = new WorkingProcedureCreateDTO();
        proc1Dto.setProcedureCode("PROC_PLAN_LINK_1_" + System.currentTimeMillis());
        proc1Dto.setProcedureName("测试工序1_计划关联");
        proc1Dto.setSteps("加工步骤");
        proc1Dto.setMainProductionEquipment(testEquipmentId);
        proc1Dto.setMainInspectionEquipment(testEquipmentId);
        proc1Dto.setOperatorRef(testEmployeeId);
        proc1Dto.setStatus("InProgress");
        testProcedureId1 = workingProcedureService.create(proc1Dto);
        log.info("工序1创建成功，ID: {}", testProcedureId1);

        // 14. 创建工序2
        WorkingProcedureCreateDTO proc2Dto = new WorkingProcedureCreateDTO();
        proc2Dto.setProcedureCode("PROC_PLAN_LINK_2_" + System.currentTimeMillis());
        proc2Dto.setProcedureName("测试工序2_计划关联");
        proc2Dto.setSteps("装配步骤");
        proc2Dto.setMainProductionEquipment(testEquipmentId);
        proc2Dto.setMainInspectionEquipment(testEquipmentId);
        proc2Dto.setOperatorRef(testEmployeeId);
        proc2Dto.setStatus("InProgress");
        testProcedureId2 = workingProcedureService.create(proc2Dto);
        log.info("工序2创建成功，ID: {}", testProcedureId2);

        // 15. 创建WorkingPlan（版本对象）
        WorkingPlanCreateDTO planDto = new WorkingPlanCreateDTO();
        planDto.setPlanCode("PLAN_LINK_" + System.currentTimeMillis());
        planDto.setPlanName("测试工艺路线_计划关联");
        planDto.setBusinessVersion("1.0");
        planDto.setProductPart(testPartId);
        planDto.setOperatorRef(testEmployeeId);  // SDK必填字段
        planDto.setDescription("集成测试工艺路线");
        testWorkingPlanId = workingPlanService.create(planDto);
        log.info("工艺路线创建成功，ID: {}", testWorkingPlanId);

        log.info("========== 所有依赖数据准备完成 ==========");
    }

    @Test
    @Order(2)
    @DisplayName("2. 批量创建工序关联")
    void test02_BatchCreate() {
        log.info("========== 测试：批量创建工序关联 ==========");

        PlanProcedureLinkBatchDTO batchDto = new PlanProcedureLinkBatchDTO();
        batchDto.setPlanId(testWorkingPlanId);

        List<PlanProcedureLinkBatchDTO.ProcedureItem> items = new ArrayList<>();

        PlanProcedureLinkBatchDTO.ProcedureItem item1 = new PlanProcedureLinkBatchDTO.ProcedureItem();
        item1.setProcedureId(testProcedureId1);
        item1.setSequenceNo(1);
        item1.setStandardDurationMin(60);
        item1.setRequirement("精加工要求");
        items.add(item1);

        PlanProcedureLinkBatchDTO.ProcedureItem item2 = new PlanProcedureLinkBatchDTO.ProcedureItem();
        item2.setProcedureId(testProcedureId2);
        item2.setSequenceNo(2);
        item2.setStandardDurationMin(30);
        item2.setRequirement("装配要求");
        items.add(item2);

        batchDto.setProcedures(items);

        List<String> ids = planProcedureLinkService.batchCreate(batchDto);

        assertNotNull(ids);
        assertEquals(2, ids.size());

        testLinkId1 = ids.get(0);
        testLinkId2 = ids.get(1);

        log.info("批量创建成功，ID: {}, {}", testLinkId1, testLinkId2);
    }

    @Test
    @Order(3)
    @DisplayName("3. 查询工序关联详情")
    void test03_GetById() {
        log.info("========== 测试：查询工序关联详情 ==========");

        PlanProcedureLink link = planProcedureLinkService.getById(testLinkId1);

        assertNotNull(link);
        assertEquals(testLinkId1, link.getId());
        assertEquals(1, link.getSequenceNo());

        // standardDurationMin是Map格式 {value: "60.0000000", precision: null}
        assertNotNull(link.getStandardDurationMin());
        assertTrue(link.getStandardDurationMin() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> durationMap = (Map<String, Object>) link.getStandardDurationMin();
        assertNotNull(durationMap.get("value"));

        assertEquals("精加工要求", link.getRequirement());

        log.info("查询成功，工序关联: {}", link);
    }

    @Test
    @Order(4)
    @DisplayName("4. 更新工序顺序")
    void test04_UpdateSequence() {
        log.info("========== 测试：更新工序顺序 ==========");

        planProcedureLinkService.updateSequence(testLinkId1, 10);

        // 验证更新成功
        PlanProcedureLink updated = planProcedureLinkService.getById(testLinkId1);
        assertEquals(10, updated.getSequenceNo());

        log.info("更新成功，新顺序号: {}", updated.getSequenceNo());
    }

    @Test
    @Order(5)
    @DisplayName("5. 按工艺路线查询工序列表")
    void test05_ListByPlan() {
        log.info("========== 测试：按工艺路线查询工序列表 ==========");

        List<PlanProcedureLink> links = planProcedureLinkService.getByPlan(testWorkingPlanId);

        assertNotNull(links);
        assertTrue(links.size() >= 2, "应该至少有2条关联记录");

        log.info("查询成功，共 {} 条关联", links.size());
    }

    @Test
    @Order(6)
    @DisplayName("6. 删除工序关联")
    void test06_Delete() {
        log.info("========== 测试：删除工序关联 ==========");

        planProcedureLinkService.delete(testLinkId2);

        // 验证删除成功 - 查询应该失败或抛出异常
        assertThrows(Exception.class, () -> {
            planProcedureLinkService.getById(testLinkId2);
        });

        log.info("删除成功，ID: {}", testLinkId2);
    }

    @Test
    @Order(99)
    @DisplayName("99. 清理：删除所有测试数据")
    void test99_Cleanup() {
        log.info("========== 清理：删除所有测试数据 ==========");

        try {
            // 删除剩余的工序关联
            if (testLinkId1 != null) {
                planProcedureLinkService.delete(testLinkId1);
                log.info("已删除工序关联: {}", testLinkId1);
            }

            // 删除工艺路线
            if (testWorkingPlanId != null) {
                workingPlanService.delete(testWorkingPlanId);
                log.info("已删除工艺路线: {}", testWorkingPlanId);
            }

            // 删除工序
            if (testProcedureId1 != null) {
                workingProcedureService.delete(testProcedureId1);
                log.info("已删除工序1: {}", testProcedureId1);
            }
            if (testProcedureId2 != null) {
                workingProcedureService.delete(testProcedureId2);
                log.info("已删除工序2: {}", testProcedureId2);
            }

            // 删除设备
            if (testEquipmentId != null) {
                equipmentService.delete(testEquipmentId);
                log.info("已删除设备: {}", testEquipmentId);
            }

            // 删除物料
            if (testPartId != null) {
                partService.delete(testPartId);
                log.info("已删除物料: {}", testPartId);
            }

            // 删除员工
            if (testEmployeeId != null) {
                employeeService.delete(testEmployeeId);
                log.info("已删除操作员: {}", testEmployeeId);
            }
            if (testManagerId != null) {
                employeeService.delete(testManagerId);
                log.info("已删除经理: {}", testManagerId);
            }

            // 删除位置
            if (testLocationId != null) {
                locationService.delete(testLocationId);
                log.info("已删除位置: {}", testLocationId);
            }

            // 删除部门
            if (testDeptId != null) {
                departmentService.delete(testDeptId);
                log.info("已删除部门: {}", testDeptId);
            }

            // 删除设备型号
            if (testModelId != null) {
                equipmentModelService.delete(testModelId);
                log.info("已删除设备型号: {}", testModelId);
            }

            // 删除设备分类
            if (testEquipmentClassId != null) {
                equipmentClassficationService.delete(testEquipmentClassId);
                log.info("已删除设备分类: {}", testEquipmentClassId);
            }

            // 删除物料分类
            if (testPartCategoryId != null) {
                partClassficationService.delete(testPartCategoryId);
                log.info("已删除物料分类: {}", testPartCategoryId);
            }

            // 删除供应商和厂家
            if (testSupplierId != null) {
                businessPartnerService.delete(testSupplierId);
                log.info("已删除供应商: {}", testSupplierId);
            }
            if (testManufacturerId != null) {
                businessPartnerService.delete(testManufacturerId);
                log.info("已删除生产厂家: {}", testManufacturerId);
            }

            // 删除计量单位
            if (testUnitId != null) {
                unitService.delete(testUnitId);
                log.info("已删除计量单位: {}", testUnitId);
            }

            log.info("========== 所有测试数据清理完成 ==========");
        } catch (Exception e) {
            log.warn("清理过程中出现异常（可忽略）: {}", e.getMessage());
        }
    }
}
