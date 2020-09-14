package com.ydc.basepack.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.service.GoodsService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
@Api(value = "商品controller", tags = {"商品操作接口"})
public class GoodsController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private GoodsService goodsService;


    @ApiOperation(value = "商品信息分页查询", notes = "商品信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "goodsQuery", value = "商品信息查询类", required = false, dataType = "goodsQuery")
    @GetMapping("page")
    public ApiResult<SpuDTO> queryByPage(SpuDTO spuDTO){
        return new ApiResult<>(goodsService.queryByPage(spuDTO));
    }

}
