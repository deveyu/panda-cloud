package com.ydc.basepack.feign;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ydc.basepack.feign.model.SkuDTO;
import com.ydc.basepack.feign.model.SpuDTO;
import com.ydc.basepack.feign.model.SpuDetailDTO;
import com.ydc.basepack.feign.model.SpuQuery;
import com.yukong.panda.common.util.ApiResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "item-service")
public interface ItemClient {

    @ApiOperation(value = "商品信息分页查询", notes = "商品信息分页查询", httpMethod = "GET")
    @ApiImplicitParam(name = "goodsQuery", value = "商品信息查询类", required = false, dataType = "goodsQuery")
    @GetMapping("page")
    ApiResult<IPage<SpuDTO>> queryByPage(SpuQuery spuQuery);


    @ApiOperation(value = "商品详情查询", notes = "根据spuId查询商品详情", httpMethod = "GET")
    @ApiImplicitParam(name = "spuDetailQuery", value = "商品详情查询", required = false, dataType = "spuDetailQuery")
    @GetMapping("/spu/detail/{spuId}")
    public ApiResult<SpuDetailDTO> querySpuDetailById(@PathVariable("spuId") Long id) ;


    @ApiOperation(value = "商品sku查询", notes = "根据spuId查询商品sku", httpMethod = "GET")
    @ApiImplicitParam(name = "skuQuery", value = "商品详情查询", required = false, dataType = "skuQuery")
    @GetMapping("sku/of/spu")
    public ApiResult<List<SkuDTO>> querySkuBySpuId(@RequestParam("id") Long id) ;




}
