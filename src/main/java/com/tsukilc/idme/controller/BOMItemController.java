package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.BOMItemBatchCreateDTO;
import com.tsukilc.idme.dto.BOMItemCreateDTO;
import com.tsukilc.idme.service.BOMItemService;
import com.tsukilc.idme.vo.BOMItemVO;
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

import java.util.List;

/**
 * BOM项 Controller
 * 竞赛核心模块（5分）- 支持树形查询和批量创建
 */
@RestController
@RequestMapping("/api/bom-item")
public class BOMItemController {

    private final BOMItemService bomItemService;

    public BOMItemController(BOMItemService bomItemService) {
        this.bomItemService = bomItemService;
    }

    /**
     * 分页查询BOM项列表
     */
    @GetMapping
    public ApiResponse<PageResult<BOMItemVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<BOMItemVO> result = bomItemService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建BOM项
     */
    @PostMapping
    public ApiResponse<String> create(@Validated @RequestBody BOMItemCreateDTO dto) {
        String id = bomItemService.create(dto);
        return ApiResponse.success(id);
    }

    /**
     * 批量创建BOM项（竞赛要求）
     */
    @PostMapping("/batch")
    public ApiResponse<List<String>> batchCreate(@Validated @RequestBody BOMItemBatchCreateDTO dto) {
        List<String> ids = bomItemService.batchCreate(dto);
        return ApiResponse.success(ids);
    }

    /**
     * 根据父项物料ID查询BOM树（树形查询，竞赛要求）
     */
    @GetMapping("/tree/{parentPartId}")
    public ApiResponse<List<BOMItemVO>> getTree(@PathVariable String parentPartId) {
        List<BOMItemVO> tree = bomItemService.getTreeByParent(parentPartId);
        return ApiResponse.success(tree);
    }

    /**
     * 查询BOM项详情
     */
    @GetMapping("/{id}")
    public ApiResponse<BOMItemVO> getById(@PathVariable String id) {
        BOMItemVO vo = bomItemService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新BOM项
     */
    @PutMapping("/{id}")
    public ApiResponse<Void> update(@PathVariable String id, @Validated @RequestBody BOMItemCreateDTO dto) {
        bomItemService.update(id, dto);
        return ApiResponse.success(null);
    }

    /**
     * 删除BOM项
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        bomItemService.delete(id);
        return ApiResponse.success(null);
    }
}
