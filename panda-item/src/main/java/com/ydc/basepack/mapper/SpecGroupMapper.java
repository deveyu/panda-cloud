package com.ydc.basepack.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

public interface SpecGroupMapper extends BaseMapper<SpecGroup> {

    //IPage<SpecGroup> pageSpecGroupByCategory(SpecGroupQuery query);
}
