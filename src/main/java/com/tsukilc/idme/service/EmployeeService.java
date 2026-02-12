package com.tsukilc.idme.service;

import com.tsukilc.idme.dao.EmployeeDao;
import com.tsukilc.idme.dto.EmployeeCreateDTO;
import com.tsukilc.idme.entity.Employee;
import com.tsukilc.idme.entity.ObjectReference;
import com.tsukilc.idme.exception.IdmeException;
import com.tsukilc.idme.vo.EmployeeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工管理服务
 * 负责业务逻辑和 Entity/VO/DTO 之间的转换
 */
@Service
@Slf4j
public class EmployeeService {
    
    @Autowired
    private EmployeeDao employeeDao;
    
    /**
     * 创建员工
     */
    public EmployeeVO create(EmployeeCreateDTO dto) {
        log.info("创建员工: {}", dto);
        
        // DTO 转 Entity
        Employee entity = convertToEntity(dto);
        
        // #region agent log
        try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_6\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"EmployeeService.java:35\",\"message\":\"转换后的Entity\",\"data\":{\"employeeNo\":\""+entity.getEmployeeNo()+"\",\"employeeName\":\""+entity.getEmployeeName()+"\",\"status\":\""+entity.getStatus()+"\",\"deptIsNull\":"+(entity.getDept()==null)+"},\"hypothesisId\":\"C\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
        // #endregion
        
        // 调用 DAO 创建
        Employee created = employeeDao.create(entity);
        
        // #region agent log
        try{java.nio.file.Files.write(java.nio.file.Paths.get("/Users/zbj/IdeaProjects/idme/.cursor/debug.log"),("{\"id\":\"log_"+System.currentTimeMillis()+"_7\",\"timestamp\":"+System.currentTimeMillis()+",\"location\":\"EmployeeService.java:45\",\"message\":\"DAO创建后的结果\",\"data\":{\"createdIsNull\":"+(created==null)+",\"idIsNull\":"+(created!=null&&created.getId()==null)+",\"employeeNoIsNull\":"+(created!=null&&created.getEmployeeNo()==null)+"},\"hypothesisId\":\"A,B\"}"+System.lineSeparator()).getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);}catch(Exception e){}
        // #endregion
        
        // Entity 转 VO 返回
        return convertToVO(created);
    }
    
    /**
     * 更新员工
     */
    public EmployeeVO update(String id, EmployeeCreateDTO dto) {
        log.info("更新员工，ID: {}, 数据: {}", id, dto);
        
        // 查询现有实体
        Employee existing = employeeDao.findById(id);
        if (existing == null) {
            throw new IdmeException("员工不存在: " + id);
        }
        
        // 更新字段
        updateEntityFromDTO(existing, dto);
        
        // 调用 DAO 更新
        Employee updated = employeeDao.update(existing);
        
        // 返回 VO
        return convertToVO(updated);
    }
    
    /**
     * 删除员工
     */
    public void delete(String id) {
        log.info("删除员工，ID: {}", id);
        employeeDao.delete(id);
    }
    
    /**
     * 根据ID查询员工
     */
    public EmployeeVO getById(String id) {
        log.info("查询员工详情，ID: {}", id);
        Employee entity = employeeDao.findById(id);
        if (entity == null) {
            throw new IdmeException("员工不存在: " + id);
        }
        return convertToVO(entity);
    }
    
