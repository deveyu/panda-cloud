package com.ydc.basepack.controller;


import com.ydc.basepack.model.query.SpecGroupQuery;
import com.ydc.basepack.service.SpecGroupService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pc
 */
@RestController
@RequestMapping("spec/group")
@Api(value = "规格组controller", tags = {"规格组操作接口"})
public class SpecGroupController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private SpecGroupService specGroupService;


    @ApiOperation(value = "规格组信息查询", notes = "按照分类id查询规格组信息", httpMethod = "GET")
    @ApiImplicitParam(name = "specGroupQuery", value = "规格组信息查询类", required = false, dataType = "specGroupQuery")
    @GetMapping("of/category")
    public ApiResult<SpecGroupQuery> pageByQuery(SpecGroupQuery query){
        return new ApiResult<>(specGroupService.pageSpecGroupByCategory(query));
    }

}
