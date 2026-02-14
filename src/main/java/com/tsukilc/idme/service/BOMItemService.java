package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.BOMItemDao;
import com.tsukilc.idme.dto.BOMItemBatchCreateDTO;
import com.tsukilc.idme.dto.BOMItemCreateDTO;
import com.tsukilc.idme.entity.BOMItem;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.vo.BOMItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * BOM项服务层
 * 竞赛核心模块（5分）- 支持树形查询和批量创建
 */
@Service
@Slf4j
public class BOMItemService {

    private final BOMItemDao bomItemDao;

    public BOMItemService(BOMItemDao bomItemDao) {
        this.bomItemDao = bomItemDao;
    }

    /**
     * 创建BOM项
     */
    public String create(BOMItemCreateDTO dto) {
        BOMItem entity = convertToEntity(dto);
        BOMItem created = bomItemDao.create(entity);
        return created.getId();
    }

    /**
     * 批量创建BOM项（竞赛要求）
     */
    public List<String> batchCreate(BOMItemBatchCreateDTO dto) {
        log.info("批量创建BOM项，数量: {}", dto.getItems().size());
        List<String> ids = new ArrayList<>();
        
        for (BOMItemCreateDTO itemDto : dto.getItems()) {
            String id = create(itemDto);
            ids.add(id);
        }
        
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
     */
    public List<BOMItemVO> getTreeByParent(String parentPartId) {
        log.info("查询BOM树，父项物料ID: {}", parentPartId);
        
        // 查询所有BOM项
        List<BOMItem> allItems = bomItemDao.findAll(1, 1000);
        
        // 查询指定父项的直接子项
        List<BOMItem> directChildren = allItems.stream()
                .filter(item -> item.getParentPart() != null && parentPartId.equals(item.getParentPart().getId()))
                .collect(Collectors.toList());
        
        // 构建树形结构
        List<BOMItemVO> tree = new ArrayList<>();
        for (BOMItem child : directChildren) {
            BOMItemVO vo = convertToVO(child);
            // 递归查询子项的子项
            if (child.getChildPart() != null) {
                vo.setChildren(getTreeByParent(child.getChildPart().getId()));
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
     * 更新BOM项（清空系统字段）
     */
    public void update(String id, BOMItemCreateDTO dto) {
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
    }

    /**
     * 删除BOM项
     */
    public void delete(String id) {
        bomItemDao.delete(id);
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
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
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
