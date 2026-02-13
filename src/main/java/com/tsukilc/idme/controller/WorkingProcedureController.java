package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.WorkingProcedureBatchCreateDTO;
import com.tsukilc.idme.dto.WorkingProcedureCreateDTO;
import com.tsukilc.idme.dto.WorkingProcedureStatusUpdateDTO;
import com.tsukilc.idme.service.WorkingProcedureService;
import com.tsukilc.idme.vo.WorkingProcedureVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工序 Controller
 * 竞赛核心模块（5分）- 支持批量创建和状态更新
 */
@RestController
@RequestMapping("/api/working-procedure")
public class WorkingProcedureController {

    private final WorkingProcedureService workingProcedureService;

    public WorkingProcedureController(WorkingProcedureService workingProcedureService) {
        this.workingProcedureService = workingProcedureService;
    }

    /**
     * 分页查询工序列表
     */
    @GetMapping
    public ApiResponse<PageResult<WorkingProcedureVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<WorkingProcedureVO> result = workingProcedureService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建工序
     */
    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody WorkingProcedureCreateDTO dto) {
        String id = workingProcedureService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 批量创建工序（竞赛要求）
     */
    @PostMapping("/batch")
    public ApiResponse<List<String>> batchCreate(@Validated @RequestBody WorkingProcedureBatchCreateDTO dto) {
        List<String> ids = workingProcedureService.batchCreate(dto);
        return ApiResponse.success(ids);
    }

    /**
     * 查询工序详情
     */
    @GetMapping("/{id}")
    public ApiResponse<WorkingProcedureVO> getById(@PathVariable String id) {
        WorkingProcedureVO vo = workingProcedureService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新工序
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody WorkingProcedureCreateDTO dto) {
        workingProcedureService.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 更新工序状态（竞赛要求）
     */
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable String id, 
                                          @Validated @RequestBody WorkingProcedureStatusUpdateDTO dto) {
        workingProcedureService.updateStatus(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 删除工序
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        workingProcedureService.delete(id);
        return ApiResponse.success(null);
    }
}
