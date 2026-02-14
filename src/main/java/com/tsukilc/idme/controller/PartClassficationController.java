package com.tsukilc.idme.controller;

import com.tsukilc.idme.common.ApiResponse;
import com.tsukilc.idme.common.PageResult;
import com.tsukilc.idme.dto.PartClassficationCreateDTO;
import com.tsukilc.idme.service.PartClassficationService;
import com.tsukilc.idme.vo.PartClassficationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 物料分类管理 Controller
 */
@RestController
@RequestMapping("/api/part-classfication")
@Slf4j
public class PartClassficationController {

    @Autowired
    private PartClassficationService partClassficationService;

    /**
     * 创建物料分类
     */
    @PostMapping
    public ApiResponse<PartClassficationVO> create(@Valid @RequestBody PartClassficationCreateDTO dto) {
        log.info("创建物料分类请求: {}", dto);
        PartClassficationVO vo = partClassficationService.create(dto);
        return ApiResponse.success(vo);
    }

    /**
     * 更新物料分类
     */
    @PutMapping("/{id}")
    public ApiResponse<PartClassficationVO> update(@PathVariable String id,
                                                     @Valid @RequestBody PartClassficationCreateDTO dto) {
        log.info("更新物料分类请求，ID: {}, DTO: {}", id, dto);
        PartClassficationVO vo = partClassficationService.update(id, dto);
        return ApiResponse.success(vo);
    }

    /**
     * 删除物料分类
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        log.info("删除物料分类请求，ID: {}", id);
        partClassficationService.delete(id);
        return ApiResponse.success(null);
    }

    /**
     * 查询物料分类详情
     */
    @GetMapping("/{id}")
    public ApiResponse<PartClassficationVO> getById(@PathVariable String id) {
        log.info("查询物料分类详情，ID: {}", id);
        PartClassficationVO vo = partClassficationService.getById(id);
        return ApiResponse.success(vo);
    }

    /**
     * 分页查询物料分类列表
     */
    @GetMapping
    public ApiResponse<PageResult<PartClassficationVO>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "20") int pageSize) {
        log.info("分页查询物料分类列表，pageNum: {}, pageSize: {}", pageNum, pageSize);
        List<PartClassficationVO> list = partClassficationService.list(pageNum, pageSize);

        PageResult<PartClassficationVO> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(list.size());

        return ApiResponse.success(pageResult);
    }
}
