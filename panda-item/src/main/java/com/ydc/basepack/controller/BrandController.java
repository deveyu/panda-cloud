package com.ydc.basepack.controller;


import com.ydc.basepack.model.query.BrandQuery;
import com.ydc.basepack.service.BrandService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
@Api(value = "品牌controller", tags = {"品牌操作接口"})
public class BrandController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private BrandService brandService;


    @ApiOperation(value = "品牌信息分页查询", notes = "品牌信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "brandQuery", value = "品牌信息查询类", required = false, dataType = "brandQuery")
    @GetMapping("/page")
    public ApiResult<List<BrandQuery>> pageByQuery(BrandQuery brandQuery){
        return new ApiResult<>(brandService.pageByQuery(brandQuery));
    }

}
