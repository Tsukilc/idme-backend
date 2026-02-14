package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.ProcedurePartLinkDao;
import com.tsukilc.idme.dto.ProcedurePartLinkCreateDTO;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.entity.ProcedurePartLink;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.ProcedurePartLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProcedurePartLinkService {
    private final ProcedurePartLinkDao dao;

    public ProcedurePartLinkService(ProcedurePartLinkDao dao) {
        this.dao = dao;
    }

    /**
     * 创建工序-物料关联
     */
    public ProcedurePartLinkVO create(ProcedurePartLinkCreateDTO dto) {
        log.info("创建工序-物料关联: {}", dto);

        ProcedurePartLink entity = convertToEntity(dto);
        ProcedurePartLink created = dao.create(entity);
        return convertToVO(created);
    }

    /**
     * 更新工序-物料关联
     */
    public ProcedurePartLinkVO update(String id, ProcedurePartLinkCreateDTO dto) {
        log.info("更新工序-物料关联，ID: {}, DTO: {}", id, dto);

        ProcedurePartLink existing = dao.findById(id);
        if (existing == null) {
            throw new IdmeException("工序-物料关联不存在: " + id);
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

        ProcedurePartLink updated = dao.update(existing);
        return convertToVO(updated);
    }

    /**
     * 查询详情
     */
    public ProcedurePartLinkVO getById(String id) {
        log.info("查询工序物料关联详情，ID: {}", id);
        ProcedurePartLink entity = dao.findById(id);
        if (entity == null) {
            throw new IdmeException("工序物料关联不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询列表
     */
    public List<ProcedurePartLinkVO> list(int pageNum, int pageSize) {
        log.info("分页查询工序-物料关联列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<ProcedurePartLink> entities = dao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 按工序查询
     */
    public List<ProcedurePartLinkVO> getByProcedure(String procedureId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("procedure", procedureId);
        List<ProcedurePartLink> entities = dao.findByCondition(condition, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 按物料查询
     */
    public List<ProcedurePartLinkVO> getByPart(String partId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("part1", partId);
        List<ProcedurePartLink> entities = dao.findByCondition(condition, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(String id) {
        log.info("删除工序-物料关联，ID: {}", id);
        dao.delete(id);
    }

    /**
     * DTO -> Entity
     */
    private ProcedurePartLink convertToEntity(ProcedurePartLinkCreateDTO dto) {
        ProcedurePartLink entity = new ProcedurePartLink();

        // 双重字段命名：同时设置source/target和procedure/part1
        ObjectReference procedure = new ObjectReference(dto.getProcedure(), "WorkingProcedure");
        ObjectReference part = new ObjectReference(dto.getPart1(), "Part");

        entity.setSource(procedure);      // SDK标准字段
        entity.setTarget(part);           // SDK标准字段
        entity.setProcedure(procedure);   // 业务别名（与source同值）
        entity.setPart1(part);            // 业务别名（与target同值）

        entity.setRole(dto.getRole());
        entity.setQuantity(dto.getQuantity());

        if (dto.getUom() != null) {
            entity.setUom(new ObjectReference(dto.getUom(), "Unit"));
        }

        entity.setIsMandatory(dto.getIsMandatory());
        entity.setRemarks(dto.getRemarks());

        return entity;
    }

    /**
     * 从 DTO 更新 Entity
     */
    private void updateEntityFromDTO(ProcedurePartLink entity, ProcedurePartLinkCreateDTO dto) {
        if (dto.getProcedure() != null) {
            ObjectReference procedure = new ObjectReference(dto.getProcedure(), "WorkingProcedure");
            entity.setSource(procedure);
            entity.setProcedure(procedure);
        }
        if (dto.getPart1() != null) {
            ObjectReference part = new ObjectReference(dto.getPart1(), "Part");
            entity.setTarget(part);
            entity.setPart1(part);
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
        if (dto.getQuantity() != null) {
            entity.setQuantity(dto.getQuantity());
        }
        if (dto.getUom() != null) {
            entity.setUom(new ObjectReference(dto.getUom(), "Unit"));
        }
        if (dto.getIsMandatory() != null) {
            entity.setIsMandatory(dto.getIsMandatory());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
    }

    /**
     * Entity -> VO
     */
    private ProcedurePartLinkVO convertToVO(ProcedurePartLink entity) {
        ProcedurePartLinkVO vo = new ProcedurePartLinkVO();
        vo.setId(entity.getId());

        // 优先从source/target提取ID（SDK标准字段）
        if (entity.getSource() != null) {
            vo.setProcedure(entity.getSource().getId());
        }
        if (entity.getTarget() != null) {
            vo.setPart(entity.getTarget().getId());
        }

        vo.setRole(convertEnumField(entity.getRole()));

        if (entity.getQuantity() != null) {
            vo.setQuantity(entity.getQuantity().intValue());
        }

        if (entity.getUom() != null) {
            vo.setUom(entity.getUom().getId());
        }

        vo.setIsMandatory(entity.getIsMandatory());

        return vo;
    }

    /**
     * 转换枚举字段
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
