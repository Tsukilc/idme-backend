package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.PartnerContactCreateDTO;
import com.tsukilc.idme.service.PartnerContactService;
import com.tsukilc.idme.vo.PartnerContactVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 联系人管理 Controller
 * 严格参照 docs/openapi.yaml 定义接口路径和参数
 */
@RestController
@RequestMapping("/api/partner-contact")
@Slf4j
public class PartnerContactController {
    
    @Autowired
    private PartnerContactService partnerContactService;

    /**
     * 分页查询联系人列表
     * 对应：GET /api/partner-contact?pageNum=1&pageSize=20
     */
    @GetMapping
    public ApiResponse<PageResult<PartnerContactVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("查询联系人列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        PageResult<PartnerContactVO> result = partnerContactService.list(pageNum, pageSize);
        return ApiResponse.success(result);
    }

    /**
     * 创建联系人
     * 对应：POST /api/partner-contact
     */
    @PostMapping
    public ApiResponse<PartnerContactVO> create(@RequestBody @Valid PartnerContactCreateDTO dto) {
        log.info("创建联系人: {}", dto);
        PartnerContactVO vo = partnerContactService.create(dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 查询联系人详情
     * 对应：GET /api/partner-contact/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<PartnerContactVO> getById(@PathVariable String id) {
        log.info("查询联系人详情，ID: {}", id);
        PartnerContactVO vo = partnerContactService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 更新联系人
     * 对应：PUT /api/partner-contact/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<PartnerContactVO> update(
            @PathVariable String id,
            @RequestBody @Valid PartnerContactCreateDTO dto
    ) {
        log.info("更新联系人，ID: {}, 数据: {}", id, dto);
        PartnerContactVO vo = partnerContactService.update(id, dto);
        return ApiResponse.success(vo);
    }
    
    /**
     * 删除联系人
     * 对应：DELETE /api/partner-contact/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除联系人，ID: {}", id);
        partnerContactService.delete(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 查询往来单位的所有联系人
     * 对应：GET /api/partner-contact/by-partner/{partnerId}
     */
    @GetMapping("/by-partner/{partnerId}")
    public ApiResponse<List<PartnerContactVO>> getByPartner(@PathVariable String partnerId) {
        log.info("查询往来单位的联系人列表，往来单位ID: {}", partnerId);
        List<PartnerContactVO> list = partnerContactService.getByPartner(partnerId);
        return ApiResponse.success(list);
    }
}
