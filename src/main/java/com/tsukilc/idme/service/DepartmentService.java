package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.DepartmentDao;
import com.tsukilc.idme.dto.DepartmentCreateDTO;
import com.tsukilc.idme.entity.Department;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门管理 Service
 */
@Service
@Slf4j
public class DepartmentService {
    
    @Autowired
    private DepartmentDao departmentDao;
    
    /**
     * 创建部门
     */
    public DepartmentVO create(DepartmentCreateDTO dto) {
        log.info("创建部门，DTO: {}", dto);
        
        // DTO -> Entity
        Department entity = convertToEntity(dto);
        
        // 调用 DAO 创建
        Department created = departmentDao.create(entity);
        
        // Entity -> VO
        return convertToVO(created);
    }
    
    /**
     * 更新部门
     */
    public DepartmentVO update(String id, DepartmentCreateDTO dto) {
        log.info("更新部门，ID: {}, DTO: {}", id, dto);
        
        // 查询现有实体
        Department existing = departmentDao.findById(id);
        if (existing == null) {
            throw new IdmeException("部门不存在: " + id);
        }
        
        // 更新字段
        updateEntityFromDTO(existing, dto);
        existing.setId(id);  // 确保ID不变
        
        // 调用 DAO 更新
        Department updated = departmentDao.update(existing);
        
        // 返回 VO
        return convertToVO(updated);
    }
    
    /**
     * 删除部门
     */
    public void delete(String id) {
        log.info("删除部门，ID: {}", id);
        departmentDao.delete(id);
    }
    
    /**
     * 查询部门详情
     */
    public DepartmentVO getById(String id) {
        log.info("查询部门详情，ID: {}", id);
        Department entity = departmentDao.findById(id);
        if (entity == null) {
            throw new IdmeException("部门不存在: " + id);
        }
        return convertToVO(entity);
    }
    
    /**
     * 查询部门列表
     */
    public List<DepartmentVO> list(int pageNum, int pageSize) {
        log.info("查询部门列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<Department> entities = departmentDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * Entity -> VO 转换
     */
    private DepartmentVO convertToVO(Department entity) {
        DepartmentVO vo = new DepartmentVO();
        vo.setId(entity.getId());
        vo.setDeptCode(entity.getDeptCode());
        vo.setDeptName(entity.getDeptName());
        
        // 扁平化父部门：只取 ID
        if (entity.getParentDept() != null) {
            vo.setParentDept(entity.getParentDept().getId());
        }
        
        vo.setManager(entity.getManager());
        vo.setRemarks(entity.getRemarks());
        
        return vo;
    }
    
    /**
     * DTO -> Entity 转换
     */
    private Department convertToEntity(DepartmentCreateDTO dto) {
        Department entity = new Department();
        entity.setDeptCode(dto.getDeptCode());
        entity.setDeptName(dto.getDeptName());
        
        // 父部门：前端传 ID，转为 ObjectReference
        if (dto.getParentDept() != null && !dto.getParentDept().isEmpty()) {
            ObjectReference parentRef = new ObjectReference();
            parentRef.setId(dto.getParentDept());
            entity.setParentDept(parentRef);
        }
        // 如果parentDept为null或空字符串，就不设置，看SDK是否能接受
        
        entity.setManager(dto.getManager());
        entity.setRemarks(dto.getRemarks());
        
        return entity;
    }
    
    /**
     * 更新实体字段
     */
    private void updateEntityFromDTO(Department entity, DepartmentCreateDTO dto) {
        if (dto.getDeptCode() != null) {
            entity.setDeptCode(dto.getDeptCode());
        }
        if (dto.getDeptName() != null) {
            entity.setDeptName(dto.getDeptName());
        }
        if (dto.getParentDept() != null && !dto.getParentDept().isEmpty()) {
            ObjectReference parentRef = new ObjectReference();
            parentRef.setId(dto.getParentDept());
            entity.setParentDept(parentRef);
        }
        if (dto.getManager() != null) {
            entity.setManager(dto.getManager());
        }
        if (dto.getRemarks() != null) {
            entity.setRemarks(dto.getRemarks());
        }
    }
}
