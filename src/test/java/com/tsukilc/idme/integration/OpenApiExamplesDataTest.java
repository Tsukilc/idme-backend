package com.tsukilc.idme.integration;

import com.tsukilc.idme.dao.*;
import com.tsukilc.idme.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OpenAPI 示例数据生成集成测试
 * 目的：创建固定ID的示例对象，用于 openapi.yaml 的 example 字段
 *
 * ⚠️ 重要：SDK要求ID必须是纯数字格式！
 *
 * ID分段规则：
 * - 10亿段：Unit
 * - 20亿段：BusinessPartner
 * - 30亿段：分类
 * - 40亿段：Department/Employee
 * - 50亿段：Model/Location
 * - 60亿段：Equipment
 * - 70亿段：Part
 * - 80亿段：Procedure/Plan
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenApiExamplesDataTest {

    // ========== DAO注入 ==========
    @Autowired private UnitDao unitDao;
    @Autowired private BusinessPartnerDao businessPartnerDao;
    @Autowired private EquipmentClassficationDao equipmentClassficationDao;
    @Autowired private PartClassficationDao partClassficationDao;
    @Autowired private DepartmentDao departmentDao;
    @Autowired private EmployeeDao employeeDao;
    @Autowired private EquipmentModelDao equipmentModelDao;
    @Autowired private LocationDao locationDao;
    @Autowired private EquipmentDao equipmentDao;
    @Autowired private PartDao partDao;
    @Autowired private WorkingProcedureDao workingProcedureDao;
    @Autowired private WorkingPlanDao workingPlanDao;

    // ========== 固定ID常量（纯数字） ==========

    // Level 0 - 基础主数据
    private static final String UNIT_DEMO_ID               = "1000000001";
    private static final String MANUFACTURER_DEMO_ID       = "2000000001";
    private static final String SUPPLIER_DEMO_ID           = "2000000002";
    private static final String EQUIP_CLASS_DEMO_ID        = "3000000001";
    private static final String PART_CLASS_DEMO_ID         = "3000000002";

    // Level 1
    private static final String DEPT_DEMO_ID               = "4000000001";
    private static final String EMPLOYEE_DEMO_ID           = "4000000002";

    // Level 2
    private static final String EQUIP_MODEL_DEMO_ID        = "5000000001";
    private static final String LOCATION_DEMO_ID           = "5000000002";

    // Level 3
    private static final String EQUIPMENT_PROD_DEMO_ID     = "6000000001";  // 生产设备
    private static final String EQUIPMENT_INSP_DEMO_ID     = "6000000002";  // 检测设备
    private static final String PART_DEMO_ID               = "7000000001";
    // Part Master ID 由SDK自动生成，无法指定

    // Level 4
    private static final String PROCEDURE_DEMO_ID          = "8000000001";
    private static final String PLAN_DEMO_ID               = "8000000002";

    // 辅助：记录Part的实际master ID（运行时获取）
    private static String partMasterDemoId;

    // ========== 测试方法 ==========

    @Test
    @Order(0)
    @DisplayName("清理旧的示例数据（如果存在）")
    void test00_CleanupOldData() {
        log.info("========== 清理旧的示例数据 ==========");

        // Level 4 清理
        tryDelete(() -> workingPlanDao.delete(PLAN_DEMO_ID), "WorkingPlan");
        tryDelete(() -> workingProcedureDao.delete(PROCEDURE_DEMO_ID), "WorkingProcedure");

        // Level 3 清理
        tryDelete(() -> partDao.delete(PART_DEMO_ID), "Part");
        tryDelete(() -> equipmentDao.delete(EQUIPMENT_INSP_DEMO_ID), "Equipment (检测)");
        tryDelete(() -> equipmentDao.delete(EQUIPMENT_PROD_DEMO_ID), "Equipment (生产)");

        // Level 2 清理
        tryDelete(() -> locationDao.delete(LOCATION_DEMO_ID), "Location");
        tryDelete(() -> equipmentModelDao.delete(EQUIP_MODEL_DEMO_ID), "EquipmentModel");

        // Level 1 清理
        tryDelete(() -> employeeDao.delete(EMPLOYEE_DEMO_ID), "Employee");
        tryDelete(() -> departmentDao.delete(DEPT_DEMO_ID), "Department");

        // Level 0 清理
        tryDelete(() -> partClassficationDao.delete(PART_CLASS_DEMO_ID), "PartClassfication");
        tryDelete(() -> equipmentClassficationDao.delete(EQUIP_CLASS_DEMO_ID), "EquipmentClassfication");
        tryDelete(() -> businessPartnerDao.delete(SUPPLIER_DEMO_ID), "Supplier");
        tryDelete(() -> businessPartnerDao.delete(MANUFACTURER_DEMO_ID), "Manufacturer");
        tryDelete(() -> unitDao.delete(UNIT_DEMO_ID), "Unit");

        log.info("========== 旧数据清理完成 ==========");
    }

    private void tryDelete(Runnable deleteAction, String entityName) {
        try {
            deleteAction.run();
            log.info("已删除旧的 {}", entityName);
        } catch (Exception e) {
            log.debug("{} 不存在或删除失败: {}", entityName, e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Level 0 - 创建基础主数据（Unit, BusinessPartner, 分类）")
    void test01_CreateLevel0() {
        log.info("========== Level 0: 创建基础主数据 ==========");

        // 1. Unit（计量单位）
        log.info("1. 创建 Unit...");
        Unit unit = new Unit();
        unit.setId(UNIT_DEMO_ID);
        unit.setUnitName("件");
        unit.setUnitDisplayName("piece");
        unit.setUnitCategory("数量");
        unit.setUnitFactor("1.0");
        unit.setMesurementSystem("公制");

        Unit createdUnit = unitDao.create(unit);
        assertEquals(UNIT_DEMO_ID, createdUnit.getId(), "Unit ID应与请求一致");
        log.info("✅ Unit created: {}", UNIT_DEMO_ID);

        // 2. BusinessPartner - Manufacturer（生产厂家）
        log.info("2. 创建 BusinessPartner (Machinery - 机床厂)...");
        BusinessPartner manufacturer = new BusinessPartner();
        manufacturer.setId(MANUFACTURER_DEMO_ID);
        manufacturer.setPartnerCode("BP-MFG-DEMO");
        manufacturer.setPartnerName("示例机床厂");
        manufacturer.setPartnerType(new PartnerTypeRef("Machinery"));  // SDK枚举值：Machinery（机械）
        manufacturer.setPhone("010-12345678");
        manufacturer.setEmail("contact@example-mfg.com");

        BusinessPartner createdMfg = businessPartnerDao.create(manufacturer);
        assertEquals(MANUFACTURER_DEMO_ID, createdMfg.getId(), "Manufacturer ID应与请求一致");
        log.info("✅ BusinessPartner (Machinery) created: {}", MANUFACTURER_DEMO_ID);

        // 3. BusinessPartner - Supplier（供应商）
        log.info("3. 创建 BusinessPartner (Parts - 零件供应商)...");
        BusinessPartner supplier = new BusinessPartner();
        supplier.setId(SUPPLIER_DEMO_ID);
        supplier.setPartnerCode("BP-SUP-DEMO");
        supplier.setPartnerName("示例零件供应商");
        supplier.setPartnerType(new PartnerTypeRef("Parts"));  // SDK枚举值：Parts（零件）
        supplier.setPhone("021-87654321");
        supplier.setEmail("supply@example-supplier.com");

        BusinessPartner createdSup = businessPartnerDao.create(supplier);
        assertEquals(SUPPLIER_DEMO_ID, createdSup.getId(), "Supplier ID应与请求一致");
        log.info("✅ Supplier created: {}", SUPPLIER_DEMO_ID);

        // 4. EquipmentClassfication（设备分类）
        log.info("4. 创建 EquipmentClassfication...");
        EquipmentClassfication equipClass = new EquipmentClassfication();
        equipClass.setId(EQUIP_CLASS_DEMO_ID);
        equipClass.setEquipmentClassName("CNC加工中心");

        EquipmentClassfication createdEC = equipmentClassficationDao.create(equipClass);
        assertEquals(EQUIP_CLASS_DEMO_ID, createdEC.getId(), "EquipmentClassfication ID应与请求一致");
        log.info("✅ EquipmentClassfication created: {}", EQUIP_CLASS_DEMO_ID);

        // 5. PartClassfication（物料分类）
        log.info("5. 创建 PartClassfication...");
        PartClassfication partClass = new PartClassfication();
        partClass.setId(PART_CLASS_DEMO_ID);
        partClass.setPartClassName("结构件");

        PartClassfication createdPC = partClassficationDao.create(partClass);
        assertEquals(PART_CLASS_DEMO_ID, createdPC.getId(), "PartClassfication ID应与请求一致");
        log.info("✅ PartClassfication created: {}", PART_CLASS_DEMO_ID);

        log.info("========== Level 0 创建完成（5个对象）==========");
    }

    @Test
    @Order(2)
    @DisplayName("Level 1 - 创建部门和员工")
    void test02_CreateLevel1() {
        log.info("========== Level 1: 创建部门和员工 ==========");

        // 1. Department（部门）
        log.info("1. 创建 Department...");
        Department dept = new Department();
        dept.setId(DEPT_DEMO_ID);
        dept.setDeptCode("DEPT-DEMO");
        dept.setDeptName("生产部");
        dept.setManager("张三");  // 简单文本字段
        dept.setRemarks("示例部门");

        Department createdDept = departmentDao.create(dept);
        assertEquals(DEPT_DEMO_ID, createdDept.getId());
        log.info("✅ Department created: {}", DEPT_DEMO_ID);

        // 2. Employee（员工）
        log.info("2. 创建 Employee...");
        Employee emp = new Employee();
        emp.setId(EMPLOYEE_DEMO_ID);
        emp.setEmployeeNo("EMP-DEMO");
        emp.setEmployeeName("李四");
        emp.setDept(createObjectRef(DEPT_DEMO_ID));  // 引用部门
        emp.setJobTitle("操作员");
        emp.setStatus("Active");  // 枚举值（Object类型，SDK会自动转换）
        emp.setHireDate(java.time.LocalDateTime.of(2024, 1, 1, 0, 0));
        emp.setPhone("13800138000");
        emp.setEmail("lisi@example.com");

        Employee createdEmp = employeeDao.create(emp);
        assertEquals(EMPLOYEE_DEMO_ID, createdEmp.getId());
        log.info("✅ Employee created: {}", EMPLOYEE_DEMO_ID);

        log.info("========== Level 1 创建完成（2个对象）==========");
    }

    @Test
    @Order(3)
    @DisplayName("Level 2 - 创建设备型号和位置")
    void test03_CreateLevel2() {
        log.info("========== Level 2: 创建设备型号和位置 ==========");

        // 1. EquipmentModel（设备型号）
        log.info("1. 创建 EquipmentModel...");
        EquipmentModel model = new EquipmentModel();
        model.setId(EQUIP_MODEL_DEMO_ID);
        model.setModelCode("MODEL-DEMO");
        model.setModelName("CNC-5000");
        model.setManufacturer(createObjectRef(MANUFACTURER_DEMO_ID));
        model.setBrand("华为");
        model.setCategory(createObjectRef(EQUIP_CLASS_DEMO_ID));

        EquipmentModel createdModel = equipmentModelDao.create(model);
        assertEquals(EQUIP_MODEL_DEMO_ID, createdModel.getId());
        log.info("✅ EquipmentModel created: {}", EQUIP_MODEL_DEMO_ID);

        // 2. Location（位置）
        log.info("2. 创建 Location...");
        Location loc = new Location();
        loc.setId(LOCATION_DEMO_ID);
        loc.setLocationCode("LOC-DEMO");
        loc.setLocationName("一车间");
        loc.setLocationType("Workshop");  // 枚举值（Object类型）
        loc.setManager(createObjectRef(EMPLOYEE_DEMO_ID));  // SDK必填
        loc.setAddressText("示例地址");

        Location createdLoc = locationDao.create(loc);
        assertEquals(LOCATION_DEMO_ID, createdLoc.getId());
        log.info("✅ Location created: {}", LOCATION_DEMO_ID);

        log.info("========== Level 2 创建完成（2个对象）==========");
    }

    @Test
    @Order(4)
    @DisplayName("Level 3 - 创建设备和物料")
    void test04_CreateLevel3() {
        log.info("========== Level 3: 创建设备和物料 ==========");

        // 1. Equipment - 生产设备
        log.info("1. 创建 Equipment (生产设备)...");
        Equipment prodEquip = new Equipment();
        prodEquip.setId(EQUIPMENT_PROD_DEMO_ID);
        prodEquip.setEquipmentCode("EQ-PROD-DEMO");
        prodEquip.setEquipmentName("CNC加工中心A");
        prodEquip.setManufacturerName(createObjectRef(MANUFACTURER_DEMO_ID));
        prodEquip.setSupplierName(createObjectRef(SUPPLIER_DEMO_ID));
        prodEquip.setCategory(createObjectRef(EQUIP_CLASS_DEMO_ID));
        prodEquip.setEquipmentModelRef(createObjectRef(EQUIP_MODEL_DEMO_ID));  // SDK必填！
        prodEquip.setLocationRef(createObjectRef(LOCATION_DEMO_ID));  // SDK必填
        prodEquip.setStatus("Standby");  // 枚举值：Standby, InOperation, UnderMaintenance, Shutdown, Scrap
        prodEquip.setBrand("华为");
        prodEquip.setSerialNumber("SN-PROD-001");

        Equipment createdProdEquip = equipmentDao.create(prodEquip);
        assertEquals(EQUIPMENT_PROD_DEMO_ID, createdProdEquip.getId());
        log.info("✅ Equipment (生产) created: {}", EQUIPMENT_PROD_DEMO_ID);

        // 2. Equipment - 检测设备
        log.info("2. 创建 Equipment (检测设备)...");
        Equipment inspEquip = new Equipment();
        inspEquip.setId(EQUIPMENT_INSP_DEMO_ID);
        inspEquip.setEquipmentCode("EQ-INSP-DEMO");
        inspEquip.setEquipmentName("三坐标测量仪");
        inspEquip.setManufacturerName(createObjectRef(MANUFACTURER_DEMO_ID));
        inspEquip.setCategory(createObjectRef(EQUIP_CLASS_DEMO_ID));
        inspEquip.setEquipmentModelRef(createObjectRef(EQUIP_MODEL_DEMO_ID));  // SDK必填！
        inspEquip.setSupplierName(createObjectRef(SUPPLIER_DEMO_ID));  // SDK必填！
        inspEquip.setLocationRef(createObjectRef(LOCATION_DEMO_ID));
        inspEquip.setStatus("Standby");

        Equipment createdInspEquip = equipmentDao.create(inspEquip);
        assertEquals(EQUIPMENT_INSP_DEMO_ID, createdInspEquip.getId());
        log.info("✅ Equipment (检测) created: {}", EQUIPMENT_INSP_DEMO_ID);

        // 3. Part（物料 - 版本对象）
        log.info("3. 创建 Part (版本对象)...");
        Part part = new Part();
        part.setId(PART_DEMO_ID);
        part.setPartNumber("PART-DEMO");
        part.setPartName("中心轮零件");
        part.setModelSpec("M-100");
        part.setStockQty(100);  // Integer类型
        part.setUnit(createObjectRef(UNIT_DEMO_ID));
        part.setSupplierName(createObjectRef(SUPPLIER_DEMO_ID));  // SDK必填
        part.setCategory(createObjectRef(PART_CLASS_DEMO_ID));
        part.setBusinessVersion("1.0");
        part.setDescription("示例物料");

        // 版本对象：设置master/branch（参考PartService.create）
        ObjectReference master = new ObjectReference();
        Map<String, Object> masterTenant = new HashMap<>();
        masterTenant.put("name", "basicTenant");
        masterTenant.put("id", "-1");
        masterTenant.put("clazz", "Tenant");
        master.setTenant(masterTenant);
        master.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "rdmExtensionType", "creator", "id"));

        ObjectReference branch = new ObjectReference();
        Map<String, Object> branchTenant = new HashMap<>();
        branchTenant.put("name", "basicTenant");
        branchTenant.put("id", "-1");
        branchTenant.put("clazz", "Tenant");
        branch.setTenant(branchTenant);
        branch.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "creator", "rdmExtensionType", "id"));

        part.setMaster(master);
        part.setBranch(branch);
        part.setSecurityLevel("internal");

        Part createdPart = partDao.create(part);
        assertEquals(PART_DEMO_ID, createdPart.getId());

        // 重要：获取SDK生成的master ID（用于WorkingPlan引用）
        partMasterDemoId = createdPart.getMaster().getId();
        log.info("✅ Part created: {}, Master ID: {}", PART_DEMO_ID, partMasterDemoId);

        log.info("========== Level 3 创建完成（3个对象）==========");
    }

    @Test
    @Order(5)
    @DisplayName("Level 4 - 创建工序和工艺路线")
    void test05_CreateLevel4() {
        log.info("========== Level 4: 创建工序和工艺路线 ==========");

        // 1. WorkingProcedure（工序）
        log.info("1. 创建 WorkingProcedure...");
        WorkingProcedure proc = new WorkingProcedure();
        proc.setId(PROCEDURE_DEMO_ID);
        proc.setProcedureCode("WP-DEMO");
        proc.setProcedureName("粗加工");
        proc.setSteps("1.上料 2.加工 3.检测");
        proc.setMainProductionEquipment(createObjectRef(EQUIPMENT_PROD_DEMO_ID));
        proc.setMainInspectionEquipment(createObjectRef(EQUIPMENT_INSP_DEMO_ID));  // SDK必填
        proc.setOperatorRef(createObjectRef(EMPLOYEE_DEMO_ID));
        proc.setStatus("NotStarted");  // 枚举值：NotStarted, InProgress, Completed, Paused

        WorkingProcedure createdProc = workingProcedureDao.create(proc);
        assertEquals(PROCEDURE_DEMO_ID, createdProc.getId());
        log.info("✅ WorkingProcedure created: {}", PROCEDURE_DEMO_ID);

        // 2. WorkingPlan（工艺路线 - 版本对象）
        log.info("2. 创建 WorkingPlan (版本对象)...");
        WorkingPlan plan = new WorkingPlan();
        plan.setId(PLAN_DEMO_ID);
        plan.setPlanCode("PLAN-DEMO");
        plan.setPlanName("中心轮零件加工");
        plan.setBusinessVersion("1.0");
        plan.setProductPart(createObjectRef(partMasterDemoId));  // 使用Part的master ID！
        plan.setOperatorRef(createObjectRef(EMPLOYEE_DEMO_ID));  // SDK必填
        plan.setDescription("中心轮零件加工工艺路线");

        // 版本对象：设置master/branch（同Part）
        ObjectReference planMaster = new ObjectReference();
        Map<String, Object> planMasterTenant = new HashMap<>();
        planMasterTenant.put("name", "basicTenant");
        planMasterTenant.put("id", "-1");
        planMasterTenant.put("clazz", "Tenant");
        planMaster.setTenant(planMasterTenant);
        planMaster.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "rdmExtensionType", "creator", "id"));

        ObjectReference planBranch = new ObjectReference();
        Map<String, Object> planBranchTenant = new HashMap<>();
        planBranchTenant.put("name", "basicTenant");
        planBranchTenant.put("id", "-1");
        planBranchTenant.put("clazz", "Tenant");
        planBranch.setTenant(planBranchTenant);
        planBranch.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "creator", "rdmExtensionType", "id"));

        plan.setMaster(planMaster);
        plan.setBranch(planBranch);

        WorkingPlan createdPlan = workingPlanDao.create(plan);
        assertEquals(PLAN_DEMO_ID, createdPlan.getId());
        log.info("✅ WorkingPlan created: {}, Master ID: {}", PLAN_DEMO_ID, createdPlan.getMaster().getId());

        log.info("========== Level 4 创建完成（2个对象）==========");
    }

    // ========== 辅助方法 ==========

    /**
     * 创建 ObjectReference（用于引用字段）
     */
    private ObjectReference createObjectRef(String id) {
        ObjectReference ref = new ObjectReference();
        ref.setId(id);
        return ref;
    }

    @Test
    @Order(99)
    @DisplayName("验证所有示例对象可查询")
    void test99_VerifyAll() {
        log.info("========== 验证所有示例对象可查询 ==========");

        // Level 0 验证
        Unit unit = unitDao.findById(UNIT_DEMO_ID);
        assertNotNull(unit, "Unit 应该存在");
        assertEquals("件", unit.getUnitName());
        log.info("✅ Unit verified: {}", UNIT_DEMO_ID);

        BusinessPartner mfg = businessPartnerDao.findById(MANUFACTURER_DEMO_ID);
        assertNotNull(mfg, "Manufacturer 应该存在");
        assertEquals("示例机床厂", mfg.getPartnerName());
        log.info("✅ Manufacturer verified: {}", MANUFACTURER_DEMO_ID);

        BusinessPartner sup = businessPartnerDao.findById(SUPPLIER_DEMO_ID);
        assertNotNull(sup, "Supplier 应该存在");
        assertEquals("示例零件供应商", sup.getPartnerName());
        log.info("✅ Supplier verified: {}", SUPPLIER_DEMO_ID);

        EquipmentClassfication ec = equipmentClassficationDao.findById(EQUIP_CLASS_DEMO_ID);
        assertNotNull(ec, "EquipmentClassfication 应该存在");
        assertEquals("CNC加工中心", ec.getEquipmentClassName());
        log.info("✅ EquipmentClassfication verified: {}", EQUIP_CLASS_DEMO_ID);

        PartClassfication pc = partClassficationDao.findById(PART_CLASS_DEMO_ID);
        assertNotNull(pc, "PartClassfication 应该存在");
        assertEquals("结构件", pc.getPartClassName());
        log.info("✅ PartClassfication verified: {}", PART_CLASS_DEMO_ID);

        // Level 1 验证
        Department dept = departmentDao.findById(DEPT_DEMO_ID);
        assertNotNull(dept, "Department 应该存在");
        assertEquals("生产部", dept.getDeptName());
        log.info("✅ Department verified: {}", DEPT_DEMO_ID);

        Employee emp = employeeDao.findById(EMPLOYEE_DEMO_ID);
        assertNotNull(emp, "Employee 应该存在");
        assertEquals("李四", emp.getEmployeeName());
        log.info("✅ Employee verified: {}", EMPLOYEE_DEMO_ID);

        // Level 2 验证
        EquipmentModel model = equipmentModelDao.findById(EQUIP_MODEL_DEMO_ID);
        assertNotNull(model, "EquipmentModel 应该存在");
        assertEquals("CNC-5000", model.getModelName());
        log.info("✅ EquipmentModel verified: {}", EQUIP_MODEL_DEMO_ID);

        Location loc = locationDao.findById(LOCATION_DEMO_ID);
        assertNotNull(loc, "Location 应该存在");
        assertEquals("一车间", loc.getLocationName());
        log.info("✅ Location verified: {}", LOCATION_DEMO_ID);

        // Level 3 验证
        Equipment prodEquip = equipmentDao.findById(EQUIPMENT_PROD_DEMO_ID);
        assertNotNull(prodEquip, "Equipment (生产) 应该存在");
        assertEquals("CNC加工中心A", prodEquip.getEquipmentName());
        log.info("✅ Equipment (生产) verified: {}", EQUIPMENT_PROD_DEMO_ID);

        Equipment inspEquip = equipmentDao.findById(EQUIPMENT_INSP_DEMO_ID);
        assertNotNull(inspEquip, "Equipment (检测) 应该存在");
        assertEquals("三坐标测量仪", inspEquip.getEquipmentName());
        log.info("✅ Equipment (检测) verified: {}", EQUIPMENT_INSP_DEMO_ID);

        Part part = partDao.findById(PART_DEMO_ID);
        assertNotNull(part, "Part 应该存在");
        assertEquals("中心轮零件", part.getPartName());
        assertNotNull(part.getMaster(), "Part.master 应该存在");
        assertNotNull(part.getMaster().getId(), "Part.master.id 应该存在");
        log.info("✅ Part verified: {}, Master ID: {}", PART_DEMO_ID, part.getMaster().getId());

        // Level 4 验证
        WorkingProcedure proc = workingProcedureDao.findById(PROCEDURE_DEMO_ID);
        assertNotNull(proc, "WorkingProcedure 应该存在");
        assertEquals("粗加工", proc.getProcedureName());
        log.info("✅ WorkingProcedure verified: {}", PROCEDURE_DEMO_ID);

        WorkingPlan plan = workingPlanDao.findById(PLAN_DEMO_ID);
        assertNotNull(plan, "WorkingPlan 应该存在");
        assertEquals("中心轮零件加工", plan.getPlanName());
        assertNotNull(plan.getMaster(), "WorkingPlan.master 应该存在");
        log.info("✅ WorkingPlan verified: {}, Master ID: {}", PLAN_DEMO_ID, plan.getMaster().getId());

        log.info("========== 所有示例对象验证通过（14个对象）==========");
    }
}
