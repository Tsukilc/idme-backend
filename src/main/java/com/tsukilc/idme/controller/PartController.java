package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.PartCreateDTO;
import com.tsukilc.idme.service.PartService;
import com.tsukilc.idme.vo.PartVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 物料管理 Controller
 * 严格遵循 openapi.yaml 的 Part 相关接口定义
 */
@RestController
@RequestMapping("/api/part")
@Slf4j
public class PartController {
    
    @Autowired
    private PartService partService;
    
    /**
     * 创建物料
     * POST /api/part
     */
    @PostMapping
    public ApiResponse<PartVO> create(@RequestBody PartCreateDTO dto) {
        log.info("创建物料: {}", dto);
        PartVO vo = partService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询物料详情
     * GET /api/part/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<PartVO> getById(@PathVariable String id) {
        log.info("查询物料详情，ID: {}", id);
        PartVO vo = partService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新物料
     * PUT /api/part/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<PartVO> update(@PathVariable String id, @RequestBody PartCreateDTO dto) {
        log.info("更新物料，ID: {}, 数据: {}", id, dto);
        PartVO vo = partService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除物料
     * DELETE /api/part/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除物料，ID: {}", id);
        partService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 分页查询物料列表
     * GET /api/part
     */
    @GetMapping
    public ApiResponse<PageResult<PartVO>> list(
        @RequestParam(defaultValue = "1") int pageNum,
        @RequestParam(defaultValue = "10") int pageSize) {
        
        log.info("分页查询物料列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<PartVO> list = partService.list(pageNum, pageSize);
        
        PageResult<PartVO> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(list.size());
        
        return ApiResponse.success(pageResult);
    }
    
    /**
     * 按物料编号查询（查询该编号下的所有版本）
     * GET /api/part/by-number/{partNumber}
     */
    @GetMapping("/by-number/{partNumber}")
    public ApiResponse<List<PartVO>> findByPartNumber(@PathVariable String partNumber) {
        log.info("按物料编号查询，partNumber: {}", partNumber);
        List<PartVO> list = partService.findByPartNumber(partNumber);
        return ApiResponse.success(list);
    }

    /**
     * 检出物料（创建工作副本）
     * POST /api/part/{masterId}/checkout
     */
    @PostMapping("/{masterId}/checkout")
    public ApiResponse<PartVO> checkout(@PathVariable String masterId) {
        log.info("检出物料，masterId: {}", masterId);
        PartVO vo = partService.checkout(masterId);
        return ApiResponse.success(vo);
    }

    /**
     * 检入物料（保存为新版本）
     * POST /api/part/{masterId}/checkin
     */
    @PostMapping("/{masterId}/checkin")
    public ApiResponse<PartVO> checkin(@PathVariable String masterId) {
        log.info("检入物料，masterId: {}", masterId);
        PartVO vo = partService.checkin(masterId);
        return ApiResponse.success(vo);
    }

    /**
     * 查询版本历史
     * GET /api/part/{masterId}/history
     */
    @GetMapping("/{masterId}/history")
    public ApiResponse<List<PartVO>> getVersionHistory(@PathVariable String masterId) {
        log.info("查询物料版本历史，masterId: {}", masterId);
        List<PartVO> list = partService.getVersionHistory(masterId);
        return ApiResponse.success(list);
    }

    /**
     * 物料库存统计
     * GET /api/part/statistics/stock
     */
    @GetMapping("/statistics/stock")
    public ApiResponse<Map<String, Integer>> getStockStatistics() {
        log.info("查询物料库存统计");
        Map<String, Integer> stats = partService.getStockStatistics();
        return ApiResponse.success(stats);
    }
}
