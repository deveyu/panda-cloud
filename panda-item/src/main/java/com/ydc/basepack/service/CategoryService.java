package com.ydc.basepack.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.CategoryQuery;

import java.util.List;

public interface CategoryService extends IService<Category> {

  List<CategoryTree> getAllCategory();
}
