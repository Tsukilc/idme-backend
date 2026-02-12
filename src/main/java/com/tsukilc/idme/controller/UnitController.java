package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.UnitCreateDTO;
import com.tsukilc.idme.service.UnitService;
import com.tsukilc.idme.vo.UnitVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 计量单位管理 Controller
 */
@RestController
@RequestMapping("/api/unit")
@Slf4j
public class UnitController {
    
    @Autowired
    private UnitService unitService;
    
    /**
     * 创建计量单位
     */
    @PostMapping
    public ApiResponse<UnitVO> create(@RequestBody @Valid UnitCreateDTO dto) {
        log.info("创建计量单位: {}", dto);
        UnitVO vo = unitService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询计量单位详情
     */
    @GetMapping("/{id}")
    public ApiResponse<UnitVO> getById(@PathVariable String id) {
        log.info("查询计量单位详情，ID: {}", id);
        UnitVO vo = unitService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新计量单位
     */
    @PutMapping("/{id}")
    public ApiResponse<UnitVO> update(
            @PathVariable String id,
            @RequestBody @Valid UnitCreateDTO dto
    ) {
        log.info("更新计量单位，ID: {}, 数据: {}", id, dto);
        UnitVO vo = unitService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除计量单位
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除计量单位，ID: {}", id);
        unitService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 分页查询计量单位列表
     */
    @GetMapping
    public ApiResponse<PageResult<UnitVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        log.info("查询计量单位列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<UnitVO> list = unitService.list(pageNum, pageSize);
        
        PageResult<UnitVO> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(list.size());
        
        return ApiResponse.success(pageResult);
    }
}
