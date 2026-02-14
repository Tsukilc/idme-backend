package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.BOMItemDao;
import com.tsukilc.idme.dto.BOMItemBatchCreateDTO;
import com.tsukilc.idme.dto.BOMItemCreateDTO;
import com.tsukilc.idme.entity.BOMItem;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.BOMItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BOM项服务层
 * 竞赛核心模块（5分）- 支持树形查询和批量创建
 */
@Service
@Slf4j
public class BOMItemService {

    private final BOMItemDao bomItemDao;

    // BOM图缓存：parentPartId -> List<childPartId>
    private Map<String, List<String>> bomGraph = new HashMap<>();
    private volatile boolean cacheInitialized = false;

    // 树查询最大深度限制（防止死循环）
    private static final int MAX_TREE_DEPTH = 100;

    public BOMItemService(BOMItemDao bomItemDao) {
        this.bomItemDao = bomItemDao;
    }

    /**
     * 初始化/刷新BOM图缓存
     * 用于循环检测和性能优化
     */
    private synchronized void refreshCache() {
        log.info("刷新BOM图缓存...");
        Map<String, List<String>> newGraph = new HashMap<>();

        List<BOMItem> allItems = bomItemDao.findAll(1, 10000);
        for (BOMItem item : allItems) {
            // 提取 parentPartId
            String parentId = null;
            if (item.getTarget() != null) {
                parentId = item.getTarget().getId();
            } else if (item.getParentPart() != null) {
                parentId = item.getParentPart().getId();
            }

            // 提取 childPartId
            String childId = null;
            if (item.getSource() != null) {
                childId = item.getSource().getId();
            } else if (item.getChildPart() != null) {
                childId = item.getChildPart().getId();
            }

            if (parentId != null && childId != null) {
                newGraph.computeIfAbsent(parentId, k -> new ArrayList<>()).add(childId);
            }
        }

        this.bomGraph = newGraph;
        this.cacheInitialized = true;
        log.info("BOM图缓存刷新完成，共 {} 个父项，{} 条边", newGraph.size(),
                newGraph.values().stream().mapToInt(List::size).sum());
    }

    /**
     * 创建BOM项（带循环检测）
     */
    public BOMItemVO create(BOMItemCreateDTO dto) {
        // 确保缓存已初始化
        if (!cacheInitialized) {
            refreshCache();
        }

        // 循环检测：检测添加边(parent -> child)是否会形成环
        String parentPartId = dto.getParentPart();
        String childPartId = dto.getChildPart();

        if (wouldCreateCycle(parentPartId, childPartId)) {
            throw new IdmeException("不允许创建循环引用：将" + childPartId +
                    "添加为" + parentPartId + "的子件会形成环");
        }

        BOMItem entity = convertToEntity(dto);
        BOMItem created = bomItemDao.create(entity);

        // 创建后刷新缓存
        refreshCache();

        return convertToVO(created);
    }

    /**
     * 检测添加边(parent -> child)是否会形成环
     * 使用DFS + visited集合
     */
    private boolean wouldCreateCycle(String parentPartId, String childPartId) {
        // 模拟添加新边后的图
        Map<String, List<String>> tempGraph = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : bomGraph.entrySet()) {
            tempGraph.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        tempGraph.computeIfAbsent(parentPartId, k -> new ArrayList<>()).add(childPartId);

        // 从childPartId开始DFS，看能否回到parentPartId
        Set<String> visited = new HashSet<>();
        return dfsHasCycle(childPartId, parentPartId, tempGraph, visited);
    }

