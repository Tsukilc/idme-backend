package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.BusinessPartnerCreateDTO;
import com.tsukilc.idme.service.BusinessPartnerService;
import com.tsukilc.idme.vo.BusinessPartnerVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 往来单位管理 Controller
 * 严格参照 docs/openapi.yaml 定义接口路径和参数
 */
@RestController
@RequestMapping("/api/business-partner")
@Slf4j
public class BusinessPartnerController {
    
    @Autowired
    private BusinessPartnerService businessPartnerService;
    
    /**
     * 创建往来单位
     * 对应：POST /api/business-partner
     */
    @PostMapping
    public ApiResponse<BusinessPartnerVO> create(@RequestBody @Valid BusinessPartnerCreateDTO dto) {
        log.info("创建往来单位: {}", dto);
        BusinessPartnerVO vo = businessPartnerService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询往来单位详情
     * 对应：GET /api/business-partner/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<BusinessPartnerVO> getById(@PathVariable String id) {
        log.info("查询往来单位详情，ID: {}", id);
        BusinessPartnerVO vo = businessPartnerService.getById(id);
        return ApiResponse.success(vo);
    }
    
    /**
     * 更新往来单位
     * 对应：PUT /api/business-partner/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<BusinessPartnerVO> update(
            @PathVariable String id,
            @RequestBody @Valid BusinessPartnerCreateDTO dto
    ) {
        log.info("更新往来单位，ID: {}, 数据: {}", id, dto);
        BusinessPartnerVO vo = businessPartnerService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除往来单位
     * 对应：DELETE /api/business-partner/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除往来单位，ID: {}", id);
        businessPartnerService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 分页查询往来单位列表
     * 对应：GET /api/business-partner
     */
    @GetMapping
    public ApiResponse<PageResult<BusinessPartnerVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        log.info("查询往来单位列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<BusinessPartnerVO> list = businessPartnerService.list(pageNum, pageSize);

        PageResult<BusinessPartnerVO> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(list.size());

        return ApiResponse.success(pageResult);
    }

    /**
     * 按类型查询往来单位
     * 对应：GET /api/business-partner/by-type/{partnerType}
     */
    @GetMapping("/by-type/{partnerType}")
    public ApiResponse<List<BusinessPartnerVO>> getByType(@PathVariable String partnerType) {
        log.info("按类型查询往来单位，类型: {}", partnerType);
        List<BusinessPartnerVO> list = businessPartnerService.getByType(partnerType);
        return ApiResponse.success(list);
    }
}
