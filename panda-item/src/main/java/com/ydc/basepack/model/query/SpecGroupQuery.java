package com.ydc.basepack.model.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydc.basepack.model.entity.SpecGroup;
import lombok.Data;

@Data
public class SpecGroupQuery extends Page<SpecGroup> {
    private Long id;

    private Long cid;

    private String name;
}