package com.tsukilc.idme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 更新顺序号 DTO
 */
@Data
public class SequenceUpdateDTO {
    @NotNull(message = "顺序号不能为空")
    private Integer sequenceNo;
}
