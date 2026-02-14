package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.BusinessPartnerDao;
import com.tsukilc.idme.dto.BusinessPartnerCreateDTO;
import com.tsukilc.idme.entity.BusinessPartner;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.BusinessPartnerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 往来单位管理服务
 */
@Service
@Slf4j
public class BusinessPartnerService {
    
    @Autowired
    private BusinessPartnerDao businessPartnerDao;
    
    /**
     * 创建往来单位
     */
    public BusinessPartnerVO create(BusinessPartnerCreateDTO dto) {
        log.info("创建往来单位: {}", dto);
        
        // DTO -> Entity
        BusinessPartner entity = convertToEntity(dto);
        
        // 调用 DAO 创建
        BusinessPartner created = businessPartnerDao.create(entity);
        
        // Entity -> VO
        return convertToVO(created);
    }
    
    /**
     * 更新往来单位
     */
    public BusinessPartnerVO update(String id, BusinessPartnerCreateDTO dto) {
        log.info("更新往来单位，ID: {}, DTO: {}", id, dto);
        
        // 查询现有实体
        BusinessPartner existing = businessPartnerDao.findById(id);
        if (existing == null) {
            throw new IdmeException("往来单位不存在: " + id);
        }
        
        // 更新字段
        updateEntityFromDTO(existing, dto);
        existing.setId(id);  // 确保ID不变
        
        // 清空系统字段，避免SDK报错（SDK不允许修改这些字段）
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);
        
        // 调用 DAO 更新
        BusinessPartner updated = businessPartnerDao.update(existing);
        
        // 返回 VO
        return convertToVO(updated);
    }
    
    /**
     * 删除往来单位
     */
    public void delete(String id) {
        log.info("删除往来单位，ID: {}", id);
        businessPartnerDao.delete(id);
    }
    
    /**
     * 查询往来单位详情
     */
    public BusinessPartnerVO getById(String id) {
        log.info("查询往来单位详情，ID: {}", id);
        BusinessPartner entity = businessPartnerDao.findById(id);
        if (entity == null) {
            throw new IdmeException("往来单位不存在: " + id);
        }
        return convertToVO(entity);
    }
    
    /**
     * 查询往来单位列表
     */
    public List<BusinessPartnerVO> list(int pageNum, int pageSize) {
        log.info("查询往来单位列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<BusinessPartner> entities = businessPartnerDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 按类型查询往来单位
     */
    public List<BusinessPartnerVO> getByType(String partnerType) {
        log.info("按类型查询往来单位，类型: {}", partnerType);
        Map<String, Object> condition = new HashMap<>();
        condition.put("partnerType", partnerType);
        List<BusinessPartner> entities = businessPartnerDao.findByCondition(condition, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * Entity -> VO 转换
     */
    private BusinessPartnerVO convertToVO(BusinessPartner entity) {
        BusinessPartnerVO vo = new BusinessPartnerVO();
        vo.setId(entity.getId());
        vo.setPartnerCode(entity.getPartnerCode());
        vo.setPartnerName(entity.getPartnerName());
        // 从PartnerTypeRef提取enName（英文名）
        vo.setPartnerType(entity.getPartnerType() != null ? entity.getPartnerType().getEnName() : null);
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        vo.setWebsite(entity.getWebsite());
        vo.setAddressText(entity.getAddressText());
        vo.setExtra(entity.getExtra());
        return vo;
    }
    
    /**
     * DTO -> Entity 转换
     */
    private BusinessPartner convertToEntity(BusinessPartnerCreateDTO dto) {
        BusinessPartner entity = new BusinessPartner();
        entity.setPartnerCode(dto.getPartnerCode());
        entity.setPartnerName(dto.getPartnerName());
        // 将String转换为PartnerTypeRef（SDK create时需要String）
        entity.setPartnerType(dto.getPartnerType() != null ? new com.tsukilc.idme.entity.PartnerTypeRef(dto.getPartnerType()) : null);
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setWebsite(dto.getWebsite());
        entity.setAddressText(dto.getAddressText());
        entity.setExtra(dto.getExtra());
        return entity;
    }
    
    /**
     * 更新实体字段
     */
    private void updateEntityFromDTO(BusinessPartner entity, BusinessPartnerCreateDTO dto) {
        if (dto.getPartnerCode() != null) {
            entity.setPartnerCode(dto.getPartnerCode());
        }
        if (dto.getPartnerName() != null) {
            entity.setPartnerName(dto.getPartnerName());
        }
        if (dto.getPartnerType() != null) {
            entity.setPartnerType(new com.tsukilc.idme.entity.PartnerTypeRef(dto.getPartnerType()));
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getWebsite() != null) {
            entity.setWebsite(dto.getWebsite());
        }
        if (dto.getAddressText() != null) {
            entity.setAddressText(dto.getAddressText());
        }
        if (dto.getExtra() != null) {
            entity.setExtra(dto.getExtra());
        }
    }
}
