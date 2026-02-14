package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.DepartmentCreateDTO;
import com.tsukilc.idme.dto.EmployeeCreateDTO;
import com.tsukilc.idme.dto.LocationCreateDTO;
import com.tsukilc.idme.service.DepartmentService;
import com.tsukilc.idme.service.EmployeeService;
import com.tsukilc.idme.service.LocationService;
import com.tsukilc.idme.vo.DepartmentVO;
import com.tsukilc.idme.vo.EmployeeVO;
import com.tsukilc.idme.vo.LocationVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Location（位置）模块集成测试
 * 测试覆盖：依赖准备 → CREATE（根/子） → GET → LIST → UPDATE → DELETE → 清理
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LocationIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    private static String testDeptId;       // 测试部门ID
    private static String testEmployeeId;   // 测试员工ID（作为manager）
    private static String rootLocationId;
    private static String subLocationId;

    @Test
    @Order(1)
    @DisplayName("1. 准备：创建测试部门和员工")
    void test01_PrepareDependencies() {
        log.info("========== 准备：创建测试部门和员工 ==========");

        // 创建测试部门
        DepartmentCreateDTO deptDto = new DepartmentCreateDTO();
        deptDto.setDeptCode("TEST_DEPT_LOC");
        deptDto.setDeptName("位置测试部门");
        deptDto.setManager("测试经理");

        DepartmentVO dept = departmentService.create(deptDto);
        testDeptId = dept.getId();
        log.info("测试部门创建成功，ID: {}", testDeptId);

        // 创建测试员工（作为Location的manager）
        EmployeeCreateDTO empDto = new EmployeeCreateDTO();
        empDto.setEmployeeNo("EMP_LOC_MGR");
        empDto.setEmployeeName("位置负责人");
        empDto.setDept(testDeptId);
        empDto.setJobTitle("负责人");
        empDto.setStatus("在职");
        empDto.setHireDate(LocalDate.of(2024, 1, 1));

        EmployeeVO emp = employeeService.create(empDto);
        testEmployeeId = emp.getId();
        log.info("测试员工创建成功，ID: {}", testEmployeeId);

        assertNotNull(testDeptId);
        assertNotNull(testEmployeeId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建根位置（工厂）")
    void test02_CreateRootLocation() {
        log.info("========== 测试：创建根位置 ==========");
        assertNotNull(testEmployeeId);

        LocationCreateDTO dto = new LocationCreateDTO();
        dto.setLocationCode("PLANT_001");
        dto.setLocationName("测试工厂");
        dto.setLocationType("Plant");
        dto.setAddressText("测试地址123号");
        dto.setManager(testEmployeeId);  // 添加manager字段

        LocationVO result = locationService.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("PLANT_001", result.getLocationCode());
        assertEquals("Plant", result.getLocationType());
        assertEquals(testEmployeeId, result.getManager());

        rootLocationId = result.getId();
        log.info("根位置创建成功，ID: {}, Manager: {}", rootLocationId, result.getManager());
    }

    @Test
    @Order(3)
    @DisplayName("3. 创建子位置（车间）")
    void test03_CreateSubLocation() {
        log.info("========== 测试：创建子位置 ==========");
        assertNotNull(rootLocationId);
        assertNotNull(testEmployeeId);

        LocationCreateDTO dto = new LocationCreateDTO();
        dto.setLocationCode("WORKSHOP_001");
        dto.setLocationName("测试车间");
        dto.setLocationType("Workshop");
        dto.setParentLocation(rootLocationId);
        dto.setManager(testEmployeeId);  // 添加manager字段

        LocationVO result = locationService.create(dto);

        assertNotNull(result);
        assertEquals(rootLocationId, result.getParentLocation());
        assertEquals(testEmployeeId, result.getManager());

        subLocationId = result.getId();
        log.info("子位置创建成功，ID: {}", subLocationId);
    }

    @Test
    @Order(4)
    @DisplayName("4. 查询位置")
    void test04_GetById() {
        log.info("========== 测试：查询位置 ==========");

        LocationVO result = locationService.getById(rootLocationId);
        assertNotNull(result);
        assertEquals("PLANT_001", result.getLocationCode());
        assertEquals(testEmployeeId, result.getManager());

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(5)
    @DisplayName("5. 分页查询位置列表")
    void test05_List() {
        log.info("========== 测试：分页查询位置列表 ==========");

        List<LocationVO> list = locationService.list(1, 1000);

        assertNotNull(list);
        assertTrue(list.stream().anyMatch(loc -> rootLocationId.equals(loc.getId())));

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(6)
    @DisplayName("6. 更新位置")
    void test06_Update() {
        log.info("========== 测试：更新位置 ==========");

        LocationCreateDTO updateDto = new LocationCreateDTO();
        updateDto.setLocationCode("PLANT_001_UPDATED");
        updateDto.setLocationName("测试工厂（已更新）");
        updateDto.setLocationType("Plant");
        updateDto.setAddressText("新地址456号");
        updateDto.setManager(testEmployeeId);  // 保留manager字段

        LocationVO result = locationService.update(rootLocationId, updateDto);

        assertEquals("PLANT_001_UPDATED", result.getLocationCode());
        log.info("更新成功: {}", result);
    }

    @Test
    @Order(7)
    @DisplayName("7. 删除位置")
    void test07_DeleteLocations() {
        log.info("========== 测试：删除位置 ==========");

        // 先删除子位置
        assertDoesNotThrow(() -> locationService.delete(subLocationId));
        log.info("子位置删除成功");

        // 再删除根位置
        assertDoesNotThrow(() -> locationService.delete(rootLocationId));
        log.info("根位置删除成功");
    }

    @Test
    @Order(8)
    @DisplayName("8. 清理：删除测试员工和部门")
    void test08_CleanupDependencies() {
        log.info("========== 清理：删除测试员工和部门 ==========");

        if (testEmployeeId != null) {
            assertDoesNotThrow(() -> employeeService.delete(testEmployeeId));
            log.info("测试员工删除成功");
        }

        if (testDeptId != null) {
            assertDoesNotThrow(() -> departmentService.delete(testDeptId));
            log.info("测试部门删除成功");
        }
    }
}
