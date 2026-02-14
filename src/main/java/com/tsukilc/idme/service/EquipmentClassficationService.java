package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.EquipmentClassficationDao;
import com.tsukilc.idme.dto.EquipmentClassficationCreateDTO;
import com.tsukilc.idme.entity.EquipmentClassfication;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.EquipmentClassficationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备分类服务
 */
@Service
@Slf4j
public class EquipmentClassficationService {

    @Autowired
    private EquipmentClassficationDao equipmentClassficationDao;

    /**
     * 创建设备分类
     */
    public EquipmentClassficationVO create(EquipmentClassficationCreateDTO dto) {
        log.info("创建设备分类: {}", dto);

        EquipmentClassfication entity = convertToEntity(dto);
        EquipmentClassfication created = equipmentClassficationDao.create(entity);

        return convertToVO(created);
    }

    /**
     * 更新设备分类
     */
    public EquipmentClassficationVO update(String id, EquipmentClassficationCreateDTO dto) {
        log.info("更新设备分类，ID: {}, DTO: {}", id, dto);

        EquipmentClassfication existing = equipmentClassficationDao.findById(id);
        if (existing == null) {
            throw new IdmeException("设备分类不存在: " + id);
        }

        existing.setEquipmentClassName(dto.getEquipmentClassName());
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);

        EquipmentClassfication updated = equipmentClassficationDao.update(existing);

        return convertToVO(updated);
    }

    /**
     * 删除设备分类
     */
    public void delete(String id) {
        log.info("删除设备分类，ID: {}", id);
        equipmentClassficationDao.delete(id);
    }

    /**
     * 按 ID 查询设备分类详情
     */
    public EquipmentClassficationVO getById(String id) {
        log.info("查询设备分类详情，ID: {}", id);
        EquipmentClassfication entity = equipmentClassficationDao.findById(id);
        if (entity == null) {
            throw new IdmeException("设备分类不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询设备分类列表
     */
    public List<EquipmentClassficationVO> list(int pageNum, int pageSize) {
        log.info("分页查询设备分类列表，pageNum: {}, pageSize: {}", pageNum, pageSize);

        List<EquipmentClassfication> entities = equipmentClassficationDao.findAll(pageNum, pageSize);

        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * Entity -> VO 转换
     */
    private EquipmentClassficationVO convertToVO(EquipmentClassfication entity) {
        EquipmentClassficationVO vo = new EquipmentClassficationVO();
        vo.setId(entity.getId());
        vo.setEquipmentClassName(entity.getEquipmentClassName());
        vo.setCreateTime(entity.getCreateTime());
        vo.setLastUpdateTime(entity.getLastUpdateTime());
        return vo;
    }

    /**
     * DTO -> Entity 转换
     */
    private EquipmentClassfication convertToEntity(EquipmentClassficationCreateDTO dto) {
        EquipmentClassfication entity = new EquipmentClassfication();
        entity.setEquipmentClassName(dto.getEquipmentClassName());
        return entity;
    }
}
