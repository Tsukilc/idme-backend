package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.EquipmentModelDao;
import com.tsukilc.idme.dto.EquipmentModelCreateDTO;
import com.tsukilc.idme.entity.EquipmentModel;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.vo.EquipmentModelVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备机型服务层
 */
@Service
public class EquipmentModelService {

    private final EquipmentModelDao equipmentModelDao;

    public EquipmentModelService(EquipmentModelDao equipmentModelDao) {
        this.equipmentModelDao = equipmentModelDao;
    }

    /**
     * 创建设备机型
     */
    public String create(EquipmentModelCreateDTO dto) {
        EquipmentModel entity = convertToEntity(dto);
        EquipmentModel created = equipmentModelDao.create(entity);
        return created.getId();
    }

    /**
     * 分页查询设备机型
     */
    public PageResult<EquipmentModelVO> list(int pageNum, int pageSize) {
        List<EquipmentModel> list = equipmentModelDao.findAll(pageNum, pageSize);
        List<EquipmentModelVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<EquipmentModelVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 根据ID查询设备机型详情
     */
    public EquipmentModelVO getById(String id) {
        EquipmentModel entity = equipmentModelDao.findById(id);
        return convertToVO(entity);
    }

    /**
     * 更新设备机型
     */
    public void update(String id, EquipmentModelCreateDTO dto) {
        EquipmentModel entity = convertToEntity(dto);
        entity.setId(id);
        equipmentModelDao.update(entity);
    }

    /**
     * 删除设备机型
     */
    public void delete(String id) {
        equipmentModelDao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private EquipmentModel convertToEntity(EquipmentModelCreateDTO dto) {
        EquipmentModel entity = new EquipmentModel();
        entity.setModelCode(dto.getModelCode());
        entity.setModelName(dto.getModelName());
        
        // 处理 manufacturer 引用
        if (StringUtils.hasText(dto.getManufacturer())) {
            entity.setManufacturer(new ObjectReference(dto.getManufacturer(), "BusinessPartner"));
        }
        
        entity.setBrand(dto.getBrand());
        entity.setModelSpec(dto.getModelSpec());
        entity.setCategory(dto.getCategory());
        entity.setDefaultTechParams(dto.getDefaultTechParams());
        entity.setRemarks(dto.getRemarks());
        
        return entity;
    }

    /**
     * Entity -> VO
     */
    private EquipmentModelVO convertToVO(EquipmentModel entity) {
        EquipmentModelVO vo = new EquipmentModelVO();
        vo.setId(entity.getId());
        vo.setModelCode(entity.getModelCode());
        vo.setModelName(entity.getModelName());
        
        // 处理 manufacturer 引用
        if (entity.getManufacturer() != null) {
            vo.setManufacturer(entity.getManufacturer().getId());
            vo.setManufacturerName(entity.getManufacturer().getDisplayName() != null 
                    ? entity.getManufacturer().getDisplayName() 
                    : entity.getManufacturer().getName());
        }
        
        vo.setBrand(entity.getBrand());
        vo.setModelSpec(entity.getModelSpec());
        vo.setCategory(entity.getCategory());
        vo.setDefaultTechParams(entity.getDefaultTechParams());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());
        
        return vo;
    }
}