    /**
     * 分页查询员工列表
     */
    public List<EmployeeVO> list(int pageNum, int pageSize) {
        log.info("分页查询员工列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<Employee> entities = employeeDao.findAll(pageNum, pageSize);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    /**
     * 按部门查询员工
     */
    public List<EmployeeVO> findByDept(String deptId) {
        log.info("按部门查询员工，deptId: {}", deptId);
        List<Employee> entities = employeeDao.findByDept(deptId, 1, 1000);
        return entities.stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
    }
    
    // ==================== 转换方法 ====================
    
    /**
     * Entity -> VO 转换
     * 只返回前端需要的字段，参照 openapi.yaml 的 Employee schema
     */
    private EmployeeVO convertToVO(Employee entity) {
        EmployeeVO vo = new EmployeeVO();
        vo.setId(entity.getId());
        vo.setEmployeeNo(entity.getEmployeeNo());
        vo.setEmployeeName(entity.getEmployeeName());
        vo.setUserRef(entity.getUserRef());
        vo.setJobTitle(entity.getJobTitle());
        
        // 扁平化部门：只取 ID
        if (entity.getDept() != null) {
            vo.setDept(entity.getDept().getId());
        }
        
        vo.setPhone(entity.getPhone());
        vo.setEmail(entity.getEmail());
        
        // SDK 使用 Active/Separated/External，转换为中文
        vo.setStatus(convertStatus(entity.getStatus()));
        
        // hireDate 从 LocalDateTime 转为 LocalDate
        if (entity.getHireDate() != null) {
            vo.setHireDate(entity.getHireDate().toLocalDate());
        }
        
        vo.setExtra(entity.getExtra());
        
        return vo;
    }
    
    /**
     * DTO -> Entity 转换（用于创建）
     */
    private Employee convertToEntity(EmployeeCreateDTO dto) {
        Employee entity = new Employee();
        entity.setEmployeeNo(dto.getEmployeeNo());
        entity.setEmployeeName(dto.getEmployeeName());
        entity.setUserRef(dto.getUserRef());
        entity.setJobTitle(dto.getJobTitle());
        
        // 部门：前端传 ID，转为 ObjectReference
        if (dto.getDept() != null) {
            ObjectReference deptRef = new ObjectReference(dto.getDept());
            entity.setDept(deptRef);
        }
        
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        
        // 状态：前端传中文，转为 SDK 枚举
        entity.setStatus(convertStatusToSdk(dto.getStatus()));
        
        // hireDate 从 LocalDate 转为 LocalDateTime
        if (dto.getHireDate() != null) {
            entity.setHireDate(dto.getHireDate().atStartOfDay());
        }
        
        entity.setExtra(dto.getExtra());
        
        return entity;
    }
    
    /**
     * 从 DTO 更新 Entity 字段
     */
    private void updateEntityFromDTO(Employee entity, EmployeeCreateDTO dto) {
        if (dto.getEmployeeNo() != null) {
            entity.setEmployeeNo(dto.getEmployeeNo());
        }
        if (dto.getEmployeeName() != null) {
            entity.setEmployeeName(dto.getEmployeeName());
        }
        if (dto.getUserRef() != null) {
            entity.setUserRef(dto.getUserRef());
        }
        if (dto.getJobTitle() != null) {
            entity.setJobTitle(dto.getJobTitle());
        }
        if (dto.getDept() != null) {
            ObjectReference deptRef = new ObjectReference(dto.getDept());
            entity.setDept(deptRef);
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(convertStatusToSdk(dto.getStatus()));
        }
        if (dto.getHireDate() != null) {
            entity.setHireDate(dto.getHireDate().atStartOfDay());
        }
        if (dto.getExtra() != null) {
            entity.setExtra(dto.getExtra());
        }
    }
    
    /**
     * SDK 状态转中文
     * Active -> 在职, Separated -> 离职, External -> 外协
     */
    private String convertStatus(String sdkStatus) {
        if (sdkStatus == null) {
            return null;
        }
        switch (sdkStatus) {
            case "Active": return "在职";
            case "Separated": return "离职";
            case "External": return "外协";
            default: return sdkStatus;
        }
    }
    
    /**
     * 中文状态转 SDK 枚举
     * 在职 -> Active, 离职 -> Separated, 外协 -> External
     */
    private String convertStatusToSdk(String status) {
        if (status == null) {
            return "Active";  // 默认在职
        }
        switch (status) {
            case "在职": return "Active";
            case "离职": return "Separated";
            case "外协": return "External";
            default: return status;
        }
    }
}
