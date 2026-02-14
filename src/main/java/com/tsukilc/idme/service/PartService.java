package com.tsukilc.idme.service;

import com.tsukilc.idme.client.IdmeSdkClient;
import com.tsukilc.idme.dao.PartDao;
import com.tsukilc.idme.dto.PartCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.Part;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.PartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物料管理服务（版本对象）
 * 支持checkout/checkin工作流
 */
@Service
@Slf4j
public class PartService {

    @Autowired
    private PartDao partDao;

    @Autowired
    private IdmeSdkClient sdkClient;

    /**
     * 创建物料（自动创建主对象和第一个版本）
     */
    public PartVO create(PartCreateDTO dto) {
        log.info("创建物料: {}", dto);

        Part entity = convertToEntity(dto);

        // 初始化版本对象必需字段
        ObjectReference master = new ObjectReference();
        Map<String, Object> masterTenant = new HashMap<>();
        masterTenant.put("name", "basicTenant");
        masterTenant.put("id", "-1");
        masterTenant.put("clazz", "Tenant");
        master.setTenant(masterTenant);
        master.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "rdmExtensionType", "creator", "id"));

        ObjectReference branch = new ObjectReference();
        Map<String, Object> branchTenant = new HashMap<>();
        branchTenant.put("name", "basicTenant");
        branchTenant.put("id", "-1");
        branchTenant.put("clazz", "Tenant");
        branch.setTenant(branchTenant);
        branch.setNeedSetNullAttrs(java.util.Arrays.asList("modifier", "creator", "rdmExtensionType", "id"));

        entity.setMaster(master);
        entity.setBranch(branch);
        entity.setSecurityLevel("internal");

        Part created = partDao.create(entity);
        return convertToVO(created);
    }

    /**
     * 检出物料（创建工作副本）
     */
    public PartVO checkout(String masterId) {
        log.info("检出物料，masterId: {}", masterId);
        Part workingCopy = sdkClient.checkout("Part", masterId, "BOTH", Part.class);
        return convertToVO(workingCopy);
    }

    /**
     * 检入物料（保存为新版本）
     */
    public PartVO checkin(String masterId) {
        log.info("检入物料，masterId: {}", masterId);
        Part newVersion = sdkClient.checkin("Part", masterId, "", Part.class);
        return convertToVO(newVersion);
    }

    /**
     * 更新物料（必须先检出，修改后再检入）
     * 此方法用于更新已检出的工作副本
     */
    public PartVO update(String id, PartCreateDTO dto) {
        log.info("更新物料，ID: {}, 数据: {}", id, dto);

        Part existing = partDao.findById(id);
        if (existing == null) {
            throw new IdmeException("物料不存在: " + id);
        }

        // 更新字段（保留版本管理字段）
        updateEntityFromDTO(existing, dto);

        // 保留完整的版本信息
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);
        existing.setRdmVersion(null);

        // 清空版本对象系统字段
        existing.setCheckOutTime(null);
        existing.setCheckOutUserName(null);
        existing.setPreVersionId(null);

        Part updated = partDao.update(existing);
        return convertToVO(updated);
    }

    /**
     * 查询版本历史
     */
    public List<PartVO> getVersionHistory(String masterId) {
        log.info("查询物料版本历史，masterId: {}", masterId);
        List<Part> versions = sdkClient.getVersionHistory("Part", masterId, Part.class);
        return versions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 删除物料
     */
    public void delete(String id) {
        log.info("删除物料，ID: {}", id);
        partDao.delete(id);
    }

    /**
     * 根据ID查询物料
     */
    public PartVO getById(String id) {
        log.info("查询物料详情，ID: {}", id);
        Part entity = partDao.findById(id);
        if (entity == null) {
            throw new IdmeException("物料不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询物料列表（只查询最新版本）
     */
    public List<PartVO> list(int pageNum, int pageSize) {
        log.info("分页查询物料列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<Part> entities = partDao.findLatest(pageNum, pageSize);
        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 按物料编号查询
     */
    public List<PartVO> findByPartNumber(String partNumber) {
        log.info("按物料编号查询，partNumber: {}", partNumber);
        List<Part> entities = partDao.findByPartNumber(partNumber, 1, 1000);
        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 物料库存统计
     * 返回所有物料的库存数量（ID -> stockQty）
     */
    public Map<String, Integer> getStockStatistics() {
        log.info("查询物料库存统计");
        List<Part> entities = partDao.findLatest(1, 1000);  // 只查询最新版本
        Map<String, Integer> stats = new HashMap<>();

        for (Part part : entities) {
            if (part.getStockQty() != null) {
                stats.put(part.getId(), part.getStockQty());
            }
        }

        log.info("库存统计完成，共 {} 个物料", stats.size());
        return stats;
    }

    /**
     * Entity -> VO 转换（严格遵循openapi.yaml）
     */
    private PartVO convertToVO(Part entity) {
        PartVO vo = new PartVO();
        vo.setId(entity.getId());

        // 主对象ID
        if (entity.getMaster() != null) {
            vo.setMasterId(entity.getMaster().getId());
        }

        vo.setPartNumber(entity.getPartNumber());
        vo.setPartName(entity.getPartName());
        vo.setModelSpec(entity.getModelSpec());
        vo.setStockQty(entity.getStockQty());

        // 计量单位ID
        if (entity.getUnit() != null) {
            vo.setUnit(entity.getUnit().getId());
        }

        // 供应商ID
        if (entity.getSupplierName() != null) {
            vo.setSupplierName(entity.getSupplierName().getId());
        }

        // 分类ID
        if (entity.getCategory() != null) {
            vo.setCategory(entity.getCategory().getId());
        }

        vo.setBusinessVersion(entity.getBusinessVersion());
        vo.setVersionNumber(entity.getVersion());  // SDK的version字段映射为versionNumber
        vo.setDescription(entity.getDescription());
        vo.setDrawingUrl(entity.getDrawingUrl());
        vo.setExtra(entity.getExtra());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastModifiedTime(entity.getLastUpdateTime());

        return vo;
    }

    /**
     * DTO -> Entity 转换
     */
    private Part convertToEntity(PartCreateDTO dto) {
        Part entity = new Part();
        entity.setPartNumber(dto.getPartNumber());
        entity.setPartName(dto.getPartName());
        entity.setModelSpec(dto.getModelSpec());
        entity.setStockQty(dto.getStockQty());

        if (dto.getUnit() != null) {
            ObjectReference unitRef = new ObjectReference(dto.getUnit(), "Unit");
            entity.setUnit(unitRef);
        }

        if (dto.getSupplierName() != null) {
            ObjectReference supplierRef = new ObjectReference(dto.getSupplierName(), "BusinessPartner");
            entity.setSupplierName(supplierRef);
        }

        if (dto.getCategory() != null) {
            ObjectReference categoryRef = new ObjectReference(dto.getCategory(), "PartClassfication");
            entity.setCategory(categoryRef);
        }

        entity.setBusinessVersion(dto.getBusinessVersion());
        entity.setDescription(dto.getDescription());
        entity.setDrawingUrl(dto.getDrawingUrl());
        entity.setExtra(dto.getExtra());

        return entity;
    }

    /**
     * 从 DTO 更新 Entity 字段
     */
    private void updateEntityFromDTO(Part entity, PartCreateDTO dto) {
        if (dto.getPartNumber() != null) {
            entity.setPartNumber(dto.getPartNumber());
        }
        if (dto.getPartName() != null) {
            entity.setPartName(dto.getPartName());
        }
        if (dto.getModelSpec() != null) {
            entity.setModelSpec(dto.getModelSpec());
        }
        if (dto.getStockQty() != null) {
            entity.setStockQty(dto.getStockQty());
        }
        if (dto.getUnit() != null) {
            entity.setUnit(new ObjectReference(dto.getUnit(), "Unit"));
        }
        if (dto.getSupplierName() != null) {
            entity.setSupplierName(new ObjectReference(dto.getSupplierName(), "BusinessPartner"));
        }
        if (dto.getCategory() != null) {
            entity.setCategory(new ObjectReference(dto.getCategory(), "PartClassfication"));
        }
        if (dto.getBusinessVersion() != null) {
            entity.setBusinessVersion(dto.getBusinessVersion());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getDrawingUrl() != null) {
            entity.setDrawingUrl(dto.getDrawingUrl());
        }
        if (dto.getExtra() != null) {
            entity.setExtra(dto.getExtra());
        }
    }

    /**
     * 转换枚举字段（处理SDK返回的Map结构）
     */
    private String convertEnumField(Object sdkEnum) {
        if (sdkEnum == null) {
            return null;
        }

        if (sdkEnum instanceof Map) {
            Object enName = ((Map<?, ?>) sdkEnum).get("enName");
            if (enName != null) {
                return enName.toString();
            }
        }

        return sdkEnum.toString();
    }
}
