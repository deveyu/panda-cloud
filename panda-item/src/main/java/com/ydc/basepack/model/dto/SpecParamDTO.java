package com.ydc.basepack.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.entity.SpecParam;
import com.ydc.basepack.model.entity.Spu;
import lombok.Data;
import org.bouncycastle.asn1.esf.SPuri;

@Data
public class SpecParamDTO extends Page<SpecParam> {
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
}