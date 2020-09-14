package com.ydc.basepack.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydc.basepack.mapper.BrandMapper;
import com.ydc.basepack.mapper.CategoryMapper;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Brand;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.CategoryQuery;
import com.ydc.basepack.service.BrandService;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.util.TreeUtil;
import com.yukong.panda.common.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<CategoryTree> getAllCategory() {
        QueryWrapper<Category> query = new QueryWrapper<>();
        List<Category> categories = categoryMapper.queryAll();
        List<CategoryTree> categoryTrees = TreeUtil.list2Tree(categories, CommonConstants.CATEGORY_TREE_ROOT);
        return categoryTrees;
    }
}
