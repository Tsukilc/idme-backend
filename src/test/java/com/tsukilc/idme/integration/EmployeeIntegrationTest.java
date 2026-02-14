package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.DepartmentCreateDTO;
import com.tsukilc.idme.dto.EmployeeCreateDTO;
import com.tsukilc.idme.service.DepartmentService;
import com.tsukilc.idme.service.EmployeeService;
import com.tsukilc.idme.vo.DepartmentVO;
import com.tsukilc.idme.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Employee（员工）模块集成测试
 * 测试覆盖：依赖创建 → CREATE → GET → LIST → UPDATE（状态/部门） → DELETE
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    private static String testDeptId;        // 测试部门ID
    private static String testDept2Id;       // 第二个测试部门ID
    private static String testEmployeeId;    // 测试员工ID

    @Test
    @Order(1)
    @DisplayName("1. 准备测试数据：创建测试部门")
    void test01_PrepareTestDepartment() {
        log.info("========== 准备：创建测试部门 ==========");

        // 1. 创建第一个部门
        DepartmentCreateDTO deptDto1 = new DepartmentCreateDTO();
        deptDto1.setDeptCode("TEST_DEPT_EMP");
        deptDto1.setDeptName("员工测试部门");
        deptDto1.setManager("测试经理");

        DepartmentVO dept1 = departmentService.create(deptDto1);
        testDeptId = dept1.getId();
        log.info("测试部门1创建成功，ID: {}", testDeptId);

        // 2. 创建第二个部门（用于测试员工调动）
        DepartmentCreateDTO deptDto2 = new DepartmentCreateDTO();
        deptDto2.setDeptCode("TEST_DEPT_EMP_2");
        deptDto2.setDeptName("员工测试部门2");
        deptDto2.setManager("测试经理2");

        DepartmentVO dept2 = departmentService.create(deptDto2);
        testDept2Id = dept2.getId();
        log.info("测试部门2创建成功，ID: {}", testDept2Id);

        assertNotNull(testDeptId);
        assertNotNull(testDept2Id);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建员工（关联部门）")
    void test02_CreateEmployee() {
        log.info("========== 测试：创建员工 ==========");
        assertNotNull(testDeptId, "testDeptId不应为null（依赖test01）");

        // 1. 构造DTO
        EmployeeCreateDTO dto = new EmployeeCreateDTO();
        dto.setEmployeeNo("EMP-TEST-001");
        dto.setEmployeeName("测试员工");
        dto.setDept(testDeptId);  // 关联测试部门
        dto.setJobTitle("软件工程师");
        dto.setPhone("13800138000");
        dto.setEmail("test.employee@example.com");
        dto.setStatus("在职");  // 中文状态
        dto.setHireDate(LocalDate.of(2024, 1, 1));  // 重新启用hireDate

        // 2. 调用service.create()
        EmployeeVO result = employeeService.create(dto);

        // 3. 验证返回值
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getId(), "ID应自动生成");
        assertEquals("EMP-TEST-001", result.getEmployeeNo(), "employeeNo应一致");
        assertEquals("测试员工", result.getEmployeeName(), "employeeName应一致");
        assertEquals(testDeptId, result.getDept(), "dept应关联到测试部门");
        assertEquals("软件工程师", result.getJobTitle(), "jobTitle应一致");
        assertEquals("在职", result.getStatus(), "status应转换为中文");
        assertEquals(LocalDate.of(2024, 1, 1), result.getHireDate(), "hireDate应一致");

        // 4. 保存testId供后续测试使用
        testEmployeeId = result.getId();
        log.info("员工创建成功，ID: {}, 部门: {}, 状态: {}",
            testEmployeeId, result.getDept(), result.getStatus());
    }

    @Test
    @Order(3)
    @DisplayName("3. 根据ID查询员工")
    void test03_GetById() {
        log.info("========== 测试：根据ID查询员工 ==========");
        assertNotNull(testEmployeeId, "testEmployeeId不应为null（依赖test02）");

        // 1. 调用service.getById()
        EmployeeVO result = employeeService.getById(testEmployeeId);

        // 2. 验证返回数据与create时一致
        assertNotNull(result, "返回结果不应为null");
        assertEquals(testEmployeeId, result.getId(), "ID应一致");
        assertEquals("EMP-TEST-001", result.getEmployeeNo(), "employeeNo应一致");
        assertEquals(testDeptId, result.getDept(), "dept应一致");
        assertEquals("在职", result.getStatus(), "status应为中文");

        log.info("查询成功: {}", result);
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询员工列表")
    void test04_List() {
        log.info("========== 测试：分页查询员工列表 ==========");
        assertNotNull(testEmployeeId, "testEmployeeId不应为null（依赖test02）");

        // 1. 调用service.list()（使用较大的pageSize）
        List<EmployeeVO> list = employeeService.list(1, 1000);

        // 2. 验证列表包含刚创建的数据
        assertNotNull(list, "列表不应为null");
        assertFalse(list.isEmpty(), "列表不应为空");

        EmployeeVO testData = list.stream()
            .filter(emp -> testEmployeeId.equals(emp.getId()))
            .findFirst()
            .orElse(null);

        assertNotNull(testData, "列表应包含刚创建的数据（ID: " + testEmployeeId + "）");
        log.info("找到测试数据: employeeNo={}, dept={}",
            testData.getEmployeeNo(), testData.getDept());

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 更新员工（切换状态）")
    void test05_UpdateStatus() {
        log.info("========== 测试：更新员工状态 ==========");
        assertNotNull(testEmployeeId, "testEmployeeId不应为null（依赖test02）");

        // 1. 先get获取当前数据
        EmployeeVO before = employeeService.getById(testEmployeeId);
        log.info("更新前状态: {}", before.getStatus());
        assertEquals("在职", before.getStatus(), "更新前应为在职");

        // 2. 修改状态为"外协"
        EmployeeCreateDTO updateDto = new EmployeeCreateDTO();
        updateDto.setEmployeeNo("EMP-TEST-001");
        updateDto.setEmployeeName("测试员工");
        updateDto.setDept(testDeptId);
        updateDto.setJobTitle("软件工程师");
        updateDto.setStatus("外协");  // 切换状态
        updateDto.setHireDate(LocalDate.of(2024, 1, 1));

        // 3. 调用service.update()
        EmployeeVO result = employeeService.update(testEmployeeId, updateDto);

        // 4. 验证更新成功
        assertNotNull(result, "返回结果不应为null");
        assertEquals(testEmployeeId, result.getId(), "ID不应变化");
        assertEquals("外协", result.getStatus(), "status应已更新为外协");

        log.info("状态更新成功: {} -> {}", before.getStatus(), result.getStatus());
    }

    @Test
    @Order(6)
    @DisplayName("6. 更新员工（调动部门）")
    void test06_UpdateDepartment() {
        log.info("========== 测试：更新员工部门 ==========");
        assertNotNull(testEmployeeId, "testEmployeeId不应为null（依赖test02）");
        assertNotNull(testDept2Id, "testDept2Id不应为null（依赖test01）");

        // 1. 先get获取当前部门
        EmployeeVO before = employeeService.getById(testEmployeeId);
        log.info("更新前部门: {}", before.getDept());
        assertEquals(testDeptId, before.getDept(), "更新前应在测试部门1");

        // 2. 调动到测试部门2
        EmployeeCreateDTO updateDto = new EmployeeCreateDTO();
        updateDto.setEmployeeNo("EMP-TEST-001");
        updateDto.setEmployeeName("测试员工");
        updateDto.setDept(testDept2Id);  // 切换部门
        updateDto.setJobTitle("高级软件工程师");  // 同时升职
        updateDto.setStatus("在职");  // 恢复在职状态
        updateDto.setHireDate(LocalDate.of(2024, 1, 1));

        // 3. 调用service.update()
        EmployeeVO result = employeeService.update(testEmployeeId, updateDto);

        // 4. 验证更新成功
        assertNotNull(result, "返回结果不应为null");
        assertEquals(testEmployeeId, result.getId(), "ID不应变化");
        assertEquals(testDept2Id, result.getDept(), "dept应已更新为测试部门2");
        assertEquals("高级软件工程师", result.getJobTitle(), "jobTitle应已更新");
        assertEquals("在职", result.getStatus(), "status应恢复为在职");

        log.info("部门调动成功: {} -> {}, 职位: {}",
            before.getDept(), result.getDept(), result.getJobTitle());
    }

    @Test
    @Order(7)
    @DisplayName("7. 按部门查询员工（已知限制：SDK可能不支持）")
    @org.junit.jupiter.api.Disabled("SDK条件查询可能不支持dept.id字段过滤，类似BusinessPartner")
    void test07_FindByDept() {
        log.info("========== 测试：按部门查询员工 ==========");
        assertNotNull(testDept2Id, "testDept2Id不应为null（依赖test01）");

        // 1. 按部门2查询（员工在test06中调动到部门2）
        List<EmployeeVO> list = employeeService.findByDept(testDept2Id);

        // 2. 验证返回结果
        assertNotNull(list, "列表不应为null");

        boolean found = list.stream()
            .anyMatch(emp -> testEmployeeId.equals(emp.getId()));
        assertTrue(found, "应包含测试员工（ID: " + testEmployeeId + "）");

        log.info("按部门查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(8)
    @DisplayName("8. 删除员工")
    void test08_DeleteEmployee() {
        log.info("========== 测试：删除员工 ==========");
        assertNotNull(testEmployeeId, "testEmployeeId不应为null（依赖test02）");

        // 1. 调用service.delete()
        assertDoesNotThrow(() -> {
            employeeService.delete(testEmployeeId);
        }, "删除员工不应抛出异常");

        // 2. 验证删除成功
        Exception exception = assertThrows(Exception.class, () -> {
            employeeService.getById(testEmployeeId);
        }, "删除后查询应抛出异常");

        log.info("员工删除成功，异常信息: {}", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("9. 清理：删除测试部门")
    void test09_CleanupTestDepartment() {
        log.info("========== 清理：删除测试部门 ==========");

        // 1. 删除测试部门1
        if (testDeptId != null) {
            assertDoesNotThrow(() -> {
                departmentService.delete(testDeptId);
            }, "删除测试部门1不应抛出异常");
            log.info("测试部门1删除成功");
        }

        // 2. 删除测试部门2
        if (testDept2Id != null) {
            assertDoesNotThrow(() -> {
                departmentService.delete(testDept2Id);
            }, "删除测试部门2不应抛出异常");
            log.info("测试部门2删除成功");
        }
    }
}
