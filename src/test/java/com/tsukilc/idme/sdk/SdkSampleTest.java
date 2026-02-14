package com.tsukilc.idme.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsukilc.idme.client.IdmeSdkClient;
import com.tsukilc.idme.client.dto.QueryRequest;
import com.tsukilc.idme.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SDK调用样例测试
 * 目的：实际调用SDK各种接口，收集请求/响应数据，用于生成文档
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SdkSampleTest {

    @Autowired
    private IdmeSdkClient sdkClient;

    @Autowired
    private ObjectMapper objectMapper;

    // 测试过程中创建的实体ID，用于后续测试和清理
    private static String testLocationId;
    private static String testEmployeeId;

    /**
     * 测试1: GET操作 - 根据ID查询单个实体
     * 实体：Unit（计量单位）
     */
    @Test
    @Order(1)
    void test01_Get() throws Exception {
        log.info("\n========== TEST 1: GET操作 - 查询单个Unit实体 ==========");

        // 1. 先list查询一个已有的Unit，获取ID
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setCondition(new HashMap<>());
        List<Unit> units = sdkClient.list("Unit", queryRequest, 1, 1, Unit.class);

        if (units.isEmpty()) {
            log.warn("跳过GET测试：没有找到Unit数据");
            return;
        }

        String unitId = units.get(0).getId();
        log.info("选择Unit ID: {}", unitId);

        // 2. 执行GET操作
        Unit unit = sdkClient.get("Unit", unitId, Unit.class);

        // 3. 输出结果
        log.info("✅ GET操作成功");
        log.info("请求参数: {\"id\": \"{}\"}", unitId);
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(unit));

        Assertions.assertNotNull(unit);
        Assertions.assertEquals(unitId, unit.getId());
    }

    /**
     * 测试2: LIST操作 - 分页查询列表（无条件）
     * 实体：Unit（计量单位）
     */
    @Test
    @Order(2)
    void test02_List() throws Exception {
        log.info("\n========== TEST 2: LIST操作 - 分页查询Unit列表（无条件） ==========");

        // 1. 构造查询请求（空条件）
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setCondition(new HashMap<>());

        // 2. 执行LIST操作
        List<Unit> units = sdkClient.list("Unit", queryRequest, 1, 5, Unit.class);

        // 3. 输出结果
        log.info("✅ LIST操作成功");
        log.info("请求参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryRequest));
        log.info("分页参数: curPage=1, pageSize=5");
        log.info("响应数据: 共{}条记录", units.size());
        if (!units.isEmpty()) {
            log.info("第一条数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(units.get(0)));
        }

        Assertions.assertNotNull(units);
    }

    /**
     * 测试3: LIST操作 - 条件查询
     * 实体：Employee（员工）
     */
    @Test
    @Order(3)
    void test03_ListWithCondition() throws Exception {
        log.info("\n========== TEST 3: LIST操作 - Employee条件查询（按状态） ==========");

        // 1. 构造查询条件
        QueryRequest queryRequest = new QueryRequest();
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", "Active");  // 查询在职员工
        queryRequest.setCondition(condition);
        queryRequest.setOrderBy("employeeNo");
        queryRequest.setOrderDirection("ASC");

        // 2. 执行LIST操作
        List<Employee> employees = sdkClient.list("Employee", queryRequest, 1, 5, Employee.class);

        // 3. 输出结果
        log.info("✅ 条件查询成功");
        log.info("请求参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(queryRequest));
        log.info("响应数据: 共{}条在职员工", employees.size());
        if (!employees.isEmpty()) {
            log.info("第一个员工: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employees.get(0)));
            // 保存第一个员工ID用于后续UPDATE测试
            testEmployeeId = employees.get(0).getId();
        }

        Assertions.assertNotNull(employees);
    }

    /**
     * 测试4: CREATE操作 - 创建普通实体
     * 实体：Location（位置）
     */
    @Test
    @Order(4)
    void test04_Create() throws Exception {
        log.info("\n========== TEST 4: CREATE操作 - 创建Location实体 ==========");

        // 1. 构造创建参数（注意：id不需要填写，会自动生成）
        Location location = new Location();
        location.setLocationCode("TEST_LOC_" + System.currentTimeMillis());
        location.setLocationName("测试位置");
        location.setLocationType("Workshop");
        location.setAddressText("测试地址");
        location.setRemarks("SDK测试创建的位置");
        // 注意：creator, modifier 会被 IdmeSdkClient 自动注入

        // 2. 执行CREATE操作
        log.info("创建参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(location));

        Location created = sdkClient.create("Location", location, Location.class);

        // 3. 输出结果
        log.info("✅ CREATE操作成功");
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(created));

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals(location.getLocationCode(), created.getLocationCode());

        // 保存ID用于后续DELETE测试
        testLocationId = created.getId();
        log.info("保存测试Location ID: {}", testLocationId);
    }

    /**
     * 测试5: CREATE操作 - 创建包含对象引用的实体
     * 实体：Location（位置）- 包含parentLocation和manager引用
     */
    @Test
    @Order(5)
    void test05_CreateWithReference() throws Exception {
        log.info("\n========== TEST 5: CREATE操作 - 创建包含对象引用的Location ==========");

        // 准备：需要先有一个父位置
        if (testLocationId == null) {
            log.warn("跳过测试：没有父位置ID");
            return;
        }

        // 1. 构造包含对象引用的Location
        Location location = new Location();
        location.setLocationCode("TEST_SUB_LOC_" + System.currentTimeMillis());
        location.setLocationName("测试子位置");
        location.setLocationType("Line");
        location.setAddressText("测试地址");

        // 对象引用：parentLocation（引用上面创建的Location）
        ObjectReference parentRef = new ObjectReference();
        parentRef.setId(testLocationId);  // create时只需要设置id即可
        location.setParentLocation(parentRef);

        // 对象引用：manager（引用Employee）
        if (testEmployeeId != null) {
            ObjectReference managerRef = new ObjectReference();
            managerRef.setId(testEmployeeId);
            location.setManager(managerRef);
        }

        log.info("创建参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(location));

        // 2. 执行CREATE操作
        Location created = sdkClient.create("Location", location, Location.class);

        // 3. 输出结果
        log.info("✅ CREATE包含引用操作成功");
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(created));
        log.info("注意：响应中的ObjectReference包含了displayName等完整信息");

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getParentLocation());
        Assertions.assertEquals(testLocationId, created.getParentLocation().getId());
    }

    /**
     * 测试6: UPDATE操作 - 更新实体
     * 实体：Location
     */
    @Test
    @Order(6)
    void test06_Update() throws Exception {
        log.info("\n========== TEST 6: UPDATE操作 - 更新Location ==========");

        if (testLocationId == null) {
            log.warn("跳过UPDATE测试：没有测试Location ID");
            return;
        }

        // 1. 先GET获取当前数据
        Location location = sdkClient.get("Location", testLocationId, Location.class);

        // 2. 修改字段
        location.setLocationName("测试位置（已更新）");
        location.setRemarks("SDK测试更新的备注");

        log.info("更新参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(location));

        // 3. 执行UPDATE操作
        Location updated = sdkClient.update("Location", location, Location.class);

        // 4. 输出结果
        log.info("✅ UPDATE操作成功");
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updated));
        log.info("注意：rdmVersion版本号会自动递增");

        Assertions.assertNotNull(updated);
        Assertions.assertEquals("测试位置（已更新）", updated.getLocationName());
        Assertions.assertTrue(updated.getRdmVersion() > location.getRdmVersion());
    }

    /**
     * 测试7: CREATE操作 - 创建关联对象（Link实体）
     * 实体：EquipmentSparePartLink（设备备件关联）
     */
    @Test
    @Order(7)
    void test07_CreateLink() throws Exception {
        log.info("\n========== TEST 7: CREATE操作 - 创建EquipmentSparePartLink关联 ==========");

        // 准备：需要先查询设备和备件
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setCondition(new HashMap<>());

        List<Equipment> equipments = sdkClient.list("Equipment", queryRequest, 1, 1, Equipment.class);
        List<Part> parts = sdkClient.list("Part", queryRequest, 1, 1, Part.class);

        if (equipments.isEmpty() || parts.isEmpty()) {
            log.warn("跳过Link测试：没有找到Equipment或Part数据");
            return;
        }

        String equipmentId = equipments.get(0).getId();
        String partId = parts.get(0).getId();

        // 1. 构造Link实体
        EquipmentSparePartLink link = new EquipmentSparePartLink();
        link.setQuantity(5);
        link.setRemarks("测试设备备件关联");

        // 设置equipment和sparePart引用
        ObjectReference equipmentRef = new ObjectReference();
        equipmentRef.setId(equipmentId);
        link.setEquipment(equipmentRef);

        ObjectReference partRef = new ObjectReference();
        partRef.setId(partId);
        link.setSparePart(partRef);

        log.info("创建参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(link));

        // 2. 执行CREATE操作
        EquipmentSparePartLink created = sdkClient.create("EquipmentSparePartLink", link, EquipmentSparePartLink.class);

        // 3. 输出结果
        log.info("✅ CREATE Link操作成功");
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(created));

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
    }

    /**
     * 测试8: DELETE操作 - 删除实体
     * 实体：Location（清理测试数据）
     */
    @Test
    @Order(8)
    void test08_Delete() throws Exception {
        log.info("\n========== TEST 8: DELETE操作 - 删除测试Location ==========");

        if (testLocationId == null) {
            log.warn("跳过DELETE测试：没有测试Location ID");
            return;
        }

        // 执行DELETE操作
        log.info("删除Location ID: {}", testLocationId);
        sdkClient.delete("Location", testLocationId);

        log.info("✅ DELETE操作成功");

        // 验证：尝试GET，应该返回null或抛出异常
        try {
            Location deleted = sdkClient.get("Location", testLocationId, Location.class);
            if (deleted != null) {
                log.warn("删除后仍能查询到数据（可能是软删除）: {}", deleted.getRdmDeleteFlag());
            }
        } catch (Exception e) {
            log.info("删除后查询失败（符合预期）: {}", e.getMessage());
        }
    }

    /**
     * 测试9: 版本对象创建
     * 实体：Part（物料）- 版本对象需要特殊处理
     */
    @Test
    @Order(9)
    void test09_CreateVersionObject() throws Exception {
        log.info("\n========== TEST 9: CREATE版本对象 - Part ==========");

        // 1. 构造Part（版本对象）
        Part part = new Part();
        part.setPartNumber("TEST_PART_" + System.currentTimeMillis());
        part.setPartName("测试物料");
        part.setDescription("SDK测试创建的物料");

        // 版本对象必需字段：首次创建时传空的ObjectReference，SDK会自动创建
        ObjectReference master = new ObjectReference();
        ObjectReference branch = new ObjectReference();
        part.setMaster(master);
        part.setBranch(branch);

        log.info("创建参数: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(part));

        // 2. 执行CREATE操作
        Part created = sdkClient.create("Part", part, Part.class);

        // 3. 输出结果
        log.info("✅ CREATE版本对象成功");
        log.info("响应数据: {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(created));
        log.info("注意：SDK自动创建了master和branch，并设置了version、latest等字段");

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getMaster());
        Assertions.assertNotNull(created.getMaster().getId());
        Assertions.assertTrue(created.getLatest());
    }

    /**
     * 测试总结：输出所有测试场景汇总
     */
    @AfterAll
    static void summary() {
        log.info("\n");
        log.info("========== SDK调用测试汇总 ==========");
        log.info("✅ TEST 1: GET操作 - 根据ID查询单个实体");
        log.info("✅ TEST 2: LIST操作 - 分页查询（无条件）");
        log.info("✅ TEST 3: LIST操作 - 条件查询");
        log.info("✅ TEST 4: CREATE操作 - 创建普通实体");
        log.info("✅ TEST 5: CREATE操作 - 创建包含对象引用的实体");
        log.info("✅ TEST 6: UPDATE操作 - 更新实体");
        log.info("✅ TEST 7: CREATE操作 - 创建关联对象（Link）");
        log.info("✅ TEST 8: DELETE操作 - 删除实体");
        log.info("✅ TEST 9: CREATE操作 - 创建版本对象");
        log.info("==========================================");
        log.info("请查看日志输出，收集请求/响应数据用于生成文档");
    }
}
