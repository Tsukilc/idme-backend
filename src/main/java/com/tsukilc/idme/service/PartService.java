package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.PartDao;
import com.tsukilc.idme.dto.PartCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.Part;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.PartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料管理服务
 * 
 * 敏捷开发策略：
 * 1. 当前实现：基本CRUD功能
 * 2. 版本管理功能：标注TODO，后续迭代实现
 */
@Service
@Slf4j
public class PartService {
    
    @Autowired
    private PartDao partDao;
    
    /**
     * 创建物料（自动创建主对象和第一个版本）
     * 
     * TODO: [版本管理-V2] 完整的版本对象创建流程
     * - [ ] 支持指定初始版本号
     * - [ ] 支持业务版本与系统版本的映射关系
     * - [ ] 返回完整的版本信息（master/branch/version）
     */
    public PartVO create(PartCreateDTO dto) {
        log.info("创建物料: {}", dto);
        
        // DTO 转 Entity
        Part entity = convertToEntity(dto);
        
        // 调用 DAO 创建（SDK会自动处理版本对象的创建）
        Part created = partDao.create(entity);
        
        // Entity 转 VO 返回
        return convertToVO(created);
    }
    
    /**
     * 更新物料（当前：直接更新，未来：创建新版本）
     * 
     * TODO: [版本管理-V2] 版本更新流程
     * - [ ] Checkout（检出）：创建工作副本
     * - [ ] 修改数据
     * - [ ] Checkin（检入）：保存为新版本（V1.0 -> V1.1）
     * - [ ] 支持版本锁定（防止并发修改）
     */
    public PartVO update(String id, PartCreateDTO dto) {
        log.info("更新物料，ID: {}, 数据: {}", id, dto);
        
        // 查询现有实体
        Part existing = partDao.findById(id);
        if (existing == null) {
            throw new IdmeException("物料不存在: " + id);
        }
        
        // 更新字段
        updateEntityFromDTO(existing, dto);
        
        // 调用 DAO 更新
        Part updated = partDao.update(existing);
        
        // 返回 VO
        return convertToVO(updated);
    }
    
    /**
     * 删除物料（删除整个主对象及其所有版本）
     * 
     * TODO: [版本管理-V2] 版本删除策略
     * - [ ] 支持删除单个版本
     * - [ ] 支持删除整个主对象
     * - [ ] 删除前检查是否有引用（BOM等）
     */
    public void delete(String id) {
        log.info("删除物料，ID: {}", id);
        partDao.delete(id);
    }
    
