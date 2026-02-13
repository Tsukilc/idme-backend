package com.tsukilc.idme.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * BOM项批量创建 DTO
 * 支持竞赛要求的批量创建功能
 */
@Data
public class BOMItemBatchCreateDTO {
    @NotEmpty(message = "BOM项列表不能为空")
    @Valid
    private List<BOMItemCreateDTO> items;
}
