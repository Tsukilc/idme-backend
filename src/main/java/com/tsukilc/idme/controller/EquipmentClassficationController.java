package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.EquipmentClassficationCreateDTO;
import com.tsukilc.idme.service.EquipmentClassficationService;
import com.tsukilc.idme.vo.EquipmentClassficationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备分类管理 Controller
 */
@RestController
@RequestMapping("/api/equipment-classfication")
@Slf4j
public class EquipmentClassficationController {

    @Autowired
    private EquipmentClassficationService equipmentClassficationService;

    @PostMapping
    public ApiResponse<EquipmentClassficationVO> create(@RequestBody EquipmentClassficationCreateDTO dto) {
        log.info("创建设备分类: {}", dto);
        EquipmentClassficationVO vo = equipmentClassficationService.create(dto);
        return ApiResponse.success(vo);
    }

    @GetMapping("/{id}")
    public ApiResponse<EquipmentClassficationVO> getById(@PathVariable String id) {
        log.info("查询设备分类详情，ID: {}", id);
        EquipmentClassficationVO vo = equipmentClassficationService.getById(id);
        return ApiResponse.success(vo);
    }

    @GetMapping
    public ApiResponse<PageResult<EquipmentClassficationVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        log.info("分页查询设备分类列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<EquipmentClassficationVO> list = equipmentClassficationService.list(pageNum, pageSize);

        PageResult<EquipmentClassficationVO> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(list.size());

        return ApiResponse.success(pageResult);
    }

    @PutMapping("/{id}")
    public ApiResponse<EquipmentClassficationVO> update(
            @PathVariable String id,
            @RequestBody EquipmentClassficationCreateDTO dto
    ) {
        log.info("更新设备分类，ID: {}, 数据: {}", id, dto);
        EquipmentClassficationVO vo = equipmentClassficationService.update(id, dto);
        return ApiResponse.success(vo);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除设备分类，ID: {}", id);
        equipmentClassficationService.delete(id);
        return ApiResponse.success(null);
    }
}
