package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.UnitDao;
import com.tsukilc.idme.dto.UnitCreateDTO;
import com.tsukilc.idme.entity.Unit;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 计量单位管理服务
 */
@Service
@Slf4j
public class UnitService {
    
    @Autowired
    private UnitDao unitDao;
    
    /**
     * 创建计量单位
     */
    public UnitVO create(UnitCreateDTO dto) {
        log.info("创建计量单位: {}", dto);
        
        Unit entity = convertToEntity(dto);
        Unit created = unitDao.create(entity);
        
        return convertToVO(created);
    }
    
    /**
     * 更新计量单位
     */
    public UnitVO update(String id, UnitCreateDTO dto) {
        log.info("更新计量单位，ID: {}, DTO: {}", id, dto);
        
        Unit existing = unitDao.findById(id);
        if (existing == null) {
            throw new IdmeException("计量单位不存在: " + id);
        }
        
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
        
        Unit updated = unitDao.update(existing);
        return convertToVO(updated);
    }
    
    /**
     * 删除计量单位
     */
    public void delete(String id) {
        log.info("删除计量单位，ID: {}", id);
        unitDao.delete(id);
    }
    
    /**
     * 查询计量单位详情
     */
    public UnitVO getById(String id) {
        log.info("查询计量单位详情，ID: {}", id);
        Unit entity = unitDao.findById(id);
        if (entity == null) {
            throw new IdmeException("计量单位不存在: " + id);
        }
        return convertToVO(entity);
    }
    
    /**
     * 查询计量单位列表
     */
    public List<UnitVO> list(int pageNum, int pageSize) {
        log.info("查询计量单位列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<Unit> entities = unitDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * Entity -> VO 转换
     */
    private UnitVO convertToVO(Unit entity) {
        UnitVO vo = new UnitVO();
        vo.setId(entity.getId());
        vo.setUnitName(entity.getUnitName());
        vo.setUnitDisplayName(entity.getUnitDisplayName());
        vo.setUnitCategory(entity.getUnitCategory());
        vo.setUnitFactor(entity.getUnitFactor());
        vo.setMesurementSystem(entity.getMesurementSystem());
        return vo;
    }
    
    /**
     * DTO -> Entity 转换
     */
    private Unit convertToEntity(UnitCreateDTO dto) {
        Unit entity = new Unit();
        entity.setUnitName(dto.getUnitName());
        entity.setUnitDisplayName(dto.getUnitDisplayName());
        entity.setUnitCategory(dto.getUnitCategory());
        entity.setUnitFactor(dto.getUnitFactor());
        entity.setMesurementSystem(dto.getMesurementSystem());
        return entity;
    }
    
    /**
     * 更新实体字段
     */
    private void updateEntityFromDTO(Unit entity, UnitCreateDTO dto) {
        if (dto.getUnitName() != null) {
            entity.setUnitName(dto.getUnitName());
        }
        if (dto.getUnitDisplayName() != null) {
            entity.setUnitDisplayName(dto.getUnitDisplayName());
        }
        if (dto.getUnitCategory() != null) {
            entity.setUnitCategory(dto.getUnitCategory());
        }
        if (dto.getUnitFactor() != null) {
            entity.setUnitFactor(dto.getUnitFactor());
        }
        if (dto.getMesurementSystem() != null) {
            entity.setMesurementSystem(dto.getMesurementSystem());
        }
    }
}