    /**
     * 根据ID查询物料（查询指定版本）
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
     * 分页查询物料列表（查询所有最新版本）
     * 
     * TODO: [版本管理-V2] 查询策略优化
     * - [ ] 默认只查询最新版本（latest=true）
     * - [ ] 提供查询所有版本的接口
     * - [ ] 支持按版本号、业务版本筛选
     */
    public List<PartVO> list(int pageNum, int pageSize) {
        log.info("分页查询物料列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        // 当前：查询所有版本（未来：只查latest=true）
        List<Part> entities = partDao.findLatest(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 按物料编号查询（查询该编号下的所有版本）
     * 
     * TODO: [版本管理-V2] 版本历史查询
     * - [ ] 返回版本列表，按时间排序
     * - [ ] 标注哪个是当前使用版本
     * - [ ] 支持版本对比功能
     */
    public List<PartVO> findByPartNumber(String partNumber) {
        log.info("按物料编号查询，partNumber: {}", partNumber);
        List<Part> entities = partDao.findByPartNumber(partNumber, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    // ==================== 未来版本管理功能（预留） ====================
    
    /**
     * TODO: [版本管理-V2] 检出物料（创建工作副本）
     * 
     * @param masterId 主对象ID
     * @return 工作副本
     */
    // public PartVO checkout(String masterId) {
    //     // 调用 SDK 的 checkout API
    //     // 创建一个工作副本，workingCopy=true, workingState=CHECKED_OUT
    // }
    
    /**
     * TODO: [版本管理-V2] 检入物料（保存为新版本）
     * 
     * @param masterId 主对象ID
     * @param dto 修改后的数据
     * @return 新版本
     */
    // public PartVO checkin(String masterId, PartCreateDTO dto) {
    //     // 调用 SDK 的 checkin API
    //     // 小版本升级：V1.0 -> V1.1
    // }
    
    /**
     * TODO: [版本管理-V2] 修订物料（创建新的大版本）
     * 
     * @param masterId 主对象ID
     * @return 新的大版本（如从 1.x 升级到 2.x）
     */
    // public PartVO revise(String masterId) {
    //     // 调用 SDK 的 revise API
    //     // 大版本升级：V1.x -> V2.0
    // }
    
    /**
     * TODO: [版本管理-V2] 撤销检出
     * 
     * @param masterId 主对象ID
     */
    // public void undoCheckout(String masterId) {
    //     // 调用 SDK 的 undoCheckout API
    //     // 删除工作副本，恢复到检出前的状态
    // }
    
    /**
     * TODO: [版本管理-V2] 查询版本历史
     * 
     * @param masterId 主对象ID
     * @return 所有版本列表
     */
    // public List<PartVO> getVersionHistory(String masterId) {
    //     // 按时间倒序返回所有版本
    //     // 标注哪个是 latest, latestVersion, workingCopy
    // }
    
    /**
     * TODO: [版本管理-V2] 版本对比
     * 
     * @param versionId1 版本1 ID
     * @param versionId2 版本2 ID
     * @return 差异对比结果
     */
    // public VersionCompareResult compareVersions(String versionId1, String versionId2) {
    //     // 返回两个版本的字段差异
    // }
    
    // ==================== 转换方法 ====================
    
    /**
     * Entity -> VO 转换
     */
    private PartVO convertToVO(Part entity) {
        PartVO vo = new PartVO();
        vo.setId(entity.getId());
        
        // 主对象ID（版本管理用）
        if (entity.getMaster() != null) {
            vo.setMasterId(entity.getMaster().getId());
        }
        
        vo.setPartNumber(entity.getPartNumber());
        vo.setPartName(entity.getPartName());
        vo.setModelSpec(entity.getModelSpec());
        vo.setStockQty(entity.getStockQty());
        // 从ObjectReference提取ID（TODO: 扩展为返回完整对象信息）
        vo.setUnit(entity.getUnit() != null ? entity.getUnit().getId() : null);
        vo.setSupplierName(entity.getSupplierName() != null ? entity.getSupplierName().getId() : null);
        vo.setCategory(entity.getCategory() != null ? entity.getCategory().getId() : null);
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
     * DTO -> Entity 转换（用于创建）
     */
    private Part convertToEntity(PartCreateDTO dto) {
        Part entity = new Part();
        entity.setPartNumber(dto.getPartNumber());
        entity.setPartName(dto.getPartName());
        entity.setModelSpec(dto.getModelSpec());
        entity.setStockQty(dto.getStockQty());
        // 将字符串ID转为ObjectReference
        if (dto.getUnit() != null) {
            entity.setUnit(new ObjectReference(dto.getUnit()));
        }
        if (dto.getSupplierName() != null) {
            entity.setSupplierName(new ObjectReference(dto.getSupplierName()));
        }
        if (dto.getCategory() != null) {
            entity.setCategory(new ObjectReference(dto.getCategory()));
        }
        entity.setBusinessVersion(dto.getBusinessVersion());
        entity.setDescription(dto.getDescription());
        entity.setDrawingUrl(dto.getDrawingUrl());
        entity.setExtra(dto.getExtra());
        
        // VersionObject 必需字段：master 和 branch
        // 首次创建时传空对象，SDK会自动创建主对象和分支对象
        ObjectReference master = new ObjectReference();
        ObjectReference branch = new ObjectReference();
        entity.setMaster(master);
        entity.setBranch(branch);
        
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
            entity.setUnit(new ObjectReference(dto.getUnit()));
        }
        if (dto.getSupplierName() != null) {
            entity.setSupplierName(new ObjectReference(dto.getSupplierName()));
        }
        if (dto.getCategory() != null) {
            entity.setCategory(new ObjectReference(dto.getCategory()));
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
}
