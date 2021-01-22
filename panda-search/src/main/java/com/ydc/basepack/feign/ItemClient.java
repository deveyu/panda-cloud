package com.ydc.basepack.feign;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.feign.model.*;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * feign的用法：
 * 首先调用方被调用方都要注册到注册中心
 * 调用方copy被调用的接口声明（在这里就是该接口）；或者被调用方的接口把所有需要对外提供的接口写到一个client中：ItemClient，以jar包的形式给调用方
 * 有了feignClient,可以说三层才完整，controller层终于有接口了！
 */
@FeignClient(value = "item-service")
public interface ItemClient {



    @ApiOperation(value = "商品sku查询", notes = "根据spuId查询商品sku", httpMethod = "GET")
    @ApiImplicitParam(name = "skuQuery", value = "商品详情查询", required = false, dataType = "skuQuery")
    @GetMapping("sku/of/spu")
    ApiResult<List<SkuDTO>> querySkuBySpuId(@RequestParam("id") Long id);


    @ApiOperation(value = "根据spuId查询分类", notes = "添加根据spuId查询分类分类", httpMethod = "GET")
    @ApiImplicitParam(name = "categoryQuery", value = "查询分类", required = false, dataType = "categoryAdd")
    @GetMapping("/category/of/spuIds")
    ApiResult<List<Category>> queryCategoryBySpuId(List<Long> ids);


    @ApiOperation(value = "根据id查询品牌", notes = "根据id查询品牌", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "Long")
    @GetMapping("id/{id}")
    ApiResult<Brand> queryBrandById(@PathVariable("id") Long id);

    @ApiOperation(value = "规格参数信息查询", notes = "按照分类id查询规格参数信息", httpMethod = "GET")
    @ApiImplicitParam(name = "specParamQuery", value = "规格组信息查询类", required = false, dataType = "specParamQuery")
    @GetMapping("of/category/{cid}")
    ApiResult<List<SpecParamDTO>> getSpecParamByCid(@PathVariable("cid") Long cid);


    @ApiOperation(value = "商品信息分页查询", notes = "商品信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "goodsQuery", value = "商品信息查询类", required = false, dataType = "goodsQuery")
    @GetMapping("page")
    ApiResult<SpuQuery> queryByPage(SpuQuery spuQuery);


    @ApiOperation(value = "商品详情(规格参数值)查询", notes = "根据spuId查询商品详情", httpMethod = "GET")
    @ApiImplicitParam(name = "spuDetailQuery", value = "商品详情查询", required = false, dataType = "spuDetailQuery")
    @GetMapping("/spu/detail/{spuId}")
    ApiResult<SpuDetailDTO> querySpuDetailById(@PathVariable("spuId") Long id);




}
