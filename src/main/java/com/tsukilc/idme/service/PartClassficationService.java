package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.PartClassficationDao;
import com.tsukilc.idme.dto.PartClassficationCreateDTO;
import com.tsukilc.idme.entity.PartClassfication;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.PartClassficationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料分类服务
 */
@Service
@Slf4j
public class PartClassficationService {

    @Autowired
    private PartClassficationDao partClassficationDao;

    /**
     * 创建物料分类
     */
    public PartClassficationVO create(PartClassficationCreateDTO dto) {
        log.info("创建物料分类: {}", dto);

        PartClassfication entity = new PartClassfication();
        entity.setPartClassName(dto.getPartClassName());

        PartClassfication created = partClassficationDao.create(entity);
        return convertToVO(created);
    }

    /**
     * 更新物料分类
     */
    public PartClassficationVO update(String id, PartClassficationCreateDTO dto) {
        log.info("更新物料分类，ID: {}, DTO: {}", id, dto);

        PartClassfication existing = partClassficationDao.findById(id);
        if (existing == null) {
            throw new IdmeException("物料分类不存在: " + id);
        }

        existing.setPartClassName(dto.getPartClassName());
        existing.setId(id);

        // 清空系统字段
        existing.setCreator(null);
        existing.setModifier(null);
        existing.setCreateTime(null);
        existing.setLastUpdateTime(null);
        existing.setRdmDeleteFlag(null);
        existing.setRdmExtensionType(null);
        existing.setClassName(null);

        PartClassfication updated = partClassficationDao.update(existing);
        return convertToVO(updated);
    }

    /**
     * 删除物料分类
     */
    public void delete(String id) {
        log.info("删除物料分类，ID: {}", id);
        partClassficationDao.delete(id);
    }

    /**
     * 查询物料分类详情
     */
    public PartClassficationVO getById(String id) {
        log.info("查询物料分类详情，ID: {}", id);
        PartClassfication entity = partClassficationDao.findById(id);
        if (entity == null) {
            throw new IdmeException("物料分类不存在: " + id);
        }
        return convertToVO(entity);
    }

    /**
     * 分页查询物料分类列表
     */
    public List<PartClassficationVO> list(int pageNum, int pageSize) {
        log.info("分页查询物料分类列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<PartClassfication> entities = partClassficationDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }

    /**
     * Entity -> VO 转换
     */
    private PartClassficationVO convertToVO(PartClassfication entity) {
        PartClassficationVO vo = new PartClassficationVO();
        vo.setId(entity.getId());
        vo.setPartClassName(entity.getPartClassName());
        return vo;
    }
}
