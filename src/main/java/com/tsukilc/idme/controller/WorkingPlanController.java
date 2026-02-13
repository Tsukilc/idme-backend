package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.WorkingPlanCreateDTO;
import com.tsukilc.idme.service.WorkingPlanService;
import com.tsukilc.idme.vo.WorkingPlanVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工艺路线 Controller
 * 竞赛核心模块（10分）- VersionObject类型
 */
@RestController
@RequestMapping("/api/working-plan")
public class WorkingPlanController {

    private final WorkingPlanService workingPlanService;

    public WorkingPlanController(WorkingPlanService workingPlanService) {
        this.workingPlanService = workingPlanService;
    }

    /**
     * 分页查询工艺路线列表
     */
    @GetMapping
    public ApiResponse<PageResult<WorkingPlanVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<WorkingPlanVO> result = workingPlanService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建工艺路线
     */
    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody WorkingPlanCreateDTO dto) {
        String id = workingPlanService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 查询工艺路线详情
     */
    @GetMapping("/{id}")
    public ApiResponse<WorkingPlanVO> getById(@PathVariable String id) {
        WorkingPlanVO vo = workingPlanService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新工艺路线
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody WorkingPlanCreateDTO dto) {
        workingPlanService.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 删除工艺路线
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        workingPlanService.delete(id);
        return ApiResponse.success(null);
    }
}
