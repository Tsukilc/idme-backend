package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.PartnerContactDao;
import com.tsukilc.idme.dto.PartnerContactCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.PartnerContact;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.PartnerContactVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联系人管理服务
 */
@Service
@Slf4j
public class PartnerContactService {
    
    @Autowired
    private PartnerContactDao partnerContactDao;
    
    /**
     * 创建联系人
     */
    public PartnerContactVO create(PartnerContactCreateDTO dto) {
        log.info("创建联系人: {}", dto);
        
        // DTO -> Entity
        PartnerContact entity = convertToEntity(dto);
        
        // 调用 DAO 创建
        PartnerContact created = partnerContactDao.create(entity);
        
        // Entity -> VO
        return convertToVO(created);
    }
    
    /**
     * 更新联系人
     */
    public PartnerContactVO update(String id, PartnerContactCreateDTO dto) {
        log.info("更新联系人，ID: {}, DTO: {}", id, dto);
        
        // 查询现有实体
        PartnerContact existing = partnerContactDao.findById(id);
        if (existing == null) {
            throw new IdmeException("联系人不存在: " + id);
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
        PartnerContact updated = partnerContactDao.update(existing);
        
        return convertToVO(updated);
    }
    
    /**
     * 删除联系人
     */
    public void delete(String id) {
        log.info("删除联系人，ID: {}", id);
        partnerContactDao.delete(id);
    }
    
    /**
     * 按 ID 查询联系人详情
     */
    public PartnerContactVO getById(String id) {
        log.info("查询联系人详情，ID: {}", id);
        PartnerContact entity = partnerContactDao.findById(id);
        if (entity == null) {
            throw new IdmeException("联系人不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询联系人列表
     */
    public PageResult<PartnerContactVO> list(int pageNum, int pageSize) {
        log.info("分页查询联系人列表，pageNum: {}, pageSize: {}", pageNum, pageSize);

        List<PartnerContact> entities = partnerContactDao.findAll(pageNum, pageSize);
        List<PartnerContactVO> vos = entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());

        PageResult<PartnerContactVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 按往来单位查询联系人列表
     */
    public List<PartnerContactVO> getByPartner(String partnerId) {
        log.info("查询往来单位的联系人列表，往来单位ID: {}", partnerId);

        // 构建查询条件：partner = partnerId
        Map<String, Object> condition = new HashMap<>();
        condition.put("partner", partnerId);

        // 执行查询（查询所有，不分页）
        List<PartnerContact> entities = partnerContactDao.findByCondition(condition, 1, 1000);

        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * Entity -> VO 转换
     */
    private PartnerContactVO convertToVO(PartnerContact entity) {
        PartnerContactVO vo = new PartnerContactVO();
        vo.setId(entity.getId());
        vo.setContactName(entity.getContactName());
        vo.setMobile(entity.getMobile());
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        vo.setRole(entity.getRole());
        vo.setRemarks(entity.getRemarks());

        return vo;
    }
    
    /**
     * DTO -> Entity 转换
     */
    private PartnerContact convertToEntity(PartnerContactCreateDTO dto) {
        PartnerContact entity = new PartnerContact();
        entity.setContactName(dto.getContactName());
        entity.setMobile(dto.getMobile());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setRemarks(dto.getRemarks());
        
        // 处理参考对象字段
        if (dto.getPartner() != null && !dto.getPartner().isEmpty()) {
            entity.setPartner(new ObjectReference(dto.getPartner(), "BusinessPartner"));
        }
        
        return entity;
    }
    
    /**
     * 更新实体字段
     */
    private void updateEntityFromDTO(PartnerContact entity, PartnerContactCreateDTO dto) {
        if (dto.getPartner() != null) {
            if (dto.getPartner().isEmpty()) {
                entity.setPartner(null);
            } else {
                entity.setPartner(new ObjectReference(dto.getPartner(), "BusinessPartner"));
            }
        }
        if (dto.getContactName() != null) {
            entity.setContactName(dto.getContactName());
        }
        if (dto.getMobile() != null) {
            entity.setMobile(dto.getMobile());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
    }
}
