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

        // 处理 category 引用
        if (StringUtils.hasText(dto.getCategory())) {
            entity.setCategory(new ObjectReference(dto.getCategory(), "EquipmentClassfication"));
        }

        entity.setTechParams(dto.getTechParams());
        entity.setRemarks(dto.getRemarks());

        return entity;
    }

    /**
     * Entity -> VO（严格遵循openapi.yaml）
     */
    private EquipmentVO convertToVO(Equipment entity) {
        EquipmentVO vo = new EquipmentVO();
        vo.setId(entity.getId());
        vo.setEquipmentCode(entity.getEquipmentCode());
        vo.setEquipmentName(entity.getEquipmentName());

        // 处理 manufacturerName 引用（只保存ID）
        if (entity.getManufacturerName() != null) {
            vo.setManufacturerName(entity.getManufacturerName().getId());
        }

        vo.setBrand(entity.getBrand());
        vo.setModelSpec(entity.getModelSpec());

        // 处理 supplierName 引用（只保存ID）
        if (entity.getSupplierName() != null) {
            vo.setSupplierName(entity.getSupplierName().getId());
        }

        vo.setProductionDate(entity.getProductionDate());
        vo.setServiceLifeYears(entity.getServiceLifeYears());
        vo.setDepreciationMethod(convertEnumField(entity.getDepreciationMethod()));
        vo.setLocationText(entity.getLocationText());

        // 处理 locationRef 引用（只保存ID）
        if (entity.getLocationRef() != null) {
            vo.setLocationRef(entity.getLocationRef().getId());
        }

        vo.setStatus(convertEnumField(entity.getStatus()));
        vo.setSerialNumber(entity.getSerialNumber());

        // 处理 category 引用（只保存ID）
        if (entity.getCategory() != null) {
            vo.setCategory(entity.getCategory().getId());
        }

        vo.setTechParams(entity.getTechParams());
        vo.setRemarks(entity.getRemarks());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastModifiedTime(entity.getLastUpdateTime());

        return vo;
    }

    /**
     * 转换枚举字段（处理SDK返回的Map结构）
     * SDK返回：{code:"XXX", cnName:"XXX", enName:"XXX", alias:"XXX"}
     * 期望输出：String (enName值)
     */
    private String convertEnumField(Object sdkEnum) {
        if (sdkEnum == null) {
            return null;
        }

        // SDK返回的是Map类型，提取enName字段
        if (sdkEnum instanceof java.util.Map) {
            Object enName = ((java.util.Map<?, ?>) sdkEnum).get("enName");
            if (enName != null) {
                return enName.toString();
            }
        }

        // 否则直接转换为字符串
        return sdkEnum.toString();
    }
}
