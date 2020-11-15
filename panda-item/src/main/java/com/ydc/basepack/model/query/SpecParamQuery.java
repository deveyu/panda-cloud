package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.dto.SpecParamDTO;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.entity.SpecParam;
import lombok.Data;

@Data
public class SpecParamQuery extends Page<SpecParam> {
    private Long id;

    private Long cid;

    private Long gid;

    private String name;
}