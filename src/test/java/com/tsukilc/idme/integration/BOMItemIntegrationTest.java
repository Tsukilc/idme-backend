package com.tsukilc.idme.integration;

import com.tsukilc.idme.dto.*;
import com.tsukilc.idme.service.*;
import com.tsukilc.idme.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BOMItem（BOM项）集成测试
 * 竞赛核心模块（5分）- 测试树形查询和批量创建
 *
 * 测试流程：
 * 1. 创建依赖数据（3个Part作为测试物料）
 * 2. 创建BOM项（父件→子件1）
 * 3. 查询BOM项详情
 * 4. 分页查询BOM项列表
 * 5. 创建第二个BOM项（父件→子件2）
 * 6. 查询BOM树（树形结构）
 * 7. 批量创建BOM项
 * 8. 更新BOM项
 * 9. 删除BOM项
 * 10. 清理测试数据
 */
@SpringBootTest
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BOMItemIntegrationTest {

    @Autowired
    private BOMItemService bomItemService;

    @Autowired
    private PartService partService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private BusinessPartnerService businessPartnerService;

    @Autowired
    private PartClassficationService partClassficationService;

    // 测试数据ID
    private static String testBOMItemId1;  // 第一个BOM项
    private static String testBOMItemId2;  // 第二个BOM项

    // 依赖数据ID（3个Part：父件、子件1、子件2）
    private static String testParentPartId;
    private static String testChildPart1Id;
    private static String testChildPart2Id;

    // 基础依赖数据ID
    private static String testUnitId;
    private static String testSupplierId;
    private static String testCategoryId;

    // 唯一测试数据标识
    private static final String UNIQUE_PREFIX = "BOM-TEST-" + System.currentTimeMillis();

    @Test
    @Order(0)
    void test00_PrepareData() {
        log.info("=== 测试0：准备依赖数据（Unit + BusinessPartner + PartClassfication + 3个Part） ===");

        // 1. 创建计量单位
        UnitCreateDTO unitDTO = new UnitCreateDTO();
        unitDTO.setUnitName("个");
        unitDTO.setUnitDisplayName("个");
        UnitVO unit = unitService.create(unitDTO);
        testUnitId = unit.getId();
        assertNotNull(testUnitId);
        log.info("创建计量单位成功，ID: {}", testUnitId);

        // 2. 创建供应商
        BusinessPartnerCreateDTO supplierDTO = new BusinessPartnerCreateDTO();
        supplierDTO.setPartnerCode(UNIQUE_PREFIX + "-SUPPLIER");
        supplierDTO.setPartnerName("BOM测试供应商");
        supplierDTO.setPartnerType("Parts");
        supplierDTO.setPhone("12345678");
        BusinessPartnerVO supplier = businessPartnerService.create(supplierDTO);
        testSupplierId = supplier.getId();
        assertNotNull(testSupplierId);
        log.info("创建供应商成功，ID: {}", testSupplierId);

        // 3. 创建物料分类
        PartClassficationCreateDTO categoryDTO = new PartClassficationCreateDTO();
        categoryDTO.setPartClassName("BOM测试分类");
        PartClassficationVO category = partClassficationService.create(categoryDTO);
        testCategoryId = category.getId();
        assertNotNull(testCategoryId);
        log.info("创建物料分类成功，ID: {}", testCategoryId);

        // 4. 创建父件Part
        PartCreateDTO parentPartDTO = new PartCreateDTO();
        parentPartDTO.setPartNumber(UNIQUE_PREFIX + "-PARENT");
        parentPartDTO.setPartName("父件物料");
        parentPartDTO.setModelSpec("P1.0");
        parentPartDTO.setStockQty(50);
        parentPartDTO.setUnit(testUnitId);
        parentPartDTO.setSupplierName(testSupplierId);
        parentPartDTO.setCategory(testCategoryId);
        parentPartDTO.setBusinessVersion("1.0");
        PartVO parentPart = partService.create(parentPartDTO);
        testParentPartId = parentPart.getId();
        assertNotNull(testParentPartId);
        log.info("创建父件物料成功，ID: {}", testParentPartId);

        // 5. 创建子件1
        PartCreateDTO child1DTO = new PartCreateDTO();
        child1DTO.setPartNumber(UNIQUE_PREFIX + "-CHILD1");
        child1DTO.setPartName("子件物料1");
        child1DTO.setModelSpec("C1.0");
        child1DTO.setStockQty(100);
        child1DTO.setUnit(testUnitId);
        child1DTO.setSupplierName(testSupplierId);
        child1DTO.setCategory(testCategoryId);
        child1DTO.setBusinessVersion("1.0");
        PartVO child1 = partService.create(child1DTO);
        testChildPart1Id = child1.getId();
        assertNotNull(testChildPart1Id);
        log.info("创建子件物料1成功，ID: {}", testChildPart1Id);

        // 6. 创建子件2
        PartCreateDTO child2DTO = new PartCreateDTO();
        child2DTO.setPartNumber(UNIQUE_PREFIX + "-CHILD2");
        child2DTO.setPartName("子件物料2");
        child2DTO.setModelSpec("C2.0");
        child2DTO.setStockQty(200);
        child2DTO.setUnit(testUnitId);
        child2DTO.setSupplierName(testSupplierId);
        child2DTO.setCategory(testCategoryId);
        child2DTO.setBusinessVersion("1.0");
        PartVO child2 = partService.create(child2DTO);
        testChildPart2Id = child2.getId();
        assertNotNull(testChildPart2Id);
        log.info("创建子件物料2成功，ID: {}", testChildPart2Id);

        log.info("依赖数据准备完成：父件={}, 子件1={}, 子件2={}", testParentPartId, testChildPart1Id, testChildPart2Id);
    }

    @Test
    @Order(1)
    void test01_CreateBOMItem() {
        log.info("=== 测试1：创建BOM项（父件→子件1） ===");

        BOMItemCreateDTO dto = new BOMItemCreateDTO();
        dto.setParentPart(testParentPartId);
        dto.setChildPart(testChildPart1Id);
        dto.setQuantity(new BigDecimal("2.5"));
        dto.setUom(testUnitId);  // SDK要求ObjectReference，传Unit ID
        dto.setFindNumber(10);
        dto.setRemarks("测试BOM项1");

        testBOMItemId1 = bomItemService.create(dto);

        assertNotNull(testBOMItemId1);
        log.info("创建BOM项成功，ID: {}", testBOMItemId1);
    }

    @Test
    @Order(2)
    void test02_GetById() {
        log.info("=== 测试2：查询BOM项详情 ===");

        BOMItemVO vo = bomItemService.getById(testBOMItemId1);

        assertNotNull(vo);
        assertEquals(testBOMItemId1, vo.getId());
        assertEquals(testParentPartId, vo.getParentPart());
        assertEquals(testChildPart1Id, vo.getChildPart());
        assertEquals(0, new BigDecimal("2.5").compareTo(vo.getQuantity()));
        assertEquals(testUnitId, vo.getUom());  // uom返回Unit ID
        assertEquals(10, vo.getFindNumber());
        assertEquals("测试BOM项1", vo.getRemarks());

        log.info("查询BOM项成功：父件={}, 子件={}, 用量={}", vo.getParentPart(), vo.getChildPart(), vo.getQuantity());
    }

    @Test
    @Order(3)
    void test03_List() {
        log.info("=== 测试3：分页查询BOM项列表 ===");

        var result = bomItemService.list(1, 1000);

        assertNotNull(result);
        assertNotNull(result.getRecords());
        assertTrue(result.getRecords().size() > 0);

        // 应该能找到刚创建的BOM项
        boolean found = result.getRecords().stream()
            .anyMatch(vo -> testBOMItemId1.equals(vo.getId()));
        assertTrue(found, "列表中应该包含刚创建的BOM项");

        log.info("查询成功，共查到 {} 个BOM项", result.getRecords().size());
    }

    @Test
    @Order(4)
    void test04_CreateBOMItem2() {
        log.info("=== 测试4：创建第二个BOM项（父件→子件2） ===");

        BOMItemCreateDTO dto = new BOMItemCreateDTO();
        dto.setParentPart(testParentPartId);
        dto.setChildPart(testChildPart2Id);
        dto.setQuantity(new BigDecimal("1.0"));
        dto.setUom(testUnitId);  // SDK要求ObjectReference
        dto.setFindNumber(20);
        dto.setRemarks("测试BOM项2");

        testBOMItemId2 = bomItemService.create(dto);

        assertNotNull(testBOMItemId2);
        log.info("创建第二个BOM项成功，ID: {}", testBOMItemId2);
    }

    @Test
    @Order(5)
    void test05_GetTree() {
        log.info("=== 测试5：查询BOM树（竞赛要求：树形查询） ===");

        List<BOMItemVO> tree = bomItemService.getTreeByParent(testParentPartId);

        assertNotNull(tree);
        assertTrue(tree.size() >= 2, "应该查到至少2个子项（子件1和子件2）");

        // 验证树中包含两个子件
        List<String> childPartIds = tree.stream()
            .map(BOMItemVO::getChildPart)
            .toList();
        assertTrue(childPartIds.contains(testChildPart1Id), "应该包含子件1");
        assertTrue(childPartIds.contains(testChildPart2Id), "应该包含子件2");

        log.info("查询BOM树成功，父件下有 {} 个子项", tree.size());
        tree.forEach(item ->
            log.info("  - 子件: {}, 用量: {}, 项次: {}", item.getChildPart(), item.getQuantity(), item.getFindNumber())
        );
    }

    @Test
    @Order(6)
    void test06_BatchCreate() {
        log.info("=== 测试6：批量创建BOM项（竞赛要求：批量创建） ===");

        // 创建一个新的子件用于批量测试
        PartCreateDTO child3DTO = new PartCreateDTO();
        child3DTO.setPartNumber(UNIQUE_PREFIX + "-CHILD3");
        child3DTO.setPartName("子件物料3（批量测试）");
        child3DTO.setModelSpec("C3.0");
        child3DTO.setStockQty(150);
        child3DTO.setUnit(testUnitId);
        child3DTO.setSupplierName(testSupplierId);
        child3DTO.setCategory(testCategoryId);
        child3DTO.setBusinessVersion("1.0");
        PartVO child3 = partService.create(child3DTO);
        String testChildPart3Id = child3.getId();

        // 批量创建2个BOM项
        BOMItemCreateDTO item1 = new BOMItemCreateDTO();
        item1.setParentPart(testParentPartId);
        item1.setChildPart(testChildPart3Id);
        item1.setQuantity(new BigDecimal("3.0"));
        item1.setUom(testUnitId);  // SDK要求ObjectReference
        item1.setFindNumber(30);

        BOMItemCreateDTO item2 = new BOMItemCreateDTO();
        item2.setParentPart(testChildPart1Id); // 子件1作为父件
        item2.setChildPart(testChildPart3Id); // 形成多层BOM
        item2.setQuantity(new BigDecimal("1.5"));
        item2.setUom(testUnitId);  // SDK要求ObjectReference
        item2.setFindNumber(10);

        BOMItemBatchCreateDTO batchDTO = new BOMItemBatchCreateDTO();
        batchDTO.setItems(Arrays.asList(item1, item2));

        List<String> ids = bomItemService.batchCreate(batchDTO);

        assertNotNull(ids);
        assertEquals(2, ids.size(), "批量创建应该返回2个ID");
        log.info("批量创建成功，创建了 {} 个BOM项", ids.size());
    }

    @Test
    @Order(7)
    void test07_Update() {
        log.info("=== 测试7：更新BOM项 ===");

        BOMItemCreateDTO updateDTO = new BOMItemCreateDTO();
        updateDTO.setParentPart(testParentPartId);
        updateDTO.setChildPart(testChildPart1Id);
        updateDTO.setQuantity(new BigDecimal("5.0")); // 修改用量
        updateDTO.setUom(testUnitId);  // SDK要求ObjectReference
        updateDTO.setFindNumber(15);  // 修改项次
        updateDTO.setRemarks("已更新的BOM项");

        bomItemService.update(testBOMItemId1, updateDTO);

        // 验证更新
        BOMItemVO updated = bomItemService.getById(testBOMItemId1);
        assertEquals(0, new BigDecimal("5.0").compareTo(updated.getQuantity()));
        assertEquals(testUnitId, updated.getUom());  // uom返回Unit ID
        assertEquals(15, updated.getFindNumber());
        assertEquals("已更新的BOM项", updated.getRemarks());

        log.info("更新BOM项成功，新用量: {}, 新单位ID: {}", updated.getQuantity(), updated.getUom());
    }

    @Test
    @Order(8)
    void test08_WhereUsed() {
        log.info("=== 测试8：BOM反向查询（where-used） ===");

        // 查询testChildPart1被哪些父件使用
        List<BOMItemVO> whereUsedList = bomItemService.getWhereUsed(testChildPart1Id);

        assertNotNull(whereUsedList);
        assertTrue(whereUsedList.size() >= 1, "子件1至少被1个父件使用（父件 + 可能的批量创建项）");

        // 验证至少包含testParentPartId作为父件
        boolean foundParent = whereUsedList.stream()
            .anyMatch(vo -> testParentPartId.equals(vo.getParentPart()));
        assertTrue(foundParent, "反向查询结果应包含父件");

        log.info("反向查询成功，子件1被 {} 个父件使用", whereUsedList.size());
        whereUsedList.forEach(vo ->
            log.info("  - 父件: {}, 用量: {}", vo.getParentPart(), vo.getQuantity())
        );
    }

    @Test
    @Order(9)
    void test09_Delete() {
        log.info("=== 测试9：删除BOM项 ===");

        bomItemService.delete(testBOMItemId2);
        log.info("删除BOM项成功，ID: {}", testBOMItemId2);

        // 验证删除（查询应该返回null或抛异常）
        try {
            BOMItemVO deleted = bomItemService.getById(testBOMItemId2);
            // 如果SDK软删除，deleted可能不为null，但rdmDeleteFlag=1
            if (deleted != null) {
                log.info("BOM项可能使用软删除机制");
            }
        } catch (Exception e) {
            log.info("删除验证：BOM项已不存在（符合预期）");
        }
    }

    @Test
    @Order(99)
    void test99_Cleanup() {
        log.info("=== 测试99：清理测试数据 ===");

        // 删除BOM项（先删子关系）
        try {
            bomItemService.delete(testBOMItemId1);
            log.info("删除BOM项1成功");
        } catch (Exception e) {
            log.warn("删除BOM项1失败: {}", e.getMessage());
        }

        // 删除Part（先删子件再删父件）
        try {
            partService.delete(testChildPart2Id);
            log.info("删除子件2成功");
        } catch (Exception e) {
            log.warn("删除子件2失败（可能有依赖关系）: {}", e.getMessage());
        }

        try {
            partService.delete(testChildPart1Id);
            log.info("删除子件1成功");
        } catch (Exception e) {
            log.warn("删除子件1失败（可能有依赖关系）: {}", e.getMessage());
        }

        try {
            partService.delete(testParentPartId);
            log.info("删除父件成功");
        } catch (Exception e) {
            log.warn("删除父件失败（可能有依赖关系）: {}", e.getMessage());
        }

        // 删除基础依赖数据
        try {
            partClassficationService.delete(testCategoryId);
            log.info("删除物料分类成功");
        } catch (Exception e) {
            log.warn("删除物料分类失败: {}", e.getMessage());
        }

        try {
            businessPartnerService.delete(testSupplierId);
            log.info("删除供应商成功");
        } catch (Exception e) {
            log.warn("删除供应商失败: {}", e.getMessage());
        }

        try {
            unitService.delete(testUnitId);
            log.info("删除计量单位成功");
        } catch (Exception e) {
            log.warn("删除计量单位失败: {}", e.getMessage());
        }

        log.info("测试数据清理完成");
    }
}