    /**
     * DFS检测环路
     */
    private boolean dfsHasCycle(String current, String target,
                                Map<String, List<String>> graph,
                                Set<String> visited) {
        if (current.equals(target)) {
            return true;  // 找到环
        }
        if (visited.contains(current)) {
            return false;
        }
        visited.add(current);

        List<String> children = graph.get(current);
        if (children != null) {
            for (String child : children) {
                if (dfsHasCycle(child, target, graph, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 批量创建BOM项（竞赛要求，带循环检测）
     */
    public List<String> batchCreate(BOMItemBatchCreateDTO dto) {
        log.info("批量创建BOM项，数量: {}", dto.getItems().size());

        // 确保缓存已初始化
        if (!cacheInitialized) {
            refreshCache();
        }

        List<String> ids = new ArrayList<>();

        for (BOMItemCreateDTO itemDto : dto.getItems()) {
            // 循环检测
            String parentPartId = itemDto.getParentPart();
            String childPartId = itemDto.getChildPart();

            if (wouldCreateCycle(parentPartId, childPartId)) {
                throw new IdmeException("批量创建失败：BOM项[" + parentPartId + "->" + childPartId + "]会形成环");
            }

            BOMItem entity = convertToEntity(itemDto);
            BOMItem created = bomItemDao.create(entity);
            ids.add(created.getId());

            // 立即更新缓存图（为下一个BOM项的循环检测做准备）
            bomGraph.computeIfAbsent(parentPartId, k -> new ArrayList<>()).add(childPartId);
        }

        // 批量创建完成后，刷新缓存（确保缓存与数据库一致）
        refreshCache();

        log.info("批量创建完成，成功创建 {} 个BOM项", ids.size());
        return ids;
    }

    /**
     * 分页查询BOM项
     */
    public PageResult<BOMItemVO> list(int pageNum, int pageSize) {
        List<BOMItem> list = bomItemDao.findAll(pageNum, pageSize);
        List<BOMItemVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<BOMItemVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 根据父项物料ID查询BOM树（树形查询，竞赛要求）
     * 包含循环引用防护和深度限制
     */
    public List<BOMItemVO> getTreeByParent(String parentPartId) {
        log.info("查询BOM树，父项物料ID: {}", parentPartId);
        Set<String> visited = new HashSet<>();
        return getTreeByParent(parentPartId, visited, 0);
    }

    /**
     * 递归查询BOM树（内部方法，带循环防护）
     */
    private List<BOMItemVO> getTreeByParent(String parentPartId, Set<String> visited, int depth) {
        // 深度限制
        if (depth >= MAX_TREE_DEPTH) {
            log.warn("达到最大树深度 {}, 停止递归", MAX_TREE_DEPTH);
            return Collections.emptyList();
        }

        // 循环检测
        if (visited.contains(parentPartId)) {
            log.warn("检测到循环引用: {}, 停止递归", parentPartId);
            return Collections.emptyList();
        }
        visited.add(parentPartId);

        // 查询所有BOM项
        List<BOMItem> allItems = bomItemDao.findAll(1, 1000);

        // 查询指定父项的直接子项
        List<BOMItem> directChildren = allItems.stream()
                .filter(item -> {
                    // 优先使用target字段（父件），fallback到parentPart
                    ObjectReference parentRef = item.getTarget() != null ? item.getTarget() : item.getParentPart();
                    return parentRef != null && parentPartId.equals(parentRef.getId());
                })
                .collect(Collectors.toList());

        // 构建树形结构
        List<BOMItemVO> tree = new ArrayList<>();
        for (BOMItem child : directChildren) {
            BOMItemVO vo = convertToVO(child);
            // 递归查询子项的子项（传递visited副本，避免兄弟节点互相影响）
            ObjectReference childRef = child.getSource() != null ? child.getSource() : child.getChildPart();
            if (childRef != null && childRef.getId() != null) {
                vo.setChildren(getTreeByParent(
                        childRef.getId(),
                        new HashSet<>(visited),  // 传递副本
                        depth + 1));
            }
            tree.add(vo);
        }

        return tree;
    }

    /**
     * 根据ID查询BOM项详情
     */
    public BOMItemVO getById(String id) {
        BOMItem entity = bomItemDao.findById(id);
        return convertToVO(entity);
    }

    /**
     * 更新BOM项（清空系统字段，刷新缓存）
     */
    public void update(String id, BOMItemCreateDTO dto) {
        // 循环检测（与创建时相同）
        String parentPartId = dto.getParentPart();
        String childPartId = dto.getChildPart();

        // 更新前先检查是否会形成环
        if (wouldCreateCycle(parentPartId, childPartId)) {
            throw new IdmeException("不允许更新为循环引用：将" + childPartId +
                    "添加为" + parentPartId + "的子件会形成环");
        }

        BOMItem entity = convertToEntity(dto);
        entity.setId(id);

        // 清空系统字段
        entity.setCreator(null);
        entity.setModifier(null);
        entity.setCreateTime(null);
        entity.setLastUpdateTime(null);
        entity.setRdmDeleteFlag(null);
        entity.setRdmExtensionType(null);
        entity.setClassName(null);
        entity.setRdmVersion(null);

        bomItemDao.update(entity);

        // 更新后刷新缓存
        refreshCache();
    }

    /**
     * 删除BOM项（刷新缓存）
     */
    public void delete(String id) {
        bomItemDao.delete(id);

        // 删除后刷新缓存
        refreshCache();
    }

    /**
     * BOM反向查询（where-used查询）
     * 查询某子件被哪些父件使用
     */
    public List<BOMItemVO> getWhereUsed(String partId) {
        log.info("BOM反向查询，子件ID: {}", partId);

        // 查询所有BOM项
        List<BOMItem> all = bomItemDao.findAll(1, 1000);

        // 筛选出子件ID = partId的BOM项
        List<BOMItem> whereUsedItems = all.stream()
                .filter(item -> {
                    // 优先使用source字段（子件），fallback到childPart
                    ObjectReference childRef = item.getSource() != null ? item.getSource() : item.getChildPart();
                    return childRef != null && partId.equals(childRef.getId());
                })
                .collect(Collectors.toList());

        log.info("反向查询完成，找到 {} 个父件使用此子件", whereUsedItems.size());

        return whereUsedItems.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * DTO -> Entity（处理双重字段命名 + quantity特殊格式）
     */
    private BOMItem convertToEntity(BOMItemCreateDTO dto) {
        BOMItem entity = new BOMItem();

        // 双重字段命名：source/target（SDK标准）+ parentPart/childPart（业务别名）
        // source = childPart（子件）, target = parentPart（父件）
        ObjectReference childPartRef = new ObjectReference(dto.getChildPart(), "Part");
        ObjectReference parentPartRef = new ObjectReference(dto.getParentPart(), "Part");

        entity.setSource(childPartRef);      // SDK标准字段
        entity.setTarget(parentPartRef);     // SDK标准字段
        entity.setChildPart(childPartRef);   // 业务别名
        entity.setParentPart(parentPartRef); // 业务别名

        // quantity：SDK要求 {value: "数值"} 格式
        if (dto.getQuantity() != null) {
            Map<String, Object> quantityMap = new HashMap<>();
            quantityMap.put("value", dto.getQuantity().toString());
            entity.setQuantity(quantityMap);
        }

        // uom：ObjectReference（-> Unit）
        if (dto.getUom() != null) {
            entity.setUom(new ObjectReference(dto.getUom(), "Unit"));
        }

        entity.setFindNumber(dto.getFindNumber());

        // effectiveFrom/To：LocalDate → LocalDateTime（类似productionDate处理）
        if (dto.getEffectiveFrom() != null) {
            entity.setEffectiveFrom(dto.getEffectiveFrom().atStartOfDay());
        }
        if (dto.getEffectiveTo() != null) {
            entity.setEffectiveTo(dto.getEffectiveTo().atStartOfDay());
        }

        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    /**
     * Entity -> VO（处理quantity和uom的反向转换）
     */
    private BOMItemVO convertToVO(BOMItem entity) {
        BOMItemVO vo = new BOMItemVO();
        vo.setId(entity.getId());

        // 优先使用source/target提取数据（SDK标准字段）
        ObjectReference parentRef = entity.getTarget() != null ? entity.getTarget() : entity.getParentPart();
        ObjectReference childRef = entity.getSource() != null ? entity.getSource() : entity.getChildPart();

        // 处理 parentPart 引用
        if (parentRef != null) {
            vo.setParentPart(parentRef.getId());
            vo.setParentPartName(parentRef.getDisplayName() != null
                    ? parentRef.getDisplayName()
                    : parentRef.getName());
        }

        // 处理 childPart 引用
        if (childRef != null) {
            vo.setChildPart(childRef.getId());
            vo.setChildPartName(childRef.getDisplayName() != null
                    ? childRef.getDisplayName()
                    : childRef.getName());
        }

        // quantity：SDK返回 {value: "1.000000"}，提取value转为BigDecimal
        if (entity.getQuantity() != null) {
            if (entity.getQuantity() instanceof Map) {
                Object value = ((Map<?, ?>) entity.getQuantity()).get("value");
                if (value != null) {
                    vo.setQuantity(new BigDecimal(value.toString()));
                }
            } else if (entity.getQuantity() instanceof Number) {
                vo.setQuantity(new BigDecimal(entity.getQuantity().toString()));
            }
        }

        // uom：ObjectReference -> String ID
        if (entity.getUom() != null) {
            vo.setUom(entity.getUom().getId());
        }

        vo.setFindNumber(entity.getFindNumber());
        vo.setEffectiveFrom(entity.getEffectiveFrom());
        vo.setEffectiveTo(entity.getEffectiveTo());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());

        return vo;
    }
}
