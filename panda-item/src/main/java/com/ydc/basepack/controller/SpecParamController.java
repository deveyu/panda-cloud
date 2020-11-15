package com.ydc.basepack.controller;


import com.ydc.basepack.model.dto.SpecParamDTO;
import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.model.query.SpecParamQuery;
import com.ydc.basepack.service.SpecGroupService;
import com.ydc.basepack.service.SpecParamService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pc
 */
@RestController
@RequestMapping("spec/param")
@Api(value = "规格参数controller", tags = {"规格参数操作接口"})
public class SpecParamController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private SpecParamService specParamService;


    @ApiOperation(value = "规格参数信息查询", notes = "按照规格组id查询规格参数信息", httpMethod = "GET")
    @ApiImplicitParam(name = "specParamQuery", value = "规格组信息查询类", required = false, dataType = "specParamQuery")
    @GetMapping("of/specGroup/{gid}")
    public ApiResult<List<SpecParamDTO>> getSpecParamByGid(@PathVariable("gid") Long gid){
        return new ApiResult<>(specParamService.getSpecParamByGid(gid));
    }

}
