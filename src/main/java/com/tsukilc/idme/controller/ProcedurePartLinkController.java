package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.dto.ProcedurePartLinkCreateDTO;
import com.tsukilc.idme.service.ProcedurePartLinkService;
import com.tsukilc.idme.vo.ProcedurePartLinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/procedure-part-link")
@Slf4j
public class ProcedurePartLinkController {
    private final ProcedurePartLinkService service;

    public ProcedurePartLinkController(ProcedurePartLinkService service) {
        this.service = service;
    }

    @PostMapping
    public ApiResponse<ProcedurePartLinkVO> create(@Valid @RequestBody ProcedurePartLinkCreateDTO dto) {
        log.info("创建工序-物料关联请求: {}", dto);
        return ApiResponse.success(service.create(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProcedurePartLinkVO> update(@PathVariable String id,
                                                     @Valid @RequestBody ProcedurePartLinkCreateDTO dto) {
        log.info("更新工序-物料关联请求，ID: {}, DTO: {}", id, dto);
        return ApiResponse.success(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProcedurePartLinkVO> getById(@PathVariable String id) {
        log.info("查询工序物料关联，ID: {}", id);
        return ApiResponse.success(service.getById(id));
    }

    @GetMapping
    public ApiResponse<List<ProcedurePartLinkVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("分页查询工序-物料关联列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        return ApiResponse.success(service.list(pageNum, pageSize));
    }

    @GetMapping("/by-procedure/{procedureId}")
    public ApiResponse<List<ProcedurePartLinkVO>> getByProcedure(@PathVariable String procedureId) {
        log.info("按工序查询关联，procedureId: {}", procedureId);
        return ApiResponse.success(service.getByProcedure(procedureId));
    }

    @GetMapping("/by-part/{partId}")
    public ApiResponse<List<ProcedurePartLinkVO>> getByPart(@PathVariable String partId) {
        log.info("按物料查询关联，partId: {}", partId);
        return ApiResponse.success(service.getByPart(partId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除工序-物料关联，ID: {}", id);
        service.delete(id);
        return ApiResponse.success(null);
    }
}
