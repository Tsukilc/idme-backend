package com.tsukilc.idme.service;

import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dao.EquipmentDao;
import com.tsukilc.idme.dto.EquipmentCreateDTO;
import com.tsukilc.idme.entity.Equipment;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.vo.EquipmentVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备服务层
 * 竞赛核心模块 - 支持技术参数和备品备件扩展
 */
@Service
public class EquipmentService {

    private final EquipmentDao equipmentDao;

    public EquipmentService(EquipmentDao equipmentDao) {
        this.equipmentDao = equipmentDao;
    }

    /**
     * 创建设备
     */
    public String create(EquipmentCreateDTO dto) {
        Equipment entity = convertToEntity(dto);
        Equipment created = equipmentDao.create(entity);
        return created.getId();
    }

    /**
     * 分页查询设备
     */
    public PageResult<EquipmentVO> list(int pageNum, int pageSize) {
        List<Equipment> list = equipmentDao.findAll(pageNum, pageSize);
        List<EquipmentVO> vos = list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        PageResult<EquipmentVO> result = new PageResult<>();
        result.setTotal(vos.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setRecords(vos);
        return result;
    }

    /**
     * 根据ID查询设备详情
     */
    public EquipmentVO getById(String id) {
        Equipment entity = equipmentDao.findById(id);
        return convertToVO(entity);
    }

    /**
     * 更新设备
     */
    public void update(String id, EquipmentCreateDTO dto) {
        Equipment entity = convertToEntity(dto);
        entity.setId(id);
        equipmentDao.update(entity);
    }

    /**
     * 删除设备
     */
    public void delete(String id) {
        equipmentDao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private Equipment convertToEntity(EquipmentCreateDTO dto) {
        Equipment entity = new Equipment();
        entity.setEquipmentCode(dto.getEquipmentCode());
        entity.setEquipmentName(dto.getEquipmentName());
        
        // 处理 manufacturerName 引用
        if (StringUtils.hasText(dto.getManufacturerName())) {
            entity.setManufacturerName(new ObjectReference(dto.getManufacturerName(), "BusinessPartner"));
        }
        
        entity.setBrand(dto.getBrand());
        entity.setModelSpec(dto.getModelSpec());
        
        // 处理 equipmentModelRef 引用
        if (StringUtils.hasText(dto.getEquipmentModelRef())) {
            entity.setEquipmentModelRef(new ObjectReference(dto.getEquipmentModelRef(), "EquipmentModel"));
        }
        
        // 处理 supplierName 引用
        if (StringUtils.hasText(dto.getSupplierName())) {
            entity.setSupplierName(new ObjectReference(dto.getSupplierName(), "BusinessPartner"));
        }
        
        entity.setProductionDate(dto.getProductionDate());
        entity.setServiceLifeYears(dto.getServiceLifeYears());
        entity.setDepreciationMethod(dto.getDepreciationMethod());
        entity.setLocationText(dto.getLocationText());
        
        // 处理 locationRef 引用
        if (StringUtils.hasText(dto.getLocationRef())) {
            entity.setLocationRef(new ObjectReference(dto.getLocationRef(), "Location"));
        }
        
        entity.setStatus(dto.getStatus());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setCategory(dto.getCategory());
        entity.setTechParams(dto.getTechParams());
        entity.setRemarks(dto.getRemarks());
        
        return entity;
    }

    /**
     * Entity -> VO
     */
    private EquipmentVO convertToVO(Equipment entity) {
        EquipmentVO vo = new EquipmentVO();
        vo.setId(entity.getId());
        vo.setEquipmentCode(entity.getEquipmentCode());
        vo.setEquipmentName(entity.getEquipmentName());
        
        // 处理 manufacturerName 引用
        if (entity.getManufacturerName() != null) {
            vo.setManufacturerName(entity.getManufacturerName().getId());
            vo.setManufacturerDisplayName(entity.getManufacturerName().getDisplayName() != null 
                    ? entity.getManufacturerName().getDisplayName() 
                    : entity.getManufacturerName().getName());
        }
        
        vo.setBrand(entity.getBrand());
        vo.setModelSpec(entity.getModelSpec());
        
        // 处理 equipmentModelRef 引用
        if (entity.getEquipmentModelRef() != null) {
            vo.setEquipmentModelRef(entity.getEquipmentModelRef().getId());
            vo.setEquipmentModelName(entity.getEquipmentModelRef().getDisplayName() != null 
                    ? entity.getEquipmentModelRef().getDisplayName() 
                    : entity.getEquipmentModelRef().getName());
        }
        
        // 处理 supplierName 引用
        if (entity.getSupplierName() != null) {
            vo.setSupplierName(entity.getSupplierName().getId());
            vo.setSupplierDisplayName(entity.getSupplierName().getDisplayName() != null 
                    ? entity.getSupplierName().getDisplayName() 
                    : entity.getSupplierName().getName());
        }
        
        vo.setProductionDate(entity.getProductionDate());
        vo.setServiceLifeYears(entity.getServiceLifeYears());
        vo.setDepreciationMethod(entity.getDepreciationMethod());
        vo.setLocationText(entity.getLocationText());
        
        // 处理 locationRef 引用
        if (entity.getLocationRef() != null) {
            vo.setLocationRef(entity.getLocationRef().getId());
            vo.setLocationName(entity.getLocationRef().getDisplayName() != null 
                    ? entity.getLocationRef().getDisplayName() 
                    : entity.getLocationRef().getName());
        }
        
        vo.setStatus(entity.getStatus());
        vo.setSerialNumber(entity.getSerialNumber());
        vo.setCategory(entity.getCategory());
        vo.setTechParams(entity.getTechParams());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());
        
        return vo;
    }
}
