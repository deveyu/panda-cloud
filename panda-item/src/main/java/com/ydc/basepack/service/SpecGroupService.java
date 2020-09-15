package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.SpecGroup;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.SpecGroupQuery;

/**
 * @author pc
 */
public interface SpecGroupService extends IService<SpecGroup> {


    SpecGroupQuery pageSpecGroupByCategory(SpecGroupQuery query);
}