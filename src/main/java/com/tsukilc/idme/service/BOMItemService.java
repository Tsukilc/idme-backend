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
     * 更新BOM项
     */
    public void update(String id, BOMItemCreateDTO dto) {
        BOMItem entity = convertToEntity(dto);
        entity.setId(id);
        bomItemDao.update(entity);
    }

    /**
     * 删除BOM项
     */
    public void delete(String id) {
        bomItemDao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private BOMItem convertToEntity(BOMItemCreateDTO dto) {
        BOMItem entity = new BOMItem();
        entity.setParentPart(new ObjectReference(dto.getParentPart(), "Part"));
        entity.setChildPart(new ObjectReference(dto.getChildPart(), "Part"));
        entity.setQuantity(dto.getQuantity());
        entity.setUom(dto.getUom());
        entity.setFindNumber(dto.getFindNumber());
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    /**
     * Entity -> VO
     */
    private BOMItemVO convertToVO(BOMItem entity) {
        BOMItemVO vo = new BOMItemVO();
        vo.setId(entity.getId());
        
        // 处理 parentPart 引用
        if (entity.getParentPart() != null) {
            vo.setParentPart(entity.getParentPart().getId());
            vo.setParentPartName(entity.getParentPart().getDisplayName() != null 
                    ? entity.getParentPart().getDisplayName() 
                    : entity.getParentPart().getName());
        }
        
        // 处理 childPart 引用
        if (entity.getChildPart() != null) {
            vo.setChildPart(entity.getChildPart().getId());
            vo.setChildPartName(entity.getChildPart().getDisplayName() != null 
                    ? entity.getChildPart().getDisplayName() 
                    : entity.getChildPart().getName());
        }
        
        vo.setQuantity(entity.getQuantity());
        vo.setUom(entity.getUom());
        vo.setFindNumber(entity.getFindNumber());
        vo.setEffectiveFrom(entity.getEffectiveFrom());
        vo.setEffectiveTo(entity.getEffectiveTo());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());
        
        return vo;
    }
}
