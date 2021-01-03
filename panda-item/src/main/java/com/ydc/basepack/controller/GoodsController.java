package com.ydc.basepack.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.model.dto.CategoryTree;
import com.ydc.basepack.model.dto.SkuDTO;
import com.ydc.basepack.model.dto.SpuDTO;
import com.ydc.basepack.model.dto.SpuDetailDTO;
import com.ydc.basepack.model.entity.Spu;
import com.ydc.basepack.model.query.SpuQuery;
import com.ydc.basepack.service.CategoryService;
import com.ydc.basepack.service.GoodsService;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pc
 */
@RestController
@RequestMapping("goods")
@Api(value = "商品controller", tags = {"商品操作接口"})
public class GoodsController {
    private static final String MODULE_NAME = "商品模块";

    @Autowired
    private GoodsService goodsService;


    @ApiOperation(value = "商品信息分页查询", notes = "商品信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "goodsQuery", value = "商品信息查询类", required = false, dataType = "goodsQuery")
    @GetMapping("page")
    public ApiResult<SpuQuery> queryByPage(SpuQuery spuQuery) {
        return new ApiResult<>(goodsService.queryByPage(spuQuery));
    }

    @ApiOperation(value = "商品详情查询", notes = "根据spuId查询商品详情", httpMethod = "GET")
    @ApiImplicitParam(name = "spuDetailQuery", value = "商品详情查询", required = false, dataType = "spuDetailQuery")
    @GetMapping("/spu/detail/{spuId}")
    public ApiResult<SpuDetailDTO> querySpuDetailById(@PathVariable("spuId") Long id) {
        return new ApiResult<>(goodsService.querySpuDetailById(id));
    }

    @ApiOperation(value = "商品详情查询", notes = "根据spuId查询商品详情", httpMethod = "GET")
    @ApiImplicitParam(name = "spuDetailQuery", value = "商品详情查询", required = false, dataType = "spuDetailQuery")
    @PutMapping("/seal")
    public ApiResult<Boolean> upAndDownSpu(@RequestBody Spu spu) {
        return new ApiResult<Boolean>(goodsService.upAndDownSpu(spu));
    }



    @ApiOperation(value = "商品sku查询", notes = "根据spuId查询商品sku", httpMethod = "GET")
    @ApiImplicitParam(name = "skuQuery", value = "商品详情查询", required = false, dataType = "skuQuery")
    @GetMapping("sku/of/spu")
    public ApiResult<List<SkuDTO>> querySkuBySpuId(@RequestParam("id") Long id) {
        return new ApiResult<>(goodsService.querySkuBySpuId(id));
    }


}
