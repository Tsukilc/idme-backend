package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.LocationDao;
import com.tsukilc.idme.dto.LocationCreateDTO;
import com.tsukilc.idme.entity.Location;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.LocationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 位置管理服务
 * 支持树形结构
 */
@Service
@Slf4j
public class LocationService {
    
    @Autowired
    private LocationDao locationDao;
    
    /**
     * 创建位置
     */
    public LocationVO create(LocationCreateDTO dto) {
        log.info("创建位置: {}", dto);
        
        // DTO -> Entity
        Location entity = convertToEntity(dto);
        
        // 调用 DAO 创建
        Location created = locationDao.create(entity);
        
        // Entity -> VO
        return convertToVO(created);
    }
    
    /**
     * 更新位置
     */
    public LocationVO update(String id, LocationCreateDTO dto) {
        log.info("更新位置，ID: {}, DTO: {}", id, dto);
        
        // 查询现有实体
        Location existing = locationDao.findById(id);
        if (existing == null) {
            throw new IdmeException("位置不存在: " + id);
        }
        
        // 更新字段
        updateEntityFromDTO(existing, dto);
        existing.setId(id);
        
        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);
        
        // 调用 DAO 更新
        Location updated = locationDao.update(existing);
        
        return convertToVO(updated);
    }
    
    /**
     * 删除位置
     */
    public void delete(String id) {
        log.info("删除位置，ID: {}", id);
        locationDao.delete(id);
    }
    
    /**
     * 查询位置详情
     */
    public LocationVO getById(String id) {
        log.info("查询位置详情，ID: {}", id);
        Location entity = locationDao.findById(id);
        if (entity == null) {
            throw new IdmeException("位置不存在: " + id);
        }
        return convertToVO(entity);
    }
    
    /**
     * 查询位置列表（分页）
     */
    public List<LocationVO> list(int pageNum, int pageSize) {
        log.info("查询位置列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<Location> entities = locationDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 查询位置树
     * 返回树形结构的位置列表（根节点列表，每个根节点包含其子节点）
     */
    public List<LocationTreeVO> getTree() {
        log.info("查询位置树");
        
        // 查询所有位置（分页大小设置较大，假设位置总数不会超过1000）
        List<Location> allLocations = locationDao.findAll(1, 1000);
        
        // 转换为VO
        List<LocationTreeVO> allVOs = allLocations.stream()
            .map(this::convertToTreeVO)
            .collect(Collectors.toList());
        
        // 构建树形结构
        return buildTree(allVOs);
    }
    
    /**
     * 构建树形结构
     * @param allNodes 所有节点的扁平列表
     * @return 根节点列表（每个根节点包含其子树）
     */
    private List<LocationTreeVO> buildTree(List<LocationTreeVO> allNodes) {
        // 1. 创建ID到节点的映射
        Map<String, LocationTreeVO> nodeMap = new HashMap<>();
        for (LocationTreeVO node : allNodes) {
            nodeMap.put(node.getId(), node);
            node.setChildren(new ArrayList<>()); // 初始化子节点列表
        }
        
        // 2. 构建树形关系
        List<LocationTreeVO> roots = new ArrayList<>();
        for (LocationTreeVO node : allNodes) {
            if (node.getParentLocation() == null || node.getParentLocation().isEmpty()) {
                // 没有父节点，是根节点
                roots.add(node);
            } else {
                // 有父节点，添加到父节点的children中
                LocationTreeVO parent = nodeMap.get(node.getParentLocation());
                if (parent != null) {
                    parent.getChildren().add(node);
                } else {
                    // 父节点不存在，也视为根节点
                    log.warn("位置 {} 的父节点 {} 不存在", node.getId(), node.getParentLocation());
                    roots.add(node);
                }
            }
        }
        
        return roots;
    }
    
    /**
     * Entity -> VO 转换
     */
    private LocationVO convertToVO(Location entity) {
        LocationVO vo = new LocationVO();
        vo.setId(entity.getId());
        vo.setLocationCode(entity.getLocationCode());
        vo.setLocationName(entity.getLocationName());
        vo.setLocationType(entity.getLocationType());
        vo.setAddressText(entity.getAddressText());
        vo.setRemarks(entity.getRemarks());
        
        // 处理参考对象字段
        if (entity.getParentLocation() != null) {
            vo.setParentLocation(entity.getParentLocation().getId());
            vo.setParentLocationName(entity.getParentLocation().getName());
        }
        
        if (entity.getManager() != null) {
            vo.setManager(entity.getManager().getId());
            vo.setManagerName(entity.getManager().getName());
        }
        
        return vo;
    }
    
    /**
     * Entity -> TreeVO 转换
     */
    private LocationTreeVO convertToTreeVO(Location entity) {
        LocationTreeVO vo = new LocationTreeVO();
        vo.setId(entity.getId());
        vo.setLocationCode(entity.getLocationCode());
        vo.setLocationName(entity.getLocationName());
        vo.setLocationType(entity.getLocationType());
        
        // 只保存父节点ID，用于构建树
        if (entity.getParentLocation() != null) {
            vo.setParentLocation(entity.getParentLocation().getId());
        }
        
        return vo;
    }
    
    /**
     * DTO -> Entity 转换
     */
    private Location convertToEntity(LocationCreateDTO dto) {
        Location entity = new Location();
        entity.setLocationCode(dto.getLocationCode());
        entity.setLocationName(dto.getLocationName());
        entity.setLocationType(dto.getLocationType());
        entity.setAddressText(dto.getAddressText());
        entity.setRemarks(dto.getRemarks());
        
        // 处理参考对象字段
        if (dto.getParentLocation() != null && !dto.getParentLocation().isEmpty()) {
            entity.setParentLocation(new ObjectReference(dto.getParentLocation(), "Location"));
        }
        
        if (dto.getManager() != null && !dto.getManager().isEmpty()) {
            entity.setManager(new ObjectReference(dto.getManager(), "Employee"));
        }
        
        return entity;
    }
    
    /**
     * 更新实体字段
     */
    private void updateEntityFromDTO(Location entity, LocationCreateDTO dto) {
        if (dto.getLocationCode() != null) {
            entity.setLocationCode(dto.getLocationCode());
        }
        if (dto.getLocationName() != null) {
            entity.setLocationName(dto.getLocationName());
        }
        if (dto.getLocationType() != null) {
            entity.setLocationType(dto.getLocationType());
        }
        if (dto.getAddressText() != null) {
            entity.setAddressText(dto.getAddressText());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
        
        // 更新参考对象
        if (dto.getParentLocation() != null) {
            if (dto.getParentLocation().isEmpty()) {
                entity.setParentLocation(null);
            } else {
                entity.setParentLocation(new ObjectReference(dto.getParentLocation(), "Location"));
            }
        }
        
        if (dto.getManager() != null) {
            if (dto.getManager().isEmpty()) {
                entity.setManager(null);
            } else {
                entity.setManager(new ObjectReference(dto.getManager(), "Employee"));
            }
        }
    }
    
    /**
     * 位置树节点 VO（内部类）
     */
    @lombok.Data
    public static class LocationTreeVO {
        private String id;
        private String locationCode;
        private String locationName;
        private String locationType;
        private String parentLocation;  // 仅用于构建树，不返回给前端
        private List<LocationTreeVO> children;
    }
}
