package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.DepartmentCreateDTO;
import com.tsukilc.idme.service.DepartmentService;
import com.tsukilc.idme.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Department（部门）模块集成测试
 * 测试覆盖：CREATE（根/子） → GET → LIST → UPDATE → DELETE → TREE
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentIntegrationTest {

    @Autowired
    private DepartmentService departmentService;

    private static String rootDeptId;    // 根部门ID
    private static String subDeptId;     // 子部门ID

    @Test
    @Order(1)
    @DisplayName("1. 创建根部门")
    void test01_CreateRootDepartment() {
        log.info("========== 测试：创建根部门 ==========");

        // 1. 构造DTO（无父部门）
        DepartmentCreateDTO dto = new DepartmentCreateDTO();
        dto.setDeptCode("TEST_ROOT");
        dto.setDeptName("测试根部门");
        dto.setManager("张三");
        dto.setRemarks("集成测试根部门");

        // 2. 调用service.create()
        DepartmentVO result = departmentService.create(dto);

        // 3. 验证返回值
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getId(), "ID应自动生成");
        assertEquals("TEST_ROOT", result.getDeptCode(), "deptCode应一致");
        assertEquals("测试根部门", result.getDeptName(), "deptName应一致");
        assertEquals("张三", result.getManager(), "manager应一致");
        assertEquals("集成测试根部门", result.getRemarks(), "remarks应一致");
        assertNull(result.getParentDept(), "根部门的parentDept应为null");

        // 4. 保存testId供后续测试使用
        rootDeptId = result.getId();
        log.info("根部门创建成功，ID: {}", rootDeptId);
    }

    @Test
    @Order(2)
    @DisplayName("2. 创建子部门")
    void test02_CreateSubDepartment() {
        log.info("========== 测试：创建子部门 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");

        // 1. 构造DTO（指定父部门）
        DepartmentCreateDTO dto = new DepartmentCreateDTO();
        dto.setDeptCode("TEST_SUB");
        dto.setDeptName("测试子部门");
        dto.setParentDept(rootDeptId);  // 引用根部门
        dto.setManager("李四");
        dto.setRemarks("集成测试子部门");

        // 2. 调用service.create()
        DepartmentVO result = departmentService.create(dto);

        // 3. 验证返回值
        assertNotNull(result, "返回结果不应为null");
        assertNotNull(result.getId(), "ID应自动生成");
        assertEquals("TEST_SUB", result.getDeptCode(), "deptCode应一致");
        assertEquals("测试子部门", result.getDeptName(), "deptName应一致");
        assertEquals(rootDeptId, result.getParentDept(), "parentDept应指向根部门");

        // 4. 保存testId
        subDeptId = result.getId();
        log.info("子部门创建成功，ID: {}, 父部门: {}", subDeptId, rootDeptId);
    }

    @Test
    @Order(3)
    @DisplayName("3. 根据ID查询部门")
    void test03_GetById() {
        log.info("========== 测试：根据ID查询部门 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");

        // 1. 查询根部门
        DepartmentVO rootDept = departmentService.getById(rootDeptId);
        assertNotNull(rootDept, "根部门查询结果不应为null");
        assertEquals(rootDeptId, rootDept.getId(), "ID应一致");
        assertEquals("TEST_ROOT", rootDept.getDeptCode(), "deptCode应一致");

        // 2. 查询子部门
        DepartmentVO subDept = departmentService.getById(subDeptId);
        assertNotNull(subDept, "子部门查询结果不应为null");
        assertEquals(subDeptId, subDept.getId(), "ID应一致");
        assertEquals(rootDeptId, subDept.getParentDept(), "父部门引用应正确");

        log.info("查询成功 - 根部门: {}, 子部门: {}", rootDept.getDeptName(), subDept.getDeptName());
    }

    @Test
    @Order(4)
    @DisplayName("4. 分页查询部门列表")
    void test04_List() {
        log.info("========== 测试：分页查询部门列表 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");
        assertNotNull(subDeptId, "subDeptId不应为null（依赖test02）");

        // 1. 调用service.list() - 使用较大pageSize避免SDK历史数据干扰
        List<DepartmentVO> list = departmentService.list(1, 1000);

        // 2. 验证列表包含创建的数据
        assertNotNull(list, "列表不应为null");
        assertFalse(list.isEmpty(), "列表不应为空");

        boolean foundRoot = list.stream().anyMatch(dept -> rootDeptId.equals(dept.getId()));
        boolean foundSub = list.stream().anyMatch(dept -> subDeptId.equals(dept.getId()));

        assertTrue(foundRoot, "列表应包含根部门（ID: " + rootDeptId + "）");
        assertTrue(foundSub, "列表应包含子部门（ID: " + subDeptId + "）");

        log.info("查询成功，共 {} 条记录", list.size());
    }

    @Test
    @Order(5)
    @DisplayName("5. 查询部门树")
    void test05_GetTree() {
        log.info("========== 测试：查询部门树 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");
        assertNotNull(subDeptId, "subDeptId不应为null（依赖test02）");

        // 1. 调用service.getTree()
        List<DepartmentService.DepartmentTreeVO> tree = departmentService.getTree();

        // 2. 验证树形结构
        assertNotNull(tree, "树结构不应为null");
        assertFalse(tree.isEmpty(), "树结构不应为空");

        // 3. 查找测试根部门
        DepartmentService.DepartmentTreeVO rootNode = tree.stream()
            .filter(node -> rootDeptId.equals(node.getId()))
            .findFirst()
            .orElse(null);

        assertNotNull(rootNode, "应能找到测试根部门");
        assertEquals("TEST_ROOT", rootNode.getDeptCode(), "根部门deptCode应一致");
        assertNotNull(rootNode.getChildren(), "children列表不应为null");

        // 4. 验证子部门在根部门的children中
        boolean foundSubInTree = rootNode.getChildren().stream()
            .anyMatch(child -> subDeptId.equals(child.getId()));

        assertTrue(foundSubInTree, "子部门应在根部门的children中");

        log.info("树形查询成功 - 根部门 {} 包含 {} 个子部门",
            rootNode.getDeptName(), rootNode.getChildren().size());
    }

    @Test
    @Order(6)
    @DisplayName("6. 更新部门")
    void test06_Update() {
        log.info("========== 测试：更新部门 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");

        // 1. 先get获取当前数据
        DepartmentVO before = departmentService.getById(rootDeptId);
        log.info("更新前数据: {}", before);

        // 2. 修改字段
        DepartmentCreateDTO updateDto = new DepartmentCreateDTO();
        updateDto.setDeptCode("TEST_ROOT_UPDATED");
        updateDto.setDeptName("测试根部门（已更新）");
        updateDto.setManager("王五");
        updateDto.setRemarks("已更新的备注");

        // 3. 调用service.update()
        DepartmentVO result = departmentService.update(rootDeptId, updateDto);

        // 4. 验证更新成功
        assertNotNull(result, "返回结果不应为null");
        assertEquals(rootDeptId, result.getId(), "ID不应变化");
        assertEquals("TEST_ROOT_UPDATED", result.getDeptCode(), "deptCode应已更新");
        assertEquals("测试根部门（已更新）", result.getDeptName(), "deptName应已更新");
        assertEquals("王五", result.getManager(), "manager应已更新");

        log.info("更新成功: {}", result);
    }

    @Test
    @Order(7)
    @DisplayName("7. 删除部门（先删子部门）")
    void test07_DeleteSubDepartment() {
        log.info("========== 测试：删除子部门 ==========");
        assertNotNull(subDeptId, "subDeptId不应为null（依赖test02）");

        // 1. 调用service.delete()
        assertDoesNotThrow(() -> {
            departmentService.delete(subDeptId);
        }, "删除子部门不应抛出异常");

        // 2. 验证删除成功
        Exception exception = assertThrows(Exception.class, () -> {
            departmentService.getById(subDeptId);
        }, "删除后查询应抛出异常");

        log.info("子部门删除成功，异常信息: {}", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("8. 删除部门（删除根部门）")
    void test08_DeleteRootDepartment() {
        log.info("========== 测试：删除根部门 ==========");
        assertNotNull(rootDeptId, "rootDeptId不应为null（依赖test01）");

        // 1. 调用service.delete()
        assertDoesNotThrow(() -> {
            departmentService.delete(rootDeptId);
        }, "删除根部门不应抛出异常");

        // 2. 验证删除成功
        Exception exception = assertThrows(Exception.class, () -> {
            departmentService.getById(rootDeptId);
        }, "删除后查询应抛出异常");

        log.info("根部门删除成功，异常信息: {}", exception.getMessage());
    }
}
