package com.ydc.basepack.controller;


import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.entity.Category;
import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.model.query.CategoryQuery;
import com.ydc.basepack.service.BrandService;
import com.ydc.basepack.service.CategoryService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@Api(value = "分类controller", tags = {"分类操作接口"})
public class CategoryController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "分类信息树结构查询", notes = "分类信息树结构查询所有", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryTreeQuery", value = "分类信息查询类", required = false, dataType = "categoryQuery")
    @GetMapping("tree")
    public ApiResult<List<CategoryTree>> getAllCategory(){
        return new ApiResult<>(categoryService.getAllCategory());
    }

    @ApiOperation(value = "分类信息分页查询", notes = "分类信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryQuery", value = "分类信息查询类", required = false, dataType = "categoryQuery")
    @GetMapping("page")
    public ApiResult<CategoryQuery> getCategoryByPage(CategoryQuery query){
        return new ApiResult<>(categoryService.getCategoryByPage(query));
    }

    @ApiOperation(value = "添加分类", notes = "添加分类", httpMethod = "POST")
    @ApiImplicitParam(name = "categoryAdd", value = "添加分类", required = false, dataType = "categoryAdd")
    @PostMapping
    public ApiResult<Boolean> addCategory(@RequestBody Category category){
        return new ApiResult<Boolean>(categoryService.save(category));
    }


}
