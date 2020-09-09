package com.ydc.basepack.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.query.BrandQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 分页查询
* @param query
     * @return
     */
    IPage<Brand> pageByQuery(BrandQuery query);
}
