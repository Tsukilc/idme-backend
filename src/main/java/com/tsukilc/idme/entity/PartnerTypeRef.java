package com.tsukilc.idme.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 往来单位类型引用
 * SDK在list/get时返回对象{code, cnName, enName, alias}
 * 在create/update时需要传入String（enName值）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = PartnerTypeRefSerializer.class)
@JsonDeserialize(using = PartnerTypeRefDeserializer.class)
public class PartnerTypeRef {
    private String code;
    private String cnName;
    private String enName;
    private String alias;
    
    public PartnerTypeRef(String enName) {
        this.enName = enName;
    }
}
