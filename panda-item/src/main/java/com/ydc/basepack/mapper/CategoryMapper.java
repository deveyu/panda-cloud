package com.ydc.basepack.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.CategoryQuery;
import com.yukong.panda.common.base.mapper.BaseMapper;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {

    List<Category> queryAll();

    IPage<Category> getCategoryByPage(CategoryQuery query);
}
