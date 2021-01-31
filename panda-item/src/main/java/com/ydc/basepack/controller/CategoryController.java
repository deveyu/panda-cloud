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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("category")
@Api(value = "分类controller", tags = {"分类操作接口"})
public class CategoryController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private CategoryService categoryService;


    /**
     * 该接口portal和admin都需要显示树形结构的分类，怎么存？这一块要动态获取最新数据吗？现在按照最新显示
     * 方案：按树结构存redis，数据结构string
     * @return
     */
    @ApiOperation(value = "分类信息树结构查询", notes = "分类信息树结构查询所有", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryTreeQuery", value = "分类信息查询类", required = false, dataType = "categoryQuery")
    @GetMapping("tree")
    public ApiResult<List<CategoryTree>> getCategoryByTree(){
        return new ApiResult<>(categoryService.getCategoryByTree());
    }

    @ApiOperation(value = "分类信息分页查询", notes = "分类信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryQuery", value = "分类信息查询类", required = false, dataType = "categoryQuery")
    @GetMapping("page")
    public ApiResult<CategoryQuery> getCategoryByPage(CategoryQuery query){
        return new ApiResult<>(categoryService.getCategoryByPage(query));
    }

    /**
     * 先更新db
     * mq消费者异步更新redis
     *
     * @param category
     * @return
     */
    @ApiOperation(value = "添加分类", notes = "添加分类", httpMethod = "POST")
    @ApiImplicitParam(name = "categoryAdd", value = "添加分类", required = false, dataType = "categoryAdd")
    @PostMapping
    public ApiResult<Boolean> addCategory(@RequestBody Category category){

        return new ApiResult<>(categoryService.addCategory(category));
    }

    @ApiOperation(value = "修改分类", notes = "修改分类", httpMethod = "PUT")
    @ApiImplicitParam(name = "categoryUpdate", value = "修改分类", required = false, dataType = "categoryUpdate")
    @PutMapping
    public ApiResult<Boolean> updateCategory(@RequestBody Category category){
        category.setUpdateTime(new Date());
        return new ApiResult<Boolean>(categoryService.updateCategory(category));
    }

    @ApiOperation(value = "删除分类", notes = "删除分类", httpMethod = "DELETE")
    @ApiImplicitParam(name = "categoryDelete", value = "删除分类", required = false, dataType = "categoryDelete")
    @DeleteMapping("id/{id}")
    public ApiResult<Boolean> removeCategory(@PathVariable("id")Long id){
        return new ApiResult<Boolean>(categoryService.removeCategory(id));
    }

    /**
     * @param ids
     * @return
     */
    @ApiOperation(value = "根据Id查询分类", notes = "添加根据Id查询分类分类", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryQuery", value = "查询分类", required = false, dataType = "")
    @GetMapping("list")
    public ApiResult<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return new ApiResult<>(categoryService.queryCategoryByIds(ids));
    }


}
